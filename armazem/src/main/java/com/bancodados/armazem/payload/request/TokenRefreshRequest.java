package com.bancodados.armazem.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshRequest {
    private String refresh;

}
