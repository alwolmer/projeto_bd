package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import bd20241.Storage.models.Discard;
import bd20241.Storage.payloads.requests.DiscardItemRequest;

@Repository
public class DiscardRepository {
    private final JdbcTemplate jdbcTemplate;

    public DiscardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Discard save(Discard discard) {
        String sql = "INSERT INTO discard (employee_cpf, item_id, discard_reason) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, discard.getEmployeeCpf(), discard.getItemId(), discard.getDiscardReason());
        String sql2 = "SELECT * FROM discard WHERE employee_cpf = ? AND item_id = ?";
        return jdbcTemplate.queryForObject(sql2, new DiscardRowMapper(), discard.getEmployeeCpf(), discard.getItemId());
    }

    public Discard save(DiscardItemRequest discardItemRequest, String employeeCpf) {
        Discard discard = new Discard();
        discard.setEmployeeCpf(employeeCpf);
        discard.setItemId(discardItemRequest.getItemId());
        discard.setDiscardReason(discardItemRequest.getDiscardReason());
        return save(discard);
    }

    public List<Discard> findAll() {
        String sql = "SELECT * FROM discard";
        return jdbcTemplate.query(sql, new DiscardRowMapper());
    }

    public void deleteByEmployeeCpfAndItemId(String employeeCpf, String itemId) {
        String sql = "DELETE FROM discard WHERE employee_cpf = ? AND item_id = ?";
        jdbcTemplate.update(sql, employeeCpf, itemId);
    }

    public void deleteById(String itemId) {
        String sql = "DELETE FROM discard WHERE item_id = ?";
        jdbcTemplate.update(sql, itemId);
    }

    private static class DiscardRowMapper implements RowMapper<Discard> {
        @Override
        public Discard mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Discard discard = new Discard();
            discard.setEmployeeCpf(rs.getString("employee_cpf"));
            discard.setItemId(rs.getString("item_id"));
            discard.setCreatedAt(rs.getDate("created_at"));
            discard.setDiscardReason(rs.getString("discard_reason"));
            discard.setUpdatedAt(rs.getDate("updated_at"));
            return discard;
        }
    }
    
}
