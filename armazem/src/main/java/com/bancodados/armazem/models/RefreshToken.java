package com.bancodados.armazem.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    private long id;
    private String employeeCpf;
    private String refreshToken;
    private Instant expiryDate;
}
