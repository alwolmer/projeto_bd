package com.bancodados.armazem.security.services;

import com.bancodados.armazem.models.Employee;
import com.bancodados.armazem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByCpf(cpf).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + cpf));

        return UserDetailsImpl.build(employee);
    }

}
