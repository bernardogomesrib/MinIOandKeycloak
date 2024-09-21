package com.quizz.cal.objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user_entity")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String first_name;
    private String last_name;
    private String realm_id;
    private String username;
    private boolean enabled;

    public String getFullName() {
        return this.first_name + " " + this.last_name;
    }
}