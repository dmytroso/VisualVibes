package com.vv.VisualVibes.web;

import com.vv.VisualVibes.entity.Image;
import com.vv.VisualVibes.payload.response.MessageResponse;
import com.vv.VisualVibes.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/image")
@CrossOrigin
public class ImageUploadController {
    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageUploadService.uploadImageToUser(file, principal);
        return new ResponseEntity<>(new MessageResponse("Image uploaded Successfully"), HttpStatus.OK);
    }

    @PostMapping("{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") String postId,
                                                             @RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageUploadService.uploadImageToPost(file,principal,Long.parseLong(postId));
        return new ResponseEntity<>(new MessageResponse("Image uploaded Successfully"), HttpStatus.OK);
    }

    @GetMapping("profileImage")
    public ResponseEntity<Image> getImageForUser(Principal principal) {
        Image userImage = imageUploadService.getImageToUser(principal);
        return new ResponseEntity<>(userImage, HttpStatus.OK);
    }

    @GetMapping("{postId}/image")
    public ResponseEntity<Image> getImageToPost(@PathVariable("postId") String postId) {
        Image postImage = imageUploadService.getImageToPost(Long.parseLong(postId));
        return new ResponseEntity<>(postImage,HttpStatus.OK);
    }
}
