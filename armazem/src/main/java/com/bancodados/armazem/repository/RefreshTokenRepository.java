package com.bancodados.armazem.repository;


import com.bancodados.armazem.models.Employee;
import com.bancodados.armazem.models.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class RefreshTokenRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<RefreshToken> findByToken(String token) {
        String sql = "SELECT * FROM refresh_token WHERE token = ?";
        RefreshToken refresh = jdbcTemplate.queryForObject(sql, new RefreshTokenMapper(), token);
        return Optional.ofNullable(refresh);
    }


    public int deleteByEmployee(Employee employee) {
        String sql = "DELETE FROM refresh_token WHERE employee_cpf = ?";
        return jdbcTemplate.update(sql, employee.getCpf());
    }

    public int deleteByToken(String token) {
        String sql = "DELETE FROM refresh_token WHERE token = ?";
        return jdbcTemplate.update(sql, token);
    }

    public RefreshToken save(RefreshToken refresh) {
        String sql = "INSERT INTO refresh_token (employee_cpf, token, expiry_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, refresh.getEmployeeCpf(), refresh.getRefreshToken(), refresh.getExpiryDate());
        return refresh;
    }


    private static class RefreshTokenMapper implements RowMapper<RefreshToken> {
        @Override
        public RefreshToken mapRow(ResultSet rs, int rowNum) throws SQLException {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setId(rs.getLong("id"));
            refreshToken.setEmployeeCpf(rs.getString("employee_cpf"));
            refreshToken.setRefreshToken(rs.getString("refresh_token"));
            refreshToken.setExpiryDate(rs.getDate("expiry_date").toInstant());
            return refreshToken;
        }
    }
}
