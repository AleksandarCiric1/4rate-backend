package com.example.backend4rate.controllers;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Comment;
import com.example.backend4rate.services.impl.CommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/v1/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService=commentService;
    }

    @PostMapping("/add/{restaurantId}")
    public Comment addComment(@PathVariable Integer restaurantId, @RequestBody Comment comment) throws NotFoundException{
        return commentService.addCommment(restaurantId, comment);
    }

    @GetMapping("/get/{commentId}")
    public Comment getComment(@PathVariable Integer commentId) throws NotFoundException
    {
       return commentService.getCommentById(commentId);
    }
        

    @GetMapping("/getAll/{restaurantId}")
    public List<Comment> getComments(@PathVariable Integer restaurantId) throws NotFoundException {
        return this.commentService.getAllComments(restaurantId);
    }
    
    @DeleteMapping("/delete/{commentId}")
    public void deleteComment(@PathVariable Integer commentId) throws EmptyResultDataAccessException{
        commentService.deleteCommentById(commentId);
    }
}
