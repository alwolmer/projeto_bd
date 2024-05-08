package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import bd20241.Storage.models.Employee;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public Optional<Employee> findByCpf(String cpf) {
        String sql = "SELECT * FROM employee WHERE cpf = ?";
        Employee employee = jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), cpf);
        return Optional.ofNullable(employee);
    }

    public Employee save(Employee employee) {
        String sql = "INSERT INTO employee (cpf, name, email, phone, state, city, zip, street, num, comp, manager_cpf, passwordHash) VALUES (?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, employee.getCpf(), employee.getName(), employee.getEmail(), employee.getPhone(), employee.getState(), employee.getCity(), employee.getZip(), employee.getStreet(), employee.getNumber(), employee.getComplement(), employee.getManagerCpf(), employee.getPasswordHash());
        return employee;
    }
    
    private static class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee employee = new Employee();
            employee.setCpf(rs.getString("cpf"));
            employee.setName(rs.getString("name"));
            employee.setEmail(rs.getString("email"));
            employee.setPhone(rs.getString("phone"));
            employee.setState(rs.getString("state"));
            employee.setCity(rs.getString("city"));
            employee.setZip(rs.getString("zip"));
            employee.setStreet(rs.getString("street"));
            employee.setNumber(rs.getString("num"));
            employee.setComplement(rs.getString("comp"));
            employee.setManagerCpf(rs.getString("manager_cpf"));
            employee.setPasswordHash(rs.getString("passwordHash"));
            return employee;
        }
    }
}
