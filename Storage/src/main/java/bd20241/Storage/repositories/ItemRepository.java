package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import bd20241.Storage.models.Item;
import bd20241.Storage.payloads.requests.CreateItemRequest;

@Repository
public class ItemRepository {
    private final JdbcTemplate jdbcTemplate;

    public ItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Item save(CreateItemRequest item, String cpf, String id) {
        String sql = "INSERT INTO item (id, product_id, supplier_cnpj, employee_cpf) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, id, item.getProductId(), item.getSupplierCnpj(), cpf);
        String sql2 = "SELECT * FROM item WHERE id = ?";
        return jdbcTemplate.queryForObject(sql2, new ItemRowMapper(), id);
    }

    public List<Item> findAll() {
        String sql = "SELECT * FROM item";
        return jdbcTemplate.query(sql, new ItemRowMapper());
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    private static class ItemRowMapper implements RowMapper<Item> {
        @Override
        public Item mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Item item = new Item();
            item.setId(rs.getString("id"));
            item.setProductId(rs.getString("product_id"));
            item.setSupplierCnpj(rs.getString("supplier_cnpj"));
            item.setEmployeeCpf(rs.getString("employee_cpf"));
            item.setCreatedAt(rs.getDate("created_at"));
            item.setUpdatedAt(rs.getDate("updated_at"));
            return item;
        }
    }
    
}
