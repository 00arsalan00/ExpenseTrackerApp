package com.app.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.app.domain.ExpenseCategory;
import com.app.domain.ExpenseType;
import com.app.entity.Expense;
import com.app.exception.InvalidDateRangeException;
import com.app.repository.ExpenseRepository;
import com.app.request.ExpenseCreateRequestDto;
import com.app.response.ExpenseResponseDto;

public class ExpenseServiceImplementationTest {

    private ExpenseServiceImplementation expenseServiceImplementation;
    private ExpenseRepository expenseRepository;

    @BeforeEach
    void setUp() {
        expenseRepository = mock(ExpenseRepository.class);
        expenseServiceImplementation =
                new ExpenseServiceImplementation(expenseRepository);
    }

    // Date Validation tests
    @Test
    void shouldThrowExceptionWhenFromIsAfterTo() {
        LocalDate from = LocalDate.of(2025, 12, 31);
        LocalDate to = LocalDate.of(2025, 1, 1);

        assertThrows(
                InvalidDateRangeException.class,
                () -> expenseServiceImplementation.getAllExpenses(
                        "user1", from, to, PageRequest.of(0, 10)
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenOnlyOneDateIsProvided() {
        LocalDate from = LocalDate.of(2025, 12, 31);

        assertThrows(
                InvalidDateRangeException.class,
                () -> expenseServiceImplementation.getAllExpenses(
                        "user1", from, null, PageRequest.of(0, 10)
                )
        );
    }

    // Create Expense test
    @Test
    void shouldCreateExpenseSuccessfully() {

        ExpenseCreateRequestDto request = new ExpenseCreateRequestDto();
        request.setAmount(BigDecimal.valueOf(100));
        request.setExpenseDate(LocalDate.now());

        Expense savedExpense = new Expense();
        savedExpense.setUserId("user1");
        savedExpense.setAmount(BigDecimal.valueOf(100));
        savedExpense.setExpenseDate(LocalDate.now());

        when(expenseRepository.save(any())).thenReturn(savedExpense);

        ExpenseResponseDto response =
                expenseServiceImplementation.createExpense("user1", request);

        assertNotNull(response);
        verify(expenseRepository, times(1)).save(any());
    }

    // Delete Expense test
    @Test
    void shouldDeleteExpenseWhenExists() {

        Expense expense = new Expense();
        expense.setUserId("user1");

        when(expenseRepository.findByIdAndUserId(1L, "user1"))
                .thenReturn(Optional.of(expense));

        expenseServiceImplementation.deleteExpense("user1", 1L);

        verify(expenseRepository, times(1)).delete(expense);
    }
    
    // CSV export test
    @Test
    void shouldExportCorrectly() {

        Expense expense = new Expense();
        expense.setUserId("user1");
        expense.setAmount(BigDecimal.valueOf(500));
        expense.setExpenseDate(LocalDate.of(2025, 1, 1));
        expense.setDescription("Test Expense");

        expense.setCategory(ExpenseCategory.SHOPPING);
        expense.setType(ExpenseType.DEBIT);

        Page<Expense> page = new PageImpl<>(List.of(expense));

        when(expenseRepository.findAllByUserId(
                eq("user1"),
                any(Pageable.class)
        )).thenReturn(page);

        byte[] csvBytes =
                expenseServiceImplementation.exportToCsv(
                        "user1", null, null);

        String csvContent = new String(csvBytes);

        assertNotNull(csvContent);
        assertTrue(csvContent.contains("ID"));
        assertTrue(csvContent.contains("Amount"));
        assertTrue(csvContent.contains("Test Expense"));
        assertTrue(csvContent.contains("500"));
    }

}
