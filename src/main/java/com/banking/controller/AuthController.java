package com.banking.controller;

import com.banking.model.User;
import com.banking.model.Token;
import com.banking.repository.UserRepository;
import com.banking.repository.TokenRepository;
import com.banking.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")  // Apply CORS to all methods in this controller
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, TokenRepository tokenRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "User already exists!";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User dbUser = userRepository.findByUsername(user.getUsername());

        if (dbUser != null && passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            Token tokenEntry = new Token();
            tokenEntry.setUsername(user.getUsername());
            tokenEntry.setToken(token);
            tokenEntry.setExpiry(LocalDateTime.now().plusHours(1)); // 1-hour expiry
            tokenRepository.save(tokenEntry);
            return token;
        }
        return "Invalid credentials";
    }

    @GetMapping("/validateToken")
    public boolean validateToken(@RequestParam String token) {
        Optional<Token> tokenRecord = tokenRepository.findByToken(token);
        return tokenRecord.isPresent() && tokenRecord.get().getExpiry().isAfter(LocalDateTime.now());
    }
}
