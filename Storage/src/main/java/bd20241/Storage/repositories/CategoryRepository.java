package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import bd20241.Storage.models.Category;

@Repository
public class CategoryRepository {
    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Category category) {
        String sql = "INSERT INTO category (id, cat_name) VALUES (?, ?)";
        jdbcTemplate.update(sql, category.getId(), category.getName());
    }

    public List<Category> findAll() {
        String sql = "SELECT * FROM category";
        return jdbcTemplate.query(sql, new CategoryRowMapper());
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM category WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateName(String id, String name) {
        String sql = "UPDATE category SET cat_name = ? WHERE id = ?";
        jdbcTemplate.update(sql, name, id);
    }

    public Category findByName(String name) {
        String sql = "SELECT * FROM category WHERE cat_name = ?";
        return jdbcTemplate.queryForObject(sql, new CategoryRowMapper(), name);
    }

    public Category findById(String id) {
        String sql = "SELECT * FROM category WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new CategoryRowMapper(), id);
    }

    private static class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Category category = new Category();
            category.setId(rs.getString("id"));
            category.setName(rs.getString("cat_name"));
            return category;
        }
    }
    
}
