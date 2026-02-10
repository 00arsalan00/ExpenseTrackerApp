package com.app.service;

import java.time.LocalDate;
import org.springframework.data.domain.*;
import com.app.request.ExpenseCreateRequestDto;
import com.app.response.ExpenseResponseDto;

public interface ExpenseService {
	
	ExpenseResponseDto createExpense(Long userId,ExpenseCreateRequestDto expenseCreate);
	
	ExpenseResponseDto getExpenseById(Long userId,Long expenseId);
	
	Page<ExpenseResponseDto> getAllExpenses(Long userId,LocalDate from,LocalDate to,Pageable pageable);
	
	void deleteExpense(Long userId,Long expenseId);

}
