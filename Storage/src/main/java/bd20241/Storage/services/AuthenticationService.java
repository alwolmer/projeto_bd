package bd20241.Storage.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bd20241.Storage.models.Employee;
import bd20241.Storage.payloads.requests.LoginEmployeeRequest;
import bd20241.Storage.payloads.requests.RegisterEmployeeRequest;
import bd20241.Storage.repositories.EmployeeRepository;

@Service
public class AuthenticationService {
    private final EmployeeRepository employeeRepository;
    
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public Employee signup(RegisterEmployeeRequest input) {
        Employee employee = new Employee();
        employee.setCpf(input.getCpf());
        employee.setName(input.getName());
        employee.setEmail(input.getEmail());
        employee.setPhone(input.getPhone());
        employee.setState(input.getState());
        employee.setCity(input.getCity());
        employee.setZip(input.getZip());
        employee.setStreet(input.getStreet());
        employee.setNumber(input.getNumber());
        employee.setComplement(input.getComplement());
        employee.setManagerCpf(null);
        employee.setPasswordHash(passwordEncoder.encode(input.getPassword()));

        return employeeRepository.save(employee);
    }

    public Employee authenticate(LoginEmployeeRequest input) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(input.getCpf(), input.getPassword())
        );

        return employeeRepository.findByCpf(input.getCpf()).orElseThrow();
    }

}
