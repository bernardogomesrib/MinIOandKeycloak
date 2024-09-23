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

import com.quizz.cal.JPA.QuizJPA;
import com.quizz.cal.objects.Quiz;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/quiz")
public class QuizController {
    @Autowired
    private QuizJPA quizJPA;
    @PreAuthorize("hasRole('aluno')")
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuiz(@RequestParam Long id) {
        Optional<Quiz> quiz = quizJPA.findById(id);
        return quiz.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('professor')")
    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        Quiz savedQuiz = quizJPA.save(quiz);
        return ResponseEntity.ok(savedQuiz);
    }
    @PreAuthorize("hasRole('professor')")
    @PutMapping("")
    public ResponseEntity<Quiz> updateQuiz(@RequestBody Quiz quizDetails) {
        Optional<Quiz> optionalQuiz = quizJPA.findById(quizDetails.getId());
        if (optionalQuiz.isPresent()) {
            Quiz updatedQuiz = quizJPA.save(optionalQuiz.get());
            return ResponseEntity.ok(updatedQuiz);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasRole('professor')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@RequestParam Long id) {
        if (quizJPA.existsById(id)) {
            quizJPA.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}
