package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import bd20241.Storage.models.DeliveryAddress;

@Repository
public class DeliveryAddressRepository {
    private final JdbcTemplate jdbcTemplate;

    public DeliveryAddressRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(DeliveryAddress deliveryAddress) {
        String sql = "INSERT INTO delivery_address (id, recipient_name, state, city, zip, street, number, details, client_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, deliveryAddress.getId(), deliveryAddress.getRecipientName(), deliveryAddress.getState(), deliveryAddress.getCity(), deliveryAddress.getZip(), deliveryAddress.getStreet(), deliveryAddress.getNumber(), deliveryAddress.getDetails(), deliveryAddress.getClientId());
    }

    public DeliveryAddress findById(String id) {
        String sql = "SELECT * FROM delivery_address WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new DeliveryAddressRowMapper(), id);
    }

    public void updateDeliveryAddress(DeliveryAddress deliveryAddress) {
        String sql = "UPDATE delivery_address SET recipient_name = ?, state = ?, city = ?, zip = ?, street = ?, number = ?, details = ?, client_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, deliveryAddress.getRecipientName(), deliveryAddress.getState(), deliveryAddress.getCity(), deliveryAddress.getZip(), deliveryAddress.getStreet(), deliveryAddress.getNumber(), deliveryAddress.getDetails(), deliveryAddress.getClientId(), deliveryAddress.getId());
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM delivery_address WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<DeliveryAddress> findAll() {
        String sql = "SELECT * FROM delivery_address";
        return jdbcTemplate.query(sql, new DeliveryAddressRowMapper());
    }


    private static class DeliveryAddressRowMapper implements RowMapper<DeliveryAddress> {
        @Override
        public DeliveryAddress mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            DeliveryAddress deliveryAddress = new DeliveryAddress();
            deliveryAddress.setId(rs.getString("id"));
            deliveryAddress.setRecipientName(rs.getString("recipient_name"));
            deliveryAddress.setState(rs.getString("state"));
            deliveryAddress.setCity(rs.getString("city"));
            deliveryAddress.setZip(rs.getString("zip"));
            deliveryAddress.setStreet(rs.getString("street"));
            deliveryAddress.setNumber(rs.getString("number"));
            deliveryAddress.setDetails(rs.getString("details"));
            deliveryAddress.setClientId(rs.getString("client_id"));
            return deliveryAddress;
        }
    }
}
