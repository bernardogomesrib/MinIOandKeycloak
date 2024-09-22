package com.quizz.cal.JPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.cal.objects.UserAnswerToQuiz;

public interface UserAnswerToQuizJPA extends JpaRepository<UserAnswerToQuiz, Long> {
    
}
