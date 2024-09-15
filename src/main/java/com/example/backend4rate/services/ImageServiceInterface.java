package com.example.backend4rate.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend4rate.exceptions.NotFoundException;

public interface ImageServiceInterface {
    void uploadImage(List<MultipartFile> imageFile, Integer id) throws IOException, NotFoundException;

    List<Resource> getImages(Integer idRestaurant) throws MalformedURLException;

    Resource getImage(Integer id, String imageName) throws NotFoundException, MalformedURLException;

    void deleteImage(Integer id) throws NotFoundException, IOException;

    String uploadAvatar(MultipartFile imageFile, Integer id) throws IOException, NotFoundException;

    Resource getAvatar(Integer id) throws NotFoundException, MalformedURLException;

    void deleteAvatar(Integer id) throws NotFoundException, IOException;
}
