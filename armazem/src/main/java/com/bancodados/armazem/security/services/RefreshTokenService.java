package com.bancodados.armazem.security.services;

import com.bancodados.armazem.exception.TokenRefreshException;
import com.bancodados.armazem.models.RefreshToken;
import com.bancodados.armazem.repository.EmployeeRepository;
import com.bancodados.armazem.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${security.jwt.refresh-expiration-time}")
    private Long refreshExpirationTime;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String employeeCpf) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setEmployeeCpf(employeeRepository.findByCpf(employeeCpf).get().getCpf());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationTime));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.deleteByToken(token.getRefreshToken());
            throw new TokenRefreshException(token.getRefreshToken(), "Token expired. Please login again");
        }
        return token;
    }

    @Transactional
    public int deleteByEmployeeCpf(String employeeCpf) {
        return refreshTokenRepository.deleteByEmployee(employeeRepository.findByCpf(employeeCpf).get());
    }

    @Transactional
    public int deleteByToken(String token) {
        return refreshTokenRepository.deleteByToken(token);
    }

}
