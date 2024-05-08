package com.bancodados.armazem.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterEmployeeDto {
    private String cpf;
    private String name;
    private String state;
    private String city;
    private String zip;
    private String street;
    private String number;
    private String complement;
    private String phone;
    private String email;
    private String password;
}
