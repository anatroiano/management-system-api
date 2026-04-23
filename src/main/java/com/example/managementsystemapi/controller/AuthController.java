package com.example.managementsystemapi.controller;

import com.example.managementsystemapi.domain.User;
import com.example.managementsystemapi.dto.LoginRequestDTO;
import com.example.managementsystemapi.dto.LoginResponseDTO;
import com.example.managementsystemapi.dto.RegisterRequestDTO;
import com.example.managementsystemapi.repository.UserRepository;
import com.example.managementsystemapi.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "Authentication", description = "Authentication and user registration")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Operation(summary = "User login", description = "Authenticates user and returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO body) {

        log.info("Login attempt for email: {}", body.getEmail());

        final User user = userRepository.findByEmail(body.getEmail()).orElseThrow(() -> new RuntimeException("User not found."));

        if (passwordEncoder.matches(body.getPassword(), user.getPassword())) {
            final String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();

    }

    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered"),
            @ApiResponse(responseCode = "400", description = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterRequestDTO body) {

        log.info("Register attempt for email: {}", body.getEmail());

        final Optional<User> user = userRepository.findByEmail(body.getEmail());

        if (user.isEmpty()) {
            final User newUser = new User();
            newUser.setName(body.getName());
            newUser.setEmail(body.getEmail());
            newUser.setPassword(passwordEncoder.encode(body.getPassword()));
            userRepository.save(newUser);

            final String token = tokenService.generateToken(newUser);
            return ResponseEntity.ok(new LoginResponseDTO(newUser.getName(), token));

        }

        return ResponseEntity.badRequest().build();

    }

}
