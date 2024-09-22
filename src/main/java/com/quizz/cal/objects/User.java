package com.quizz.cal.objects;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user_entity")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String email;
    private String first_name;
    private String last_name;
    private String realm_id;
    private String username;
    private boolean enabled;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserAnswerToQuiz> userAnswerToQuizzes;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Quiz> participationList;
    public String getFullName() {
        return this.first_name + " " + this.last_name;
    }

}