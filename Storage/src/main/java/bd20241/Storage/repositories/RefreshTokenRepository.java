package bd20241.Storage.repositories;

import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import bd20241.Storage.models.RefreshToken;

@Repository
public class RefreshTokenRepository {

    private final JdbcTemplate jdbcTemplate;

    public RefreshTokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<RefreshToken> findByToken(String token) {
        String sql = "SELECT * FROM refresh_token WHERE token = ?";
        RefreshToken refresh = jdbcTemplate.queryForObject(sql, new RefreshTokenMapper(), token);
        return Optional.ofNullable(refresh);
    }

    public RefreshToken save(RefreshToken refreshToken) {
        String sql = "INSERT INTO refresh_token (employee_cpf, token, expiry_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, refreshToken.getEmployeeCpf(), refreshToken.getRefreshToken(), refreshToken.getExpiryDate());
        return refreshToken;
    }

    public void deleteByToken(String token) {
        String sql = "DELETE FROM refresh_token WHERE token = ?";
        jdbcTemplate.update(sql, token);
    }


    private static class RefreshTokenMapper implements RowMapper<RefreshToken> {
        @Override
        public RefreshToken mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setId(rs.getLong("id"));
            refreshToken.setEmployeeCpf(rs.getString("employee_cpf"));
            refreshToken.setRefreshToken(rs.getString("token"));
            refreshToken.setExpiryDate(rs.getTimestamp("expiry_date").toInstant());
            return refreshToken;
        }
    }
    
}
