package com.example.managementsystemapi.controller;

import com.example.managementsystemapi.domain.User;
import com.example.managementsystemapi.dto.LoginRequestDTO;
import com.example.managementsystemapi.dto.LoginResponseDTO;
import com.example.managementsystemapi.dto.RegisterRequestDTO;
import com.example.managementsystemapi.repository.UserRepository;
import com.example.managementsystemapi.security.TokenService;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO body) {

        log.info("Login attempt for email: {}", body.email());

        final User user = userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found."));

        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            final String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();

    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody RegisterRequestDTO body) {

        log.info("Register attempt for email: {}", body.email());

        final Optional<User> user = userRepository.findByEmail(body.email());

        if (user.isEmpty()) {
            final User newUser = new User();
            newUser.setName(body.name());
            newUser.setEmail(body.email());
            newUser.setPassword(passwordEncoder.encode(body.password()));
            userRepository.save(newUser);

            final String token = tokenService.generateToken(newUser);
            return ResponseEntity.ok(new LoginResponseDTO(newUser.getName(), token));

        }

        return ResponseEntity.badRequest().build();

    }

}
