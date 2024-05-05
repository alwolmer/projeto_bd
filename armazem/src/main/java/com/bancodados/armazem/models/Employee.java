package com.bancodados.armazem.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private String cpf;
    private String name;
    private String state;
    private String city;
    private String zip;
    private String number;
    private String complement;
    private String phone;
    private String email;
    private String passwordHash;
    private String managerCpf;
}
