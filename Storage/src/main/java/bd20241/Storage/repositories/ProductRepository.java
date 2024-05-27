package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import bd20241.Storage.models.Product;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Product product) {
        String sql = "INSERT INTO product (id, prod_name ) VALUES (?, ?)";
        jdbcTemplate.update(sql, product.getId(), product.getName());
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, new ProductRowMapper());
    }

    public Product findById(String id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new ProductRowMapper(), id);
    }

    public void update(Product product) {
        String sql = "UPDATE product SET prod_name = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getId());
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getString("id"));
            product.setName(rs.getString("prod_name"));

            return product;
        }
    }
    
}
