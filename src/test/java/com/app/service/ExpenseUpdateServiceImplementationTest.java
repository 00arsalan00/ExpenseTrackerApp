package com.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.entity.Expense;
import com.app.repository.ExpenseRepository;
import com.app.request.ExpenseCreateRequestDto;
import com.app.request.ExpenseUpdateRequestDto;
import com.app.response.ExpenseResponseDto;

public class ExpenseUpdateServiceImplementationTest {
	private ExpenseRepository expenseRepository;
	private ExpenseUpdateServiceImplementation expenseUpdateServiceImplementation;
	
	@BeforeEach
    void setUp() {
        expenseRepository = mock(ExpenseRepository.class);
        expenseUpdateServiceImplementation =
                new ExpenseUpdateServiceImplementation(expenseRepository);
    }
	
	// Expense Update test
	@Test
	void shouldUpdateExpenseCorrectly() {
		ExpenseUpdateRequestDto request = new ExpenseUpdateRequestDto();
		request.setAmount(BigDecimal.valueOf(200));
        request.setExpenseDate(LocalDate.now());
        request.setDescription("Updated Description");
        
        Expense existingExpense = new Expense();
        existingExpense.setUserId("user1");
        existingExpense.setAmount(BigDecimal.valueOf(100));
        existingExpense.setExpenseDate(LocalDate.of(2024, 1, 1));
        existingExpense.setDescription("Old Description");
        

        when(expenseRepository.findByIdAndUserId(1L, "user1")).thenReturn(Optional.of(existingExpense));
        
        when(expenseRepository.save(any())).thenReturn(existingExpense);

        ExpenseResponseDto response =
        		expenseUpdateServiceImplementation.updateExpenses("user1",1L, request);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(200), existingExpense.getAmount());
        assertEquals("Updated Description", existingExpense.getDescription());
        
        verify(expenseRepository, times(1)).findByIdAndUserId(1L, "user1");
        verify(expenseRepository, times(1)).save(existingExpense);
        
        
	}
	

}
