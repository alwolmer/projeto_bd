package com.bancodados.armazem.services;

import com.bancodados.armazem.dtos.LoginUserDto;
import com.bancodados.armazem.dtos.RegisterEmployeeDto;
import com.bancodados.armazem.models.Employee;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(EmployeeService employeeService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public Employee signup(RegisterEmployeeDto input) {
        Employee employee = new Employee();
        employee.setCpf(input.getCpf());
        employee.setName(input.getName());
        employee.setState(input.getState());
        employee.setCity(input.getCity());
        employee.setZip(input.getZip());
        employee.setNumber(input.getNumber());
        employee.setComplement(input.getComplement());
        employee.setPhone(input.getPhone());
        employee.setEmail(input.getEmail());
        employee.setPasswordHash(passwordEncoder.encode(input.getPassword()));


        return employeeService.save(employee);
    }

    public Employee authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return employeeService.findByEmail(input.getEmail());
    }
}
