package com.example.backend4rate.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.services.impl.ImageService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("v1/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService){
        this.imageService = imageService;
    }

   @PostMapping("/uploadRestaurantImages/{id}")
    public ResponseEntity<?> uploadImages(@PathVariable Integer id, @RequestParam("files")List<MultipartFile> files) throws IOException, NotFoundException {
        imageService.uploadImage(files ,id);
        return  ResponseEntity.ok().build();
    }

    @GetMapping("/getImages/{id}")
    public List<String> getImages(@PathVariable Integer id) throws NullPointerException{
        return imageService.getImages(id);
     }

     @PostMapping("/uploadAvatar/{id}")
     public ResponseEntity<?> uploadAvatar(@PathVariable Integer id, @RequestParam("files") MultipartFile file) throws IOException, NotFoundException {
         imageService.uploadAvatar(file ,id);
         return  ResponseEntity.ok().build();
     }
}
