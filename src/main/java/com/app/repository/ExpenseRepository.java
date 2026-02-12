package com.app.repository;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.domain.ExpenseCategory;
import com.app.entity.Expense;
import com.app.model.*;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	// Basic operation
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
    
    // Monthly Report
    @Query("""
    		SELECT new com.app.model.MonthlyReportDto(
    		FUNCTION('DATE_FORMAT',e.expenseDate,'%Y-%m'),
    		SUM(e.amount)
    		)
    		FROM expense e
    		WHERE e.userId = :userId
    		GROUP BY FUNCTION('DATE_FORMAT',e.expenseDate,'%Y-%m')
    		ORDER BY FUNCTION('DATE_FORMAT',e.expenseDate,'%Y-%m')
    		""")
    List<MonthlyReportDto> getMonthlyReport(String userId);
    
    // Categorywise Report
    @Query("""
    		SELECT new com.app.model.CategoryReportDto(
    		e.category,
    		SUM(e.amount)
    		)
    		FROM expense e
    		WHERE e.userId = :userId
    		GROUP BY e.category
    		ORDER BY SUM(e.amount) DESC
    		""")
    List<CategoryReportDto> getCategoryReport(String userId);
    
 // Total expenses
    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.userId = :userId
    """)
    BigDecimal getTotalExpenses(String userId);


    // Monthly expenses
    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.userId = :userId
        AND MONTH(e.expenseDate) = MONTH(CURRENT_DATE)
        AND YEAR(e.expenseDate) = YEAR(CURRENT_DATE)
    """)
    BigDecimal getMonthlyExpenses(String userId);

    
    //Top Category
    @Query("""
    		SELECT e.category
    		FROM expense e
    		WHERE e.userId = :userId
    		GROUP BY e.category
    		ORDER BY SUM(e.amount) DESC
    		""")
    List<ExpenseCategory> getTopCategory(String userId);
    
    
}
