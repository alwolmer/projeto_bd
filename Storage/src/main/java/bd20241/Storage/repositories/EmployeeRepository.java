package bd20241.Storage.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
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
        String sql = "INSERT INTO employee (cpf, name, email, phone, is_manager, manager_cpf, passwordHash) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, employee.getCpf(), employee.getName(), employee.getEmail(), employee.getPhone(), employee.getIsManager() ,employee.getManagerCpf(), employee.getPasswordHash());
        return employee;
    }

    public List<Employee> findAll() {
        String sql = "SELECT * FROM employee";
        return jdbcTemplate.query(sql, new EmployeeRowMapper());
    }

    public void update(Employee employee) {
        String sql = "UPDATE employee SET name = ?, email = ?, phone = ?, is_manager = ?, manager_cpf = ?, passwordHash = ? WHERE cpf = ?";
        jdbcTemplate.update(sql, employee.getName(), employee.getEmail(), employee.getPhone(), employee.getIsManager(), employee.getManagerCpf(), employee.getPasswordHash(), employee.getCpf());
    }
    
    private static class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Employee employee = new Employee();
            employee.setCpf(rs.getString("cpf"));
            employee.setName(rs.getString("name"));
            employee.setEmail(rs.getString("email"));
            employee.setPhone(rs.getString("phone"));
            employee.setIsManager(rs.getBoolean("is_manager"));
            employee.setManagerCpf(rs.getString("manager_cpf"));
            employee.setPasswordHash(rs.getString("passwordHash"));
            return employee;
        }
    }
}
