package bd20241.Storage.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bd20241.Storage.models.Employee;
import bd20241.Storage.payloads.requests.LoginEmployeeRequest;
import bd20241.Storage.payloads.requests.RegisterEmployeeRequest;
import bd20241.Storage.payloads.responses.LoginResponse;
import bd20241.Storage.services.AuthenticationService;
import bd20241.Storage.services.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginEmployeeRequest loginEmployeeRequest) {
        Employee authenticatedEmployee = authenticationService.authenticate(loginEmployeeRequest);

        String jwtToken = jwtService.generateToken(authenticatedEmployee);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<Employee> signup(@RequestBody RegisterEmployeeRequest registerEmployeeRequest) {
        Employee createdEmployee = authenticationService.signup(registerEmployeeRequest);

        return ResponseEntity.ok(createdEmployee);
    }
    
}
