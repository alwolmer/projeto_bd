package bd20241.Storage.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import bd20241.Storage.exception.TokenRefreshException;
import bd20241.Storage.models.RefreshToken;
import bd20241.Storage.repositories.EmployeeRepository;
import bd20241.Storage.repositories.RefreshTokenRepository;

@Service
public class RefreshTokenService {
    @Value("${security.jwt.refresh-expiration-time}")
    private Long refreshExpirationTime;

    private RefreshTokenRepository refreshTokenRepository;

    private EmployeeRepository employeeRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, EmployeeRepository employeeRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.employeeRepository = employeeRepository;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.deleteByToken(token.getRefreshToken());
            throw new TokenRefreshException(token.getRefreshToken(), "Expired token. Please issue a new request");
        }
        return token;
    }
}
