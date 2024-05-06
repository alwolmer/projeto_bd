package com.bancodados.armazem.controllers;

import com.bancodados.armazem.dao.EmployeeDAO;
import com.bancodados.armazem.dtos.LoginUserDto;
import com.bancodados.armazem.dtos.RegisterEmployeeDto;
import com.bancodados.armazem.exception.TokenRefreshException;
import com.bancodados.armazem.models.Employee;
import com.bancodados.armazem.models.RefreshToken;
import com.bancodados.armazem.payload.request.TokenRefreshRequest;
import com.bancodados.armazem.payload.response.CurrentEmployeeResponse;
import com.bancodados.armazem.payload.response.JwtResponse;
import com.bancodados.armazem.payload.response.MessageResponse;
import com.bancodados.armazem.payload.response.TokenRefreshResponse;
import com.bancodados.armazem.repository.EmployeeRepository;
import com.bancodados.armazem.security.jwt.JwtUtils;
import com.bancodados.armazem.security.services.RefreshTokenService;
import com.bancodados.armazem.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterEmployeeDto registerEmployeeDto) {
        return employeeDAO.register(registerEmployeeDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getCpf());
        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getRefreshToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        String requestRefresh = request.getRefresh();

        return refreshTokenService.findByToken(requestRefresh).map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getEmployeeCpf)
                .map(employeeCpf -> {
                    Employee employee = employeeRepository.findByCpf(employeeCpf).get();
                    String token = jwtUtils.generateTokenFromUser(UserDetailsImpl.build(employee));
                    String newRefresh = refreshTokenService.createRefreshToken(employee.getCpf()).getRefreshToken();
                    refreshTokenService.deleteByToken(requestRefresh);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, newRefresh));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefresh, "Refresh token not found"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String cpf = userDetails.getCpf();
        refreshTokenService.deleteByEmployeeCpf(cpf);
        return ResponseEntity.ok(new MessageResponse("You have been logged out successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CurrentEmployeeResponse response =  new CurrentEmployeeResponse(userDetails);
        return ResponseEntity.ok().body(response);
    }
}
