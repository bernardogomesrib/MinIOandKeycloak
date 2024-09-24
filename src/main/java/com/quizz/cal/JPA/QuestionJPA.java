package com.quizz.cal.JPA;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.cal.objects.Question;


public interface QuestionJPA extends JpaRepository<Question, Long> {
    Optional<Question> findByIdAndAuthorId(long id,String authorId);
}
