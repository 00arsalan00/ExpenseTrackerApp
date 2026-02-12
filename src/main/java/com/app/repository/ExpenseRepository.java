package com.app.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.domain.ExpenseCategory;
import com.app.entity.Expense;
import com.app.model.CategoryReportDto;
import com.app.model.MonthlyReportDto;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // BASIC OPERATIONS 

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
    	        YEAR(e.expenseDate),
    	        MONTH(e.expenseDate),
    	        SUM(e.amount)
    	    )
    	    FROM Expense e
    	    WHERE e.userId = :userId
    	    GROUP BY YEAR(e.expenseDate), MONTH(e.expenseDate)
    	    ORDER BY YEAR(e.expenseDate), MONTH(e.expenseDate)
    	""")
    	List<MonthlyReportDto> getMonthlyReport(@Param("userId") String userId);



    // Category-wise Report
    @Query("""
        SELECT new com.app.model.CategoryReportDto(
            e.category,
            SUM(e.amount)
        )
        FROM Expense e
        WHERE e.userId = :userId
        GROUP BY e.category
        ORDER BY SUM(e.amount) DESC
    """)
    List<CategoryReportDto> getCategoryReport(@Param("userId") String userId);


    // Total Expenses
    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.userId = :userId
    """)
    BigDecimal getTotalExpenses(@Param("userId") String userId);


    // This Month Expenses
    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.userId = :userId
        AND MONTH(e.expenseDate) = MONTH(CURRENT_DATE)
        AND YEAR(e.expenseDate) = YEAR(CURRENT_DATE)
    """)
    BigDecimal getMonthlyExpenses(@Param("userId") String userId);


    // Top Category
    @Query("""
        SELECT e.category
        FROM Expense e
        WHERE e.userId = :userId
        GROUP BY e.category
        ORDER BY SUM(e.amount) DESC
    """)
    List<ExpenseCategory> getTopCategory(@Param("userId") String userId);
}
