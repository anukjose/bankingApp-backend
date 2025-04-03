package com.banking.controller;

import com.banking.security.JwtUtil;
import com.banking.service.BankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")  // Apply CORS to all methods in this controller
@RestController
@RequestMapping("/api/banking")
public class BankingController {

    private final BankingService bankingService;
    private final JwtUtil jwtUtil;

    public BankingController(BankingService bankingService, JwtUtil jwtUtil) {
        this.bankingService = bankingService;
        this.jwtUtil = jwtUtil;
    }

    // Utility method to extract token from Authorization header
    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Remove "Bearer " prefix and return token
        }
        return null;
    }

    // Utility method to validate token and authenticate user
    private boolean validateTokenAndAuthenticate(String token) {
        String username = jwtUtil.extractUsername(token);
        if (username != null && jwtUtil.validateToken(token, username)) {
            // Set the authentication in the SecurityContext
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        }
        return false;
    }

    @GetMapping("/checkBalance")
    public ResponseEntity<Double> checkBalance(@RequestParam int accountId, @RequestHeader("Authorization") String authorizationHeader) {
        // Extract and validate the token
        String token = extractTokenFromHeader(authorizationHeader);
        if (token != null && validateTokenAndAuthenticate(token)) {
            // Proceed if the token is valid
            double balance = bankingService.checkBalance(accountId);
            return ResponseEntity.ok(balance);
        }
        return ResponseEntity.status(403).header("Error", "Invalid or expired token").body(null);
    }

    @PostMapping("/addMoney")
    public ResponseEntity<String> addMoney(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String authorizationHeader) {
        // Extract and validate the token
        String token = extractTokenFromHeader(authorizationHeader);
        if (token != null && validateTokenAndAuthenticate(token)) {
            // Proceed if the token is valid
            int accountId = (int) request.get("accountId");
            double amount = (double) request.get("amount");
            bankingService.addMoney(accountId, amount);
            return ResponseEntity.ok("Money added successfully!");
        }
        return ResponseEntity.status(403).header("Error", "Invalid or expired token").body(null);
    }

    @PostMapping("/deductMoney")
    public ResponseEntity<String> deductMoney(@RequestBody Map<String, Object> request, @RequestHeader("Authorization") String authorizationHeader) {
        // Extract and validate the token
        String token = extractTokenFromHeader(authorizationHeader);
        if (token != null && validateTokenAndAuthenticate(token)) {
            // Proceed if the token is valid
            int accountId = (int) request.get("accountId");
            double amount = (double) request.get("amount");
            bankingService.deductMoney(accountId, amount);
            return ResponseEntity.ok("Money deducted successfully!");
        }
        return ResponseEntity.status(403).body("Invalid or expired token");
    }
}
