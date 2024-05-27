package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import bd20241.Storage.models.Carrier;

@Repository
public class CarrierRepository {
    private final JdbcTemplate jdbcTemplate;

    public CarrierRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Carrier carrier) {
        String sql = "INSERT INTO carrier (cnpj, name, email, phone) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, carrier.getCnpj(), carrier.getName(), carrier.getEmail(), carrier.getPhone());
    }

    public Carrier findByCnpj(String cnpj) {
        String sql = "SELECT * FROM carrier WHERE cnpj = ?";
        return jdbcTemplate.queryForObject(sql, new CarrierRowMapper(), cnpj);
    }

    public void updateCarrier(Carrier carrier) {
        String sql = "UPDATE carrier SET name = ?, email = ?, phone = ? WHERE cnpj = ?";
        jdbcTemplate.update(sql, carrier.getName(), carrier.getEmail(), carrier.getPhone(), carrier.getCnpj());
    }

    public void deleteByCnpj(String cnpj) {
        String sql = "DELETE FROM carrier WHERE cnpj = ?";
        jdbcTemplate.update(sql, cnpj);
    }

    public List<Carrier> findAll() {
        String sql = "SELECT * FROM carrier";
        return jdbcTemplate.query(sql, new CarrierRowMapper());
    }

    private static class CarrierRowMapper implements RowMapper<Carrier> {
        @Override
        public Carrier mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Carrier carrier = new Carrier();
            carrier.setCnpj(rs.getString("cnpj"));
            carrier.setName(rs.getString("name"));
            carrier.setEmail(rs.getString("email"));
            carrier.setPhone(rs.getString("phone"));
            return carrier;
        }
    }
    
}
