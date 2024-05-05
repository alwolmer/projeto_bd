package com.bancodados.armazem.security.services;

import com.bancodados.armazem.models.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Getter
    private String cpf;
    @Getter
    private String name;
    @Getter
    private String state;
    @Getter
    private String city;
    @Getter
    private String zip;
    @Getter
    private String number;
    @Getter
    private String complement;
    @Getter
    private String phone;
    @Getter
    private String email;
    @JsonIgnore
    private String passwordHash;
    @Getter
    private String managerCpf;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String cpf, String name, String state, String city, String zip, String number, String complement, String phone, String email, String passwordHash, String managerCpf ,Collection<? extends GrantedAuthority> authorities) {
        this.cpf = cpf;
        this.name = name;
        this.state = state;
        this.city = city;
        this.zip = zip;
        this.number = number;
        this.complement = complement;
        this.phone = phone;
        this.email = email;
        this.passwordHash = passwordHash;
        this.managerCpf = managerCpf;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Employee employee) {
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));

        return new UserDetailsImpl(
                employee.getCpf(),
                employee.getName(),
                employee.getState(),
                employee.getCity(),
                employee.getZip(),
                employee.getNumber(),
                employee.getComplement(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getPasswordHash(),
                employee.getManagerCpf(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return Objects.equals(cpf, that.cpf);
    }
}
