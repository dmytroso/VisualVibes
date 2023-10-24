package com.vv.VisualVibes.service;

import com.vv.VisualVibes.entity.Image;
import com.vv.VisualVibes.entity.Post;
import com.vv.VisualVibes.entity.User;
import com.vv.VisualVibes.exception.ImageNotFoundException;
import com.vv.VisualVibes.repository.ImageRepository;
import com.vv.VisualVibes.repository.PostRepository;
import com.vv.VisualVibes.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageUploadService {
    private static final Logger LOG = LoggerFactory.getLogger(ImageUploadService.class);

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Autowired
    public ImageUploadService(ImageRepository imageRepository, PostRepository postRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    public Image uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        LOG.info("Uploading image profile to User {}", user.getUsername());

        Image userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }

        Image image = new Image();
        image.setUserId(user.getId());
        image.setImageBytes(compressBytes(file.getBytes()));
        image.setName(file.getOriginalFilename());
        return imageRepository.save(image);
    }

    public Image uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        User user = getUserByPrincipal(principal);
        Post post = user.getPosts()
                .stream()
                .filter(p -> p.getId().equals(postId))
                .collect(toSinglePostCollector());

        Image image = new Image();
        image.setPostId(post.getId());
        image.setImageBytes(file.getBytes());
        image.setImageBytes(compressBytes(file.getBytes()));
        image.setName(file.getOriginalFilename());
        LOG.info("Upload image to Post {}", post.getId());

        return imageRepository.save(image);
    }

    //TODO: Post without image
    /*
    Checking !ObjectUtils.isEmpty(image) exception if post doesn't have image
    In future need refactoring this code for post without image
     */
    public Image getImageToUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        Image image = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(compressBytes(image.getImageBytes()));
        }
        return image;
    }

    public Image getImageToPost(Long postId) {
        Image image = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Cannot find image to Post: " + postId));
        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(compressBytes(image.getImageBytes()));
        }
        return image;
    }

    private <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
            if (list.size() != 1) {
                throw new IllegalStateException();
            }
            return list.get(0);
        });
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot decompress bytes");
        }
        return outputStream.toByteArray();
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username" + username));
    }
}
