package com.quizz.cal.JPA;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.quizz.cal.objects.User;

public interface UserJPA extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.email LIKE %:value% OR u.first_name LIKE %:value% OR u.last_name LIKE %:value% OR CONCAT(u.first_name, ' ', u.last_name) LIKE %:value% AND u.realm_id = :realm_id AND u.enabled = true")
    List<User> findByAnyAndRealm_id(String value, String realm_id);
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.realm_id = :realm_id AND u.enabled = true")
    Optional<User> findByIdAndRealmId(String id, String realm_id);
}
