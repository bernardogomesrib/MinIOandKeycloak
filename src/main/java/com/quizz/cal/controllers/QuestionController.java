package com.quizz.cal.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quizz.cal.JPA.QuestionJPA;
import com.quizz.cal.objects.Question;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;




@RestController()
@RequestMapping("/question")
@SecurityRequirement(name = "bearerAuth")
public class QuestionController {

    @Autowired
    private QuestionJPA questionJPA;
    @PreAuthorize("hasRole('professor')")
    @PostMapping()
    public Question post(@RequestBody Question entity) {
        questionJPA.save(entity);
        return entity;
    }
    @PreAuthorize("hasRole('aluno')")
    @GetMapping("{id}")
    public ResponseEntity<Question> get(@RequestParam long id) {
        Optional<Question> question = questionJPA.findById(id);
        return question.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('professor')")
    @PutMapping()
    public Question putMethodName(@RequestBody Question entity) {
        var r = questionJPA.findById(entity.getId());
        if(r.isPresent()) {
            Question q = questionJPA.save(entity);
            return q;
        }else{
            return null;
        }
    }
    @PreAuthorize("hasRole('professor')")
    @DeleteMapping({"{id}"})
    public boolean deleteMethodName(@RequestParam long id) {
        var r = questionJPA.findById(id);
        if(r.isPresent()) {
            questionJPA.delete(r.get());
            return true;
        }else{
            return false;
        }
        
    }
}
