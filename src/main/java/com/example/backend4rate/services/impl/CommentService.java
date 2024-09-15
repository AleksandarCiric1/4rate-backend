package com.example.backend4rate.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Comment;
import com.example.backend4rate.models.entities.CommentEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.repositories.CommentRepository;
import com.example.backend4rate.repositories.GuestRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
// import com.example.backend4rate.repositories.StandardUserRepository;
import com.example.backend4rate.repositories.UserAccountRepository;
import com.example.backend4rate.services.CommentServiceInterface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class CommentService implements CommentServiceInterface {

    private final CommentRepository commentRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserAccountRepository userAccountRepository;
    // private final StandardUserRepository standardUserRepository;
    private final ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public CommentService(CommentRepository commentRepository, RestaurantRepository restaurantRepository,
            GuestRepository guestRepository,
            UserAccountRepository userAccountRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.userAccountRepository = userAccountRepository;
        // this.standardUserRepository=standardUserRepository;
        this.modelMapper = modelMapper;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Comment addCommment(Integer restaurantId, Comment comment) throws NotFoundException {
        CommentEntity commentEntity = modelMapper.map(comment, CommentEntity.class);
        commentEntity.setId(null);
        commentEntity.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(NotFoundException::new));
        // commentEntity.setStandardUser(standardUserRepository.findByUserAccount(userAccountRepository.findByUsername(comment.getStandardUserUserAccountUsername())));
        commentEntity = commentRepository.saveAndFlush(commentEntity);
        return comment;
    }

    @Override
    public void deleteCommentById(Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment getCommentById(Integer commentId) throws NotFoundException {
        return modelMapper.map(commentRepository.findById(commentId).orElseThrow(NotFoundException::new),
                Comment.class);
    }

    @Override
    public List<Comment> getAllComments(Integer restaurantId) throws NotFoundException {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundException::new);
        return commentRepository.findByRestaurant(restaurantEntity).stream().map(l -> modelMapper.map(l, Comment.class))
                .collect(Collectors.toList());
    }

}
