package com.example.ending.repository;

import com.example.ending.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    int countByAttemptTimeAfter(Date date);
    int countByUsernameAndSuccessAndAttemptTimeAfter(String username, int success, Date date);
}
