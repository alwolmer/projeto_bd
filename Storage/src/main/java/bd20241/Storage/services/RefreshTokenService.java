package bd20241.Storage.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import bd20241.Storage.exception.TokenRefreshException;
import bd20241.Storage.models.Employee;
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

    private boolean isTokenExpired(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.deleteByToken(token.getRefreshToken());
            return true;
        }
        return false;
    }

    public boolean isTokenValid(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException(token, "Invalid refresh token"));
        return !isTokenExpired(refreshToken);
    }

    public Employee getEmployee(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException(token, "Invalid refresh token"));
        return employeeRepository.findByCpf(refreshToken.getEmployeeCpf())
                .orElseThrow(() -> new TokenRefreshException(token, "Employee not found"));
    }

    public RefreshToken createRefreshToken(String employeeCpf) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setEmployeeCpf(employeeRepository.findByCpf(employeeCpf).get().getCpf());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationTime));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }
}
