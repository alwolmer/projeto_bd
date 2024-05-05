package com.bancodados.armazem.dao;

import com.bancodados.armazem.dtos.RegisterEmployeeDto;
import com.bancodados.armazem.models.Employee;
import com.bancodados.armazem.payload.response.MessageResponse;
import com.bancodados.armazem.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class EmployeeDAO {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDAO.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> register(RegisterEmployeeDto employee) {
        if (employeeRepository.existsByCpf(employee.getCpf())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Employee with this CPF already exists"));
        }

        if (employeeRepository.existsByEmail(employee.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Employee with this email already exists"));
        }
        Employee newEmployee = new Employee();
        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        newEmployee.setCpf(employee.getCpf());
        newEmployee.setName(employee.getName());
        newEmployee.setState(employee.getState());
        newEmployee.setCity(employee.getCity());
        newEmployee.setZip(employee.getZip());
        newEmployee.setNumber(employee.getNumber());
        newEmployee.setComplement(employee.getComplement());
        newEmployee.setEmail(employee.getEmail());
        newEmployee.setPhone(employee.getPhone());
        employeeRepository.save(newEmployee);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Employee registered successfully"));
    }
}
