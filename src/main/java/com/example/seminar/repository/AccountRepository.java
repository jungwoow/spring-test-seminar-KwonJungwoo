package com.example.seminar.repository;

import com.example.seminar.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository
        extends JpaRepository<Account, Long> {
}