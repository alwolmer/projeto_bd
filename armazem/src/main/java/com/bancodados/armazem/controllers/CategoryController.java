package com.bancodados.armazem.controllers;

import com.bancodados.armazem.models.Category;
import com.bancodados.armazem.requests.CategoryRequest;
import com.bancodados.armazem.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest request) {
        Category createdCategory = categoryService.createCategory(request);
        if (createdCategory == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

}
