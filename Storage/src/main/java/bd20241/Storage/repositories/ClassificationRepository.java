package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import bd20241.Storage.models.Classification;

@Repository
public class ClassificationRepository {
    private final JdbcTemplate jdbcTemplate;

    public ClassificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Classification classification) {
        String sql = "INSERT INTO classification (product_id, category_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, classification.getProductId(), classification.getCategoryId());
    }

    public void deleteByProductId(String productId) {
        String sql = "DELETE FROM classification WHERE product_id = ?";
        jdbcTemplate.update(sql, productId);
    }

    public void deleteByCategoryId(String categoryId) {
        String sql = "DELETE FROM classification WHERE category_id = ?";
        jdbcTemplate.update(sql, categoryId);
    }

    public void deleteByProductIdAndCategoryId(String productId, String categoryId) {
        String sql = "DELETE FROM classification WHERE product_id = ? AND category_id = ?";
        jdbcTemplate.update(sql, productId, categoryId);
    }

    public List<Classification> findAll() {
        String sql = "SELECT * FROM classification";
        return jdbcTemplate.query(sql, new ClassificationRowMapper());
    }

    public List<Classification> findByProductId(String productId) {
        String sql = "SELECT * FROM classification WHERE product_id = ?";
        return jdbcTemplate.query(sql, new ClassificationRowMapper(), productId);
    }

    public List<Classification> findByCategoryId(String categoryId) {
        String sql = "SELECT * FROM classification WHERE category_id = ?";
        return jdbcTemplate.query(sql, new ClassificationRowMapper(), categoryId);
    }

    public boolean existsByProductIdAndCategoryId(String productId, String categoryId) {
        String sql = "SELECT COUNT(*) FROM classification WHERE product_id = ? AND category_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, productId, categoryId) > 0;
    }

    private static class ClassificationRowMapper implements RowMapper<Classification> {
        @Override
        public Classification mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Classification classification = new Classification();
            classification.setProductId(rs.getString("product_id"));
            classification.setCategoryId(rs.getString("category_id"));
            return classification;
        }
    }
    
}
