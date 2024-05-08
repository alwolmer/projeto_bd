package com.bancodados.armazem.payload.response;

import com.bancodados.armazem.security.services.UserDetailsImpl;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentEmployeeResponse {
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
    private String managerCpf;

    public CurrentEmployeeResponse(UserDetailsImpl userDetails) {
        this.cpf = userDetails.getCpf();
        this.name = userDetails.getName();
        this.state = userDetails.getState();
        this.city = userDetails.getCity();
        this.zip = userDetails.getZip();
        this.street = userDetails.getStreet();
        this.number = userDetails.getNumber();
        this.complement = userDetails.getComplement();
        this.phone = userDetails.getPhone();
        this.email = userDetails.getEmail();
        this.managerCpf = userDetails.getManagerCpf();
    }
}
