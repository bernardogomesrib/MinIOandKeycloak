package com.quizz.cal.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizz.cal.JPA.QuestionJPA;
import com.quizz.cal.objects.Question;
import com.quizz.cal.objects.User;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;




@RestController()
@RequestMapping("/question")

public class QuestionController {

    @Autowired
    private QuestionJPA questionJPA;
    @PreAuthorize("hasRole('professor')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping()
    public Question post(@RequestBody Question entity,@AuthenticationPrincipal Jwt jwt) {
        User u = new User();
        u.setId(jwt.getClaim("sub"));
        u.setEmail(jwt.getClaim("email"));
        u.setFirst_name(jwt.getClaim("given_name"));
        u.setLast_name(jwt.getClaim("family_name"));
        entity.setAuthor(u);
        questionJPA.save(entity);
        return entity;
    }
    //@PreAuthorize("hasRole('aluno')")
    @GetMapping("{id}")
    public ResponseEntity<Question> get(@PathVariable long id) {
        Optional<Question> question = questionJPA.findById(id);
        return question.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('professor')")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping()
    public Question putMethodName(@RequestBody Question entity,@AuthenticationPrincipal Jwt jwt) {
        var r = questionJPA.findByIdAndAuthorId(entity.getId(),jwt.getClaim("sub"));
        if(r.isPresent()) {
            Question q = questionJPA.save(entity);
            return q;
        }else{
            return null;
        }
    }
    @PreAuthorize("hasRole('professor')")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping({"{id}"})
    public boolean deleteMethodName(@PathVariable long id,@AuthenticationPrincipal Jwt jwt) {
        var r = questionJPA.findByIdAndAuthorId(id,jwt.getClaim("sub"));
        if(r.isPresent()) {
            questionJPA.delete(r.get());
            return true;
        }else{
            return false;
        }
        
    }
}
