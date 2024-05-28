package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import bd20241.Storage.models.Client;

@Repository
public class ClientRepository {
    private final JdbcTemplate jdbcTemplate;

    public ClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Client client) {
        if (client.getCnpj() == null) {
            String sql = "INSERT INTO client (id, name, phone, email, cpf) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, client.getId(), client.getName(), client.getPhone(), client.getEmail(), client.getCpf());
        } else {
            String sql = "INSERT INTO client (id, name, phone, email, cnpj) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, client.getId(), client.getName(), client.getPhone(), client.getEmail(), client.getCnpj());
        }
    }

    public Client findById(String id) {
        String sql = "SELECT * FROM client WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new ClientRowMapper(), id);
    }

    public void updateClient(Client client) {
        String sql = "UPDATE client SET name = ?, phone = ?, email = ?, cpf = ?, cnpj = ? WHERE id = ?";
        jdbcTemplate.update(sql, client.getName(), client.getPhone(), client.getEmail(), client.getCpf(), client.getCnpj(), client.getId());
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM client WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Client> findAll() {
        String sql = "SELECT * FROM client";
        return jdbcTemplate.query(sql, new ClientRowMapper());
    }

    private static class ClientRowMapper implements RowMapper<Client> {
        @Override
        public Client mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Client client = new Client();
            client.setId(rs.getString("id"));
            client.setName(rs.getString("name"));
            client.setPhone(rs.getString("phone"));
            client.setEmail(rs.getString("email"));
            client.setCpf(rs.getString("cpf"));
            client.setCnpj(rs.getString("cnpj"));
            return client;
        }
    }
    
}
