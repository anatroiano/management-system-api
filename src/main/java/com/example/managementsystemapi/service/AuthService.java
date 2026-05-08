package com.example.managementsystemapi.service;

import com.example.managementsystemapi.domain.User;
import com.example.managementsystemapi.dto.auth.LoginRequestDTO;
import com.example.managementsystemapi.dto.auth.LoginResponseDTO;
import com.example.managementsystemapi.dto.auth.RegisterRequestDTO;
import com.example.managementsystemapi.exception.BusinessException;
import com.example.managementsystemapi.repository.UserRepository;
import com.example.managementsystemapi.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(
                dto.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new BusinessException("Invalid email or password");
        }

        String token = tokenService.generateToken(user);

        return new LoginResponseDTO(
                user.getName(),
                token
        );
    }

    @Transactional
    public LoginResponseDTO register(RegisterRequestDTO dto) {

        userRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new BusinessException("Email already registered");
                });

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        String token = tokenService.generateToken(user);

        return new LoginResponseDTO(user.getName(), token);
    }
}