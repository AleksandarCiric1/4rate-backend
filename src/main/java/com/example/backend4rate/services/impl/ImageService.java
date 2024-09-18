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
import com.example.backend4rate.models.entities.RestaurantPhoneEntity;
import com.example.backend4rate.models.entities.UserAccountEntity;
import com.example.backend4rate.repositories.ImageRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.repositories.UserAccountRepository;
import com.example.backend4rate.services.ImageServiceInterface;

@Service
public class ImageService implements ImageServiceInterface {

    private final String pathToAvatar = "src/main/resources/avatars/";
    private final String pathToRestaurant = "src/main/resources/restaurants/";
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
        deleteImagesForRestaurant(restaurantEntity);
        for (MultipartFile imageFile : imageFiles) {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setImageUrl(uniqueFileName);
            imageEntity.setRestaurant(restaurantEntity);
            imageEntity.setId(null);

            imageRepository.save(imageEntity);

            Path uploadPath = Path.of(pathToRestaurant + restaurantEntity.getId());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(uniqueFileName);

            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private boolean deleteImagesForRestaurant(RestaurantEntity restaurantEntity) throws NotFoundException, IOException {
        List<ImageEntity> imageEntities = imageRepository.findAllByRestaurant(restaurantEntity);

        for (ImageEntity elem : imageEntities) {
            deleteImage(elem.getId());
        }

        return true;
    }

    @Override
    public List<Resource> getImages(Integer idRestaurant) throws MalformedURLException {
        List<Resource> resources = new ArrayList<>();

        File dir = new File(pathToRestaurant + idRestaurant);
        File[] images = dir.listFiles();
        List<Path> paths = new ArrayList<>();

        for (File image : images) {
            paths.add(image.toPath());
        }
        for (Path path : paths) {
            resources.add(new UrlResource(path.toUri()));
        }
        return resources;
    }

    @Override
    public Resource getImage(Integer id, String imageName) throws NotFoundException, MalformedURLException {
        String dir = pathToRestaurant + id;
        Path path = Path.of(dir).resolve(imageName);
        return new UrlResource(path.toUri());
    }

    @Override
    public void deleteImage(Integer id) throws NotFoundException, IOException {
        ImageEntity imageEntity = imageRepository.findById(id).orElseThrow(NotFoundException::new);

        String filePath = pathToRestaurant + imageEntity.getRestaurant().getId() + "/" + imageEntity.getImageUrl();
        Path path = Path.of(filePath);
        try {
            Files.delete(path);
        } catch (IOException ex) {
        }

        imageRepository.deleteById(id);
    }

    @Override
    public String uploadAvatar(MultipartFile imageFile, Integer id) throws IOException, NotFoundException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElseThrow(NotFoundException::new);
        String avatar = userAccountEntity.getAvatarUrl();

        if (avatar != null) {
            this.deleteAvatar(id);
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

        userAccountEntity.setAvatarUrl(uniqueFileName);
        userAccountRepository.save(userAccountEntity);

        Path uploadPath = Path.of(pathToAvatar);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @Override
    public Resource getAvatar(Integer id) throws NotFoundException, MalformedURLException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElseThrow(NotFoundException::new);
        String avatar = userAccountEntity.getAvatarUrl();
        if (avatar != null) {
            Path path = Paths.get(pathToAvatar).resolve(avatar);
            return new UrlResource(path.toUri());
        }
        return null;
    }

    @Override
    public void deleteAvatar(Integer id) throws NotFoundException, IOException {
        UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElseThrow(NotFoundException::new);
        String avatar = userAccountEntity.getAvatarUrl();

        if (avatar != null) {
            String filePath = pathToAvatar + avatar;
            Path path = Paths.get(filePath);
            try {
                Files.delete(path);
            } catch (IOException ex) {
            }
        }

        userAccountEntity.setAvatarUrl(null);
        userAccountRepository.save(userAccountEntity);
    }

}
