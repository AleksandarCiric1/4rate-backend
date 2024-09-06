package com.example.backend4rate.services;

import java.util.List;

import com.example.backend4rate.exceptions.EmptyResultDataAccessException;
import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Comment;

public interface CommentServiceInterface {

    public Comment addCommment(Integer restaurantId, Comment comment) throws NotFoundException;

    public void deleteCommentById(Integer commentId) throws EmptyResultDataAccessException ;

    public Comment getCommentById(Integer commentId) throws NotFoundException;

    public List<Comment> getAllComments(Integer restauranId) throws NotFoundException; 
    
}
