package com.quizz.cal.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizz.cal.JPA.UserJPA;
import com.quizz.cal.objects.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    @Value("${keycloak.realm.id}")
    String realmId;

    @Autowired
    private UserJPA userJPA;
    @Operation(summary = "procura um usuário pelo nome ou email")
    @PreAuthorize("hasRole('aluno')")
    @GetMapping("{value}")
    public ResponseEntity<?> getUser(@PathVariable String value) {
        List<User> user = userJPA.findByAnyAndRealm_id(value,   realmId);
        return ResponseEntity.ok(user);
    }
    @Operation(summary = "procura um usuário pelo id")
    @PreAuthorize("hasRole('aluno')")
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        Optional<User> user = userJPA.findByIdAndRealmId(id, realmId);
        return ResponseEntity.ok(user);
    }
    
}
