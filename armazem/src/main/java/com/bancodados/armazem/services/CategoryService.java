package com.bancodados.armazem.services;


import com.bancodados.armazem.models.Category;
import com.bancodados.armazem.requests.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
public class CategoryService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Category createCategory(@RequestBody CategoryRequest request) {

        try {
            String uuid = UUID.randomUUID().toString();
            String sql = "INSERT INTO category (id, cat_name) VALUES (?, ?)";
            jdbcTemplate.update(sql, uuid, request.getName());
            return new Category(uuid, request.getName());
        } catch (Exception e) {
            return null;
        }

    }
}
