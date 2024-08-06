package com.example.ending.repository;

import com.example.ending.entity.RegistrationAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RegistrationAttemptRepository extends JpaRepository<RegistrationAttempt, Long> {
    int countByAttemptTimeAfter(Date date);
}

