package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import bd20241.Storage.models.Supplier;

@Repository
public class SupplierRepository {

    private final JdbcTemplate jdbcTemplate;

    public SupplierRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Supplier supplier) {
        String sql = "INSERT INTO product_supplier (cnpj, name, email, phone) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, supplier.getCnpj(), supplier.getName(), supplier.getEmail(), supplier.getPhone());
    }

    public List<Supplier> findAll() {
        String sql = "SELECT * FROM product_supplier";
        return jdbcTemplate.query(sql, new SupplierRowMapper());
    }

    public Supplier findByCnpj(String cnpj) {
        String sql = "SELECT * FROM product_supplier WHERE cnpj = ?";
        return jdbcTemplate.queryForObject(sql, new SupplierRowMapper(), cnpj);
    }

    public void updateSupplier(Supplier supplier) {
        String sql = "UPDATE product_supplier SET name = ?, email = ?, phone = ? WHERE cnpj = ?";
        jdbcTemplate.update(sql, supplier.getName(), supplier.getEmail(), supplier.getPhone(), supplier.getCnpj());
    }


    private static class SupplierRowMapper implements RowMapper<Supplier> {
        @Override
        public Supplier mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Supplier supplier = new Supplier();
            supplier.setCnpj(rs.getString("cnpj"));
            supplier.setName(rs.getString("name"));
            supplier.setEmail(rs.getString("email"));
            supplier.setPhone(rs.getString("phone"));
            return supplier;
        }
    }
    
}
