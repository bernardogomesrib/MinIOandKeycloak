package com.quizz.cal.JPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.cal.objects.Question;

public interface QuestionJPA extends JpaRepository<Question, Long> {
    
}
