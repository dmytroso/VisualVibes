package com.vv.VisualVibes.facade;

import com.vv.VisualVibes.dto.CommentDTO;
import com.vv.VisualVibes.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {
    public CommentDTO commentToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setUsername(comment.getUsername());
        commentDTO.setMessage(comment.getMessage());
        return commentDTO;
    }
}
