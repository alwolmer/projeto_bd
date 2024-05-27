package bd20241.Storage.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bd20241.Storage.models.Employee;
import bd20241.Storage.payloads.requests.EditEmployeeRequest;
import bd20241.Storage.payloads.requests.RegisterEmployeeRequest;
import bd20241.Storage.payloads.responses.EmployeeResponse;
import bd20241.Storage.repositories.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Employee> findByCpf(String cpf) {
        return employeeRepository.findByCpf(cpf);
    }

    public List<EmployeeResponse> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponse> employeeResponses = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeResponse employeeResponse = new EmployeeResponse();
            employeeResponse.setCpf(employee.getCpf());
            employeeResponse.setName(employee.getName());
            employeeResponse.setEmail(employee.getEmail());
            employeeResponse.setPhone(employee.getPhone());
            employeeResponse.setIsManager(employee.getIsManager());
            employeeResponse.setManagerCpf(employee.getManagerCpf());
            employeeResponses.add(employeeResponse);
        }
        return employeeResponses;
    }

    public EmployeeResponse createEmployee(RegisterEmployeeRequest registerEmployeeRequest, String currentCpf) {
        Employee employee = new Employee();
        employee.setCpf(registerEmployeeRequest.getCpf());
        employee.setName(registerEmployeeRequest.getName());
        employee.setEmail(registerEmployeeRequest.getEmail());
        employee.setPhone(registerEmployeeRequest.getPhone());
        employee.setIsManager(registerEmployeeRequest.getIsManager());
        employee.setManagerCpf(currentCpf);
        employee.setPasswordHash(passwordEncoder.encode(registerEmployeeRequest.getPassword()));
        employeeRepository.save(employee);
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setCpf(employee.getCpf());
        employeeResponse.setName(employee.getName());
        employeeResponse.setEmail(employee.getEmail());
        employeeResponse.setPhone(employee.getPhone());
        employeeResponse.setIsManager(employee.getIsManager());
        employeeResponse.setManagerCpf(employee.getManagerCpf());
        return employeeResponse;
    }

    public EmployeeResponse updateEmployee(String cpf, EditEmployeeRequest editEmployeeRequest) {
        Employee employee = employeeRepository.findByCpf(cpf).orElseThrow();
        employee.setName(editEmployeeRequest.getName());
        employee.setEmail(editEmployeeRequest.getEmail());
        employee.setPhone(editEmployeeRequest.getPhone());
        employee.setIsManager(editEmployeeRequest.getIsManager());
        if (editEmployeeRequest.getPassword() != null && editEmployeeRequest.getPassword().length() > 0) {
            employee.setPasswordHash(passwordEncoder.encode(editEmployeeRequest.getPassword()));
        }
        employeeRepository.update(employee);
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setCpf(employee.getCpf());
        employeeResponse.setName(employee.getName());
        employeeResponse.setEmail(employee.getEmail());
        employeeResponse.setPhone(employee.getPhone());
        employeeResponse.setIsManager(employee.getIsManager());
        employeeResponse.setManagerCpf(employee.getManagerCpf());
        return employeeResponse;
    }

    public void deleteEmployee(String cpf) {
        employeeRepository.deleteByCpf(cpf);
    }

    
}
