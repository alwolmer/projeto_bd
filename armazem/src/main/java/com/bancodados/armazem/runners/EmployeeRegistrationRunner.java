package com.bancodados.armazem.runners;

import com.bancodados.armazem.dao.EmployeeDAO;
import com.bancodados.armazem.dtos.RegisterEmployeeDto;
import com.bancodados.armazem.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class EmployeeRegistrationRunner implements CommandLineRunner {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeDAO employeeDAO;


    @Override
    public void run(String... args) throws Exception {
        System.out.println("Welcome to the Armazem");
        if (employeeRepository.getEmployeeCount() > 0) {
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("No employees found. Starting employee registration...");

            System.out.print("Enter employee CPF: ");
            String employeeCPF = scanner.nextLine();
            System.out.print("Enter employee name: ");
            String employeeName = scanner.nextLine();
            System.out.print("Enter employee State: ");
            String employeeState = scanner.nextLine();
            System.out.print("Enter employee City: ");
            String employeeCity = scanner.nextLine();
            System.out.print("Enter employee Zip: ");
            String employeeZip = scanner.nextLine();
            System.out.print("Enter employee address Street: ");
            String employeeAddressStreet = scanner.nextLine();
            System.out.print("Enter employee address Number: ");
            String employeeNumber = scanner.nextLine();
            System.out.print("Enter employee address Complement: ");
            String employeeComplement = scanner.nextLine();
            System.out.print("Enter employee phone Number: ");
            String employeePhone = scanner.nextLine();
            System.out.print("Enter employee email: ");
            String employeeEmail = scanner.nextLine();
            System.out.print("Enter employee password: ");
            String employeePassword = scanner.nextLine();

            RegisterEmployeeDto input = new RegisterEmployeeDto();
            input.setCpf(employeeCPF);
            input.setName(employeeName);
            input.setState(employeeState);
            input.setCity(employeeCity);
            input.setZip(employeeZip);
            input.setStreet(employeeAddressStreet);
            input.setNumber(employeeNumber);
            input.setComplement(employeeComplement);
            input.setPhone(employeePhone);
            input.setEmail(employeeEmail);
            input.setPassword(employeePassword);


            EmployeeDAO.RegisterResponse response = employeeDAO.register(input);

            switch (response) {
                case SUCCESS:
                    System.out.println("Employee registered successfully");
                case ERROR:
                    System.out.println("Employee registration failed");
            }
        } catch (Exception e) {
            System.err.println("An error occurred during registration: " + e.getMessage());
        }
    }
}
