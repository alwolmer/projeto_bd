package com.bancodados.armazem.controllers;

import com.bancodados.armazem.models.Category;
import com.bancodados.armazem.payload.request.TokenRefreshRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

//    @PostMapping
//    public ResponseEntity<Category> createCategory(@RequestBody TokenRefreshRequest.CategoryRequest request) {
//        Category createdCategory = categoryService.createCategory(request);
//        if (createdCategory == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Category>> getAllCategories() {
//        return ResponseEntity.ok(categoryService.getAllCategories());
//    }

}
