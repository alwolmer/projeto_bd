package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class PackageRepository {
    private final JdbcTemplate jdbcTemplate;

    public PackageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(bd20241.Storage.models.Package newPackage) {
        String sql = "INSERT INTO packages (id, tracking_code, delivery_notes, order_id, fragile) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, newPackage.getId(), newPackage.getTrackingCode(), newPackage.getDeliveryNotes(), newPackage.getOrderId(), newPackage.isFragile());
    }

    public bd20241.Storage.models.Package findById(String id) {
        String sql = "SELECT * FROM packages WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new PackageRowMapper(), id);
    }

    public void updatePackage(bd20241.Storage.models.Package newPackage) {
        String sql = "UPDATE packages SET tracking_code = ?, delivery_notes = ?, order_id = ?, fragile = ? WHERE id = ?";
        jdbcTemplate.update(sql, newPackage.getTrackingCode(), newPackage.getDeliveryNotes(), newPackage.getOrderId(), newPackage.isFragile(), newPackage.getId());
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM packages WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<bd20241.Storage.models.Package> findAll() {
        String sql = "SELECT * FROM packages";
        return jdbcTemplate.query(sql, new PackageRowMapper());
    }

    private static class PackageRowMapper implements RowMapper<bd20241.Storage.models.Package> {
        @Override
        public bd20241.Storage.models.Package mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            bd20241.Storage.models.Package newPackage = new bd20241.Storage.models.Package();
            newPackage.setId(rs.getString("id"));
            newPackage.setTrackingCode(rs.getString("tracking_code"));
            newPackage.setDeliveryNotes(rs.getString("delivery_notes"));
            newPackage.setOrderId(rs.getString("order_id"));
            newPackage.setFragile(rs.getBoolean("fragile"));
            return newPackage;
        }
    }
}
