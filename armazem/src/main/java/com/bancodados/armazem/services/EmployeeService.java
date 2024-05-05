package com.bancodados.armazem.services;

import com.bancodados.armazem.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Employee findByEmail(String email) {
        String sql = "SELECT * FROM employee WHERE email = ? LIMIT 1";
        return jdbcTemplate.query(sql, new EmployeeRowMapper()).getFirst();
    }

    public Employee save(Employee employee) {
        try {
            String sql = "INSERT INTO employee(cpf, emp_name, emp_state, emp_city, emp_zip, emp_num, emp_comp, phone, email, passwordHash, manager_cpf) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, employee.getCpf(), employee.getName(), employee.getState(), employee.getCity(), employee.getZip(), employee.getNumber(), employee.getComplement(), employee.getPhone(), employee.getEmail(), employee.getPasswordHash(), employee.getManagerCpf());
            return employee;
        } catch (Exception e) {
            return null;
        }
    }

    public int getEmployeeCount() {

        String sql = "SELECT COUNT(*) FROM employee";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        if (count == null) {
            return 0;
        }
        return count;
    }

    private static class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            String cpf = rs.getString("cpf");
            String name = rs.getString("emp_name");
            String state = rs.getString("emp_state");
            String city = rs.getString("emp_city");
            String zip = rs.getString("emp_zip");
            String number = rs.getString("emp_num");
            String complement = rs.getString("emp_comp");
            String phone = rs.getString("phone");
            String email = rs.getString("email");
            String passwordHash = rs.getString("passwordHash");
            String managerCpf = rs.getString("manager_cpf");
            return new Employee(cpf, name, state, city, zip, number, complement, phone, email, passwordHash, managerCpf);
        }
    }
}
