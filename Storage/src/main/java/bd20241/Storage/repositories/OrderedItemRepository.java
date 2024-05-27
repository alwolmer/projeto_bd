package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import bd20241.Storage.models.OrderedItem;

@Repository
public class OrderedItemRepository {
    private final JdbcTemplate jdbcTemplate;

    public OrderedItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void save(OrderedItem orderedItem) {
        String sql = "INSERT INTO ordered_items (item_id, order_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, orderedItem.getItemId(), orderedItem.getOrderId());
    }

    public void delete(OrderedItem orderedItem) {
        String sql = "DELETE FROM ordered_items WHERE item_id = ? AND order_id = ?";
        jdbcTemplate.update(sql, orderedItem.getItemId(), orderedItem.getOrderId());
    }


    private static class OrderedItemRowMapper implements RowMapper<OrderedItem> {
        @Override
        public OrderedItem mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            OrderedItem orderedItem = new OrderedItem();
            orderedItem.setItemId(rs.getString("item_id"));
            orderedItem.setOrderId(rs.getString("order_id"));
            return orderedItem;
        }
    }
    
}
