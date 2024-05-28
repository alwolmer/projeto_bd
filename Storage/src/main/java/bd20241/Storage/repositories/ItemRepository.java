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
import bd20241.Storage.payloads.responses.StockStatsResponse;

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

    public List<Item> findStock() {
        String sql = "SELECT * FROM current_stock";
        return jdbcTemplate.query(sql, new ItemRowMapper());
    }

    public List<StockStatsResponse> findStockStats() {
        String sql = "SELECT DATE(i.created_at) AS date, COUNT(i.id) AS item_count FROM item i LEFT JOIN discard d ON i.id = d.item_id LEFT JOIN ordered_item oi ON i.id = oi.item_id WHERE d.item_id IS NULL AND oi.item_id IS NULL GROUP BY DATE(i.created_at) ORDER BY date";
        return jdbcTemplate.query(sql, new StockStatsRowMapper());
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

    private static class StockStatsRowMapper implements RowMapper<StockStatsResponse> {
        @Override
        public StockStatsResponse mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            StockStatsResponse stockStatsResponse = new StockStatsResponse();
            stockStatsResponse.setDate(rs.getDate("date"));
            stockStatsResponse.setItemCount(rs.getInt("item_count"));
            return stockStatsResponse;
        }
    }

}
