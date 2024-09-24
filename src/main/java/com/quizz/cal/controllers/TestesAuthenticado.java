package com.quizz.cal.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/testes/authenticado")
@SecurityRequirement(name = "bearerAuth")
public class TestesAuthenticado {
    
    @PreAuthorize("hasRole('aluno')")
    @GetMapping()
    public Map<String, Object> getAutenticado(@AuthenticationPrincipal(expression = "claims") Map<String, Object> user) {
       
        return user;
    }
    @PreAuthorize("hasRole('aluno')")
    @GetMapping("/teste/2")
    public ResponseEntity<?> getAutenticado2(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(200).body(jwt.getClaims());
    }
    @PreAuthorize("hasRole('aluno')")
    @GetMapping("/teste/3")
    public ResponseEntity<?> getAutenticado3(@AuthenticationPrincipal  OAuth2User principal) {
        return ResponseEntity.status(200).body(principal);
    }
}
