package com.quizz.cal.JPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.cal.objects.Quiz;

public interface QuizJPA extends JpaRepository<Quiz, Long> {
    
}
