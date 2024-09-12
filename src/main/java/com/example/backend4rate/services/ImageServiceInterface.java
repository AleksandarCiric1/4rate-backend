package com.example.backend4rate.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend4rate.exceptions.NotFoundException;

public interface ImageServiceInterface{
    public void uploadImage(List<MultipartFile> imageFile, Integer id) throws IOException, NotFoundException;

    public List<String> getImages(Integer id) throws NullPointerException;

    public void uploadAvatar(MultipartFile imageFile, Integer id) throws IOException, NotFoundException;

    public Resource getAvatar(Integer id) throws NotFoundException, MalformedURLException;
}
