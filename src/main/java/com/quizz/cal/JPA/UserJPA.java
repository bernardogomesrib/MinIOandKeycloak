package com.quizz.cal.JPA;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizz.cal.objects.User;

public interface UserJPA extends JpaRepository<User, String> {
    
}
