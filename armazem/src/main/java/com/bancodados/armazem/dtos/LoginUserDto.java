package com.bancodados.armazem.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {
    private String cpf;
    private String password;
}
