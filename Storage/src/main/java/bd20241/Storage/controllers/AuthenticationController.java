package bd20241.Storage.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bd20241.Storage.models.Employee;
import bd20241.Storage.payloads.requests.LoginEmployeeRequest;
import bd20241.Storage.payloads.requests.RegisterEmployeeRequest;
import bd20241.Storage.payloads.requests.TokenRefreshRequest;
import bd20241.Storage.payloads.responses.LoginResponse;
import bd20241.Storage.services.AuthenticationService;
import bd20241.Storage.services.JwtService;
import bd20241.Storage.services.RefreshTokenService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    private final RefreshTokenService refreshTokenService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginEmployeeRequest loginEmployeeRequest) {
        Employee authenticatedEmployee = authenticationService.authenticate(loginEmployeeRequest);

        String jwtToken = jwtService.generateToken(authenticatedEmployee);
        String refreshToken = refreshTokenService.createRefreshToken(authenticatedEmployee.getCpf()).getRefreshToken();

        LoginResponse loginResponse = new LoginResponse(jwtToken, refreshToken);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody RegisterEmployeeRequest registerEmployeeRequest) {
        authenticationService.signup(registerEmployeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (refreshTokenService.isTokenValid(refreshToken)) {
            Employee employee = refreshTokenService.getEmployee(refreshToken);
            String jwtToken = jwtService.generateToken(employee);

            LoginResponse loginResponse = new LoginResponse(jwtToken, refreshToken);

            return ResponseEntity.ok(loginResponse);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
}
