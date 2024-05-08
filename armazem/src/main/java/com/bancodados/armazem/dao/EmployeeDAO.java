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

    public enum RegisterResponse {
        EXISTS_CPF,
        EXISTS_EMAIL,
        ERROR,
        SUCCESS
    }

    public RegisterResponse register(RegisterEmployeeDto employee) {
        if (employeeRepository.existsByCpf(employee.getCpf())) {
            return RegisterResponse.EXISTS_CPF;
        }

        if (employeeRepository.existsByEmail(employee.getEmail())) {
            return RegisterResponse.EXISTS_EMAIL;
        }
        Employee newEmployee = new Employee();
        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        newEmployee.setCpf(employee.getCpf());
        newEmployee.setName(employee.getName());
        newEmployee.setState(employee.getState());
        newEmployee.setCity(employee.getCity());
        newEmployee.setZip(employee.getZip());
        newEmployee.setStreet(employee.getStreet());
        newEmployee.setNumber(employee.getNumber());
        newEmployee.setComplement(employee.getComplement());
        newEmployee.setEmail(employee.getEmail());
        newEmployee.setPhone(employee.getPhone());
        newEmployee.setPasswordHash(encodedPassword);
        if (employeeRepository.save(newEmployee) != null) {
            return RegisterResponse.SUCCESS;
        }
        return RegisterResponse.ERROR;
    }
}
