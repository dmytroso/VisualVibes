package com.vv.VisualVibes.service;

import com.vv.VisualVibes.dto.CommentDTO;
import com.vv.VisualVibes.entity.Comment;
import com.vv.VisualVibes.entity.Post;
import com.vv.VisualVibes.entity.User;
import com.vv.VisualVibes.exception.PostNotFoundException;
import com.vv.VisualVibes.repository.CommentRepository;
import com.vv.VisualVibes.repository.PostRepository;
import com.vv.VisualVibes.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final Logger LOG = LoggerFactory.getLogger(CommentService.class);


    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comment saveComment(Long id, CommentDTO commentDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + user.getEmail()));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setId(id);
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());

        LOG.info("Saving comment for Post: {}", post.getId());

        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        List<Comment> comments = commentRepository.findAllByPost(post);
        return comments;
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username" + username));
    }
}
