package bd20241.Storage.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bd20241.Storage.models.Employee;
import bd20241.Storage.payloads.requests.EditEmployeeRequest;
import bd20241.Storage.payloads.requests.RegisterEmployeeRequest;
import bd20241.Storage.payloads.responses.EmployeeResponse;
import bd20241.Storage.services.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getEmployees() {
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody RegisterEmployeeRequest registerEmployeeRequest) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentCpf = userDetails.getUsername();
        Employee me = employeeService.findByCpf(currentCpf).orElseThrow();
        if (!me.getIsManager()) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(employeeService.createEmployee(registerEmployeeRequest, currentCpf));
    }

    @PatchMapping("/{cpf}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable String cpf,@RequestBody EditEmployeeRequest editEmployeeRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentCpf = userDetails.getUsername();
        Employee me = employeeService.findByCpf(currentCpf).orElseThrow();
        if (!me.getIsManager()) {
            return ResponseEntity.status(403).build();
        }
        String cleanedCpf = cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");


        Employee employee = employeeService.findByCpf(cleanedCpf).orElseThrow();


        if (employee.getManagerCpf() != null && !employee.getManagerCpf().equals(currentCpf) && !me.getIsManager()) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(employeeService.updateEmployee(cleanedCpf, editEmployeeRequest));
    }
    
}
