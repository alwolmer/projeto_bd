package com.bancodados.armazem.repository;

import com.bancodados.armazem.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class EmployeeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer getEmployeeCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employee", Integer.class);
    }

    public Optional<Employee> findByEmail(String email) {
        String sql = "SELECT * FROM employee WHERE email = ?";
        Employee employee = jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), email);
        return Optional.ofNullable(employee);
    }

    public Optional<Employee> findByCpf(String cpf) {
        String sql = "SELECT * FROM employee WHERE cpf = ?";
        Employee employee = jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), cpf);
        return Optional.ofNullable(employee);
    }

    public boolean existsByCpf(String cpf) {
        String sql = "SELECT * FROM employee WHERE cpf = ?";
        return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), cpf) != null;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT * FROM employee WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), email) != null;
    }

    public Employee save(Employee employee) {
        try {
            String sql = "INSERT INTO employee(cpf, emp_name, emp_state, emp_city, emp_zip, emp_street, emp_num, emp_comp, phone, email, passwordHash, manager_cpf) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, employee.getCpf(), employee.getName(), employee.getState(), employee.getCity(), employee.getZip(), employee.getStreet(), employee.getNumber(), employee.getComplement(), employee.getPhone(), employee.getEmail(), employee.getPasswordHash(), employee.getManagerCpf());
            return employee;
        } catch (Exception e) {
            return null;
        }
    }


    private static class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            String cpf = rs.getString("cpf");
            String name = rs.getString("emp_name");
            String state = rs.getString("emp_state");
            String city = rs.getString("emp_city");
            String zip = rs.getString("emp_zip");
            String street = rs.getString("emp_street");
            String number = rs.getString("emp_num");
            String complement = rs.getString("emp_comp");
            String phone = rs.getString("phone");
            String email = rs.getString("email");
            String passwordHash = rs.getString("passwordHash");
            String managerCpf = rs.getString("manager_cpf");
            return new Employee(cpf, name, state, city, zip, street,number, complement, phone, email, passwordHash, managerCpf);
        }
    }
}
