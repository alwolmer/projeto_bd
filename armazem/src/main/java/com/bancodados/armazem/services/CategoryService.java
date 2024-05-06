package com.bancodados.armazem.services;


import com.bancodados.armazem.models.Category;
import com.bancodados.armazem.payload.request.TokenRefreshRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

//    public Category createCategory(@RequestBody TokenRefreshRequest.CategoryRequest request) {
//
//        try {
//            String uuid = UUID.randomUUID().toString();
//            String sql = "INSERT INTO category (id, cat_name) VALUES (?, ?)";
//            jdbcTemplate.update(sql, uuid, request.getName());
//            return new Category(uuid, request.getName());
//        } catch (Exception e) {
//            return null;
//        }
//
//    }
//
//    public List<Category> getAllCategories() {
//        String sql = "SELECT * FROM category";
//        return jdbcTemplate.query(sql, new CategoryRowMapper());
//    }
//
//    private static class CategoryRowMapper implements RowMapper<Category> {
//        @Override
//        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
//            String id = rs.getString("id");
//            String name = rs.getString("cat_name");
//            return new Category(id, name);
//        }
//    }
}
