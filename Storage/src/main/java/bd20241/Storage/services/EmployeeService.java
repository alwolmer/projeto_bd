package bd20241.Storage.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import bd20241.Storage.models.Employee;
import bd20241.Storage.repositories.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Optional<Employee> findByCpf(String cpf) {
        return employeeRepository.findByCpf(cpf);
    }

    
}
