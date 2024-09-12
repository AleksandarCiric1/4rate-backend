package com.example.backend4rate.services;

import java.util.List;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Category;

public interface CategoryServiceInterface {
    List<Category> getAll() throws NotFoundException;
    boolean activateCategory(Integer id) throws NotFoundException;
    boolean blockCategory(Integer id) throws NotFoundException;
    Category addCategory(Category category);
    Category editCategory(Category category) throws NotFoundException;
}
