package com.example.backend4rate.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.entities.ImageEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.models.entities.UserAccountEntity;
import com.example.backend4rate.repositories.ImageRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.repositories.UserAccountRepository;
import com.example.backend4rate.services.ImageServiceInterface;

@Service
public class ImageService implements ImageServiceInterface {

    private final ImageRepository imageRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserAccountRepository userAccountRepository;

    public ImageService(ImageRepository imageRepository, RestaurantRepository restaurantRepository,
            UserAccountRepository userAccountRepository) {
        this.imageRepository = imageRepository;
        this.restaurantRepository = restaurantRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public void uploadImage(List<MultipartFile> imageFiles, Integer id) throws IOException, NotFoundException {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(id).orElseThrow(NotFoundException::new);

        for (MultipartFile imageFile : imageFiles) {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setImageUrl(uniqueFileName);
            imageEntity.setRestaurant(restaurantEntity);
            imageEntity.setId(null);

            imageRepository.save(imageEntity);

            Path uploadPath = Path.of("uploads/" + restaurantEntity.getId());
            Path filePath = uploadPath.resolve(uniqueFileName);

            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    public List<String> getImages(Integer id) throws NullPointerException {
        List<String> nameOfImages = new ArrayList<>();

        File dir = new File("uploads/" + id);
        File[] images = dir.listFiles();

        for (File image : images) {
            nameOfImages.add(image.getName());
        }

        return nameOfImages;
    }

    public void uploadAvatar(MultipartFile imageFile, Integer id) throws IOException, NotFoundException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElseThrow(NotFoundException::new);

        String avatar = userAccountEntity.getAvatarUrl();
        if (avatar != null) {
            String filePath = "src/main/resources/avatars/" + avatar;

            Path path = Paths.get(filePath);
            try {
                Files.delete(path);
            } catch (IOException e) {

            }
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

        userAccountEntity.setAvatarUrl(uniqueFileName);
        userAccountRepository.save(userAccountEntity);

        Path uploadPath = Path.of("src/main/resources/avatars/");
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public Resource getAvatar(Integer id) throws NotFoundException, MalformedURLException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElseThrow(NotFoundException::new);
        String avatar = userAccountEntity.getAvatarUrl();
        Path path = Paths.get("src/main/resources/avatars/").resolve(avatar);
        return new UrlResource(path.toUri());
    }
}
