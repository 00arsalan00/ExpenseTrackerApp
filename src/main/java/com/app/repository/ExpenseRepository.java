package com.app.repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.domain.ExpenseCategory;
import com.app.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByIdAndUserId(Long id, String userId);

    Page<Expense> findAllByUserId(String userId, Pageable pageable);

    Page<Expense> findAllByUserIdAndExpenseDateBetween(
        String userId,
        LocalDate from,
        LocalDate to,
        Pageable pageable
    );

    Page<Expense> findAllByUserIdAndCategory(
        String userId,
        ExpenseCategory category,
        Pageable pageable
    );
}
