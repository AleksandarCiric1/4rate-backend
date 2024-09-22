package com.example.backend4rate.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

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

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("v1/images")
public class ImageController {
    private final ImageService imageService;
    private final String pathToRestaurant = "src/main/resources/restorani/";

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/uploadRestaurantImages/{id}")
    public ResponseEntity<?> uploadImages(@PathVariable Integer id, @RequestParam("files") List<MultipartFile> files)
            throws IOException, NotFoundException {
        imageService.uploadImage(files, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getImages/{idRestaurant}")
    public ResponseEntity<List<String>> getImageUrls(@PathVariable Integer idRestaurant) throws MalformedURLException {
        List<Resource> resources = imageService.getImages(idRestaurant);
        List<String> imageUrls = resources.stream()
                .map(resource -> resource.getFilename()) // Assume filename is the URL or can be used to build URL
                .map(filename -> pathToRestaurant + idRestaurant + "/" + filename) // Build URL
                .collect(Collectors.toList());
        return ResponseEntity.ok(imageUrls);
    }

    @GetMapping("/getImage/{id}/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable Integer id, @PathVariable String imageName)
            throws NotFoundException, MalformedURLException {
        Resource resource = imageService.getImage(id, imageName);

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteImage/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable Integer id) throws NotFoundException, IOException {
        imageService.deleteImage(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/uploadAvatar/{id}")
    public ResponseEntity<?> uploadAvatar(@PathVariable Integer id, @RequestParam("file") MultipartFile file)
            throws IOException, NotFoundException {
        return ResponseEntity.ok().body(imageService.uploadAvatar(file, id));
    }

    @GetMapping("/getAvatar/{id}")
    public ResponseEntity<Resource> getAvatar(@PathVariable Integer id)
            throws NotFoundException, MalformedURLException {
        Resource resource = imageService.getAvatar(id);

        if (resource != null && (resource.exists() || resource.isReadable())) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getAvatar")
    public ResponseEntity<Resource> getAvatarBYAvatarUrl(@RequestParam("avatarUrl") String avatarUrl)
            throws NotFoundException, MalformedURLException {
        Resource resource = imageService.getAvatarByAvatarUrl(avatarUrl);

        if (resource != null && (resource.exists() || resource.isReadable())) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/deleteAvatar/{id}")
    public ResponseEntity<?> deleteAvatar(@PathVariable Integer id) throws NotFoundException, IOException {
        imageService.deleteAvatar(id);
        return ResponseEntity.ok().build();
    }

}
