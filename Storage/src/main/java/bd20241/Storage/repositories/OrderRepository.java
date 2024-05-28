package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import bd20241.Storage.models.Order;

@Repository
public class OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Order order) {
        String sql = "INSERT INTO orders (id, client_id, delivery_address_id, employee_cpf, carrier_cnpj, tracking_code) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, order.getId(), order.getClientId(), order.getDeliveryAddressId(), order.getEmployeeCpf(), order.getCarrierCnpj(), order.getTrackingCode());
    }

    public Order findById(String id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new OrderRowMapper(), id);
    }

    public void updateOrder(Order order) {
        String sql = "UPDATE orders SET client_id = ?, delivery_address_id = ?, employee_cpf = ?, carrier_cnpj = ?, tracking_code = ?, created_at = ?, updated_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, order.getClientId(), order.getDeliveryAddressId(), order.getEmployeeCpf(), order.getCarrierCnpj(), order.getTrackingCode(), order.getCreatedAt(), order.getUpdatedAt(), order.getId());
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Order> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }


    private static class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setId(rs.getString("id"));
            order.setClientId(rs.getString("client_id"));
            order.setDeliveryAddressId(rs.getString("delivery_address_id"));
            order.setEmployeeCpf(rs.getString("employee_cpf"));
            order.setTrackingCode(rs.getString("tracking_code"));
            order.setCarrierCnpj(rs.getString("carrier_cnpj"));
            order.setCreatedAt(rs.getDate("created_at"));
            order.setUpdatedAt(rs.getDate("updated_at"));
            return order;
        }
    }
    
}
