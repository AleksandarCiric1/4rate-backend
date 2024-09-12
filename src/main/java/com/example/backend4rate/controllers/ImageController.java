package com.example.backend4rate.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



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

     @PutMapping("/uploadAvatar/{id}")
     public ResponseEntity<?> uploadAvatar(@PathVariable Integer id, @RequestParam("files") MultipartFile file) throws IOException, NotFoundException {
         imageService.uploadAvatar(file ,id);
         return  ResponseEntity.ok().build();
     }

     @GetMapping("/getAvatar/{id}")
     public ResponseEntity<Resource> getAvatar(@PathVariable Integer id) throws NotFoundException, MalformedURLException {
        Resource resource = imageService.getAvatar(id);

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
     }
     
}
