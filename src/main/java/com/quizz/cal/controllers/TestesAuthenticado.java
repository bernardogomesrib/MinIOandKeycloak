package com.quizz.cal.controllers;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    
}
