package com.example.backend4rate.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend4rate.exceptions.NotFoundException;
import com.example.backend4rate.models.dto.Category;
import com.example.backend4rate.services.CategoryServiceInterface;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoryServiceInterface categoryService;

    public CategoryController(CategoryServiceInterface categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCategories() throws NotFoundException {
        return ResponseEntity.ok().body(categoryService.getAll());

    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activateCategory(@PathVariable Integer id) throws NotFoundException {
        if (categoryService.activateCategory(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<?> blockCategory(@PathVariable Integer id) throws NotFoundException {
        if (categoryService.blockCategory(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.addCategory(category);
        if (createdCategory != null) {
            return ResponseEntity.ok().body(createdCategory);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editCategory(@RequestBody Category category) throws NotFoundException {
        Category editedCategory = categoryService.editCategory(category);
        if (editedCategory != null) {
            return ResponseEntity.ok().body(editedCategory);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
