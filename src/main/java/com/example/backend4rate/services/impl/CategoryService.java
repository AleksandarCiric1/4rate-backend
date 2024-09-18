package com.example.backend4rate.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Category;
import com.example.backend4rate.models.entities.CategoryEntity;
import com.example.backend4rate.repositories.CategoryRepository;
import com.example.backend4rate.services.CategoryServiceInterface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService implements CategoryServiceInterface {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Category> getAll() throws NotFoundException {
        List<CategoryEntity> categoryEntities = categoryRepository.findByStatus(true);
        return categoryEntities.stream().map(elem -> modelMapper.map(elem, Category.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean activateCategory(Integer id) throws NotFoundException {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Couldn't found category!"));

        categoryEntity.setStatus(true);
        categoryRepository.saveAndFlush(categoryEntity);
        return true;
    }

    @Override
    public boolean blockCategory(Integer id) throws NotFoundException {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Couldn't found category!"));

        categoryEntity.setStatus(false);
        categoryRepository.saveAndFlush(categoryEntity);
        return true;
    }

    @Override
    public Category addCategory(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(category.getName());
        categoryEntity.setStatus(true);
        categoryRepository.saveAndFlush(categoryEntity);
        entityManager.refresh(categoryEntity);

        return modelMapper.map(categoryEntity, Category.class);
    }

    @Override
    public Category editCategory(Category category) throws NotFoundException {
        CategoryEntity categoryEntity = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new NotFoundException("Couldn't found category"));

        modelMapper.map(category, categoryEntity);

        return modelMapper.map(categoryRepository.save(categoryEntity), Category.class);
    }

}
