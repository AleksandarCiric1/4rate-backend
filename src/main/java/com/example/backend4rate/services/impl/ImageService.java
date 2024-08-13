package com.example.backend4rate.services.impl;

import org.springframework.stereotype.Service;

import com.example.backend4rate.models.entities.ImageEntity;
import com.example.backend4rate.repositories.ImageRepository;
import com.example.backend4rate.services.ImageServiceInterface;

@Service
public class ImageService implements ImageServiceInterface{

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void doSomethingTest(){
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(1);
        imageEntity.setImageUrl("asss");
        this.imageRepository.saveAndFlush(imageEntity);
    }

}
