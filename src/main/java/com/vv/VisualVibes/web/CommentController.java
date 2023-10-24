package com.vv.VisualVibes.web;

import com.vv.VisualVibes.dto.CommentDTO;
import com.vv.VisualVibes.entity.Comment;
import com.vv.VisualVibes.facade.CommentFacade;
import com.vv.VisualVibes.payload.response.MessageResponse;
import com.vv.VisualVibes.service.CommentService;
import com.vv.VisualVibes.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/comment")
@CrossOrigin
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentFacade commentFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDTO commentDTO,
                                                @PathVariable("postId") String postId,
                                                BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Comment comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal);
        CommentDTO createdComment = commentFacade.commentToCommentDTO(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

    @PostMapping("{postId}/all")
    public ResponseEntity<List<CommentDTO>> getAllCommentToPost(@PathVariable("postId") String postId) {
        List<CommentDTO> commentDTOList = commentService.getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(commentFacade::commentToCommentDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PostMapping("{commentId}/delete")
    public ResponseEntity<MessageResponse> deleyeComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Comment was delete"), HttpStatus.OK);
    }
}
