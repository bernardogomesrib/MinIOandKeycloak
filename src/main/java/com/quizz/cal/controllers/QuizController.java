package com.quizz.cal.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizz.cal.JPA.QuizJPA;
import com.quizz.cal.objects.Quiz;

@RestController
@RequestMapping("/quiz")
public class QuizController {
    @Autowired
    private QuizJPA quizJPA;

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Long id) {
        Optional<Quiz> quiz = quizJPA.findById(id);
        return quiz.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        Quiz savedQuiz = quizJPA.save(quiz);
        return ResponseEntity.ok(savedQuiz);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        if (quizJPA.existsById(id)) {
            quizJPA.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}
