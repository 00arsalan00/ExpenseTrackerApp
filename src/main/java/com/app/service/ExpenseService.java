package com.app.service;

import java.time.LocalDate;
import org.springframework.data.domain.*;
import com.app.request.ExpenseCreateRequestDto;
import com.app.response.ExpenseResponseDto;

public interface ExpenseService {
	
	ExpenseResponseDto createExpense(String userId,ExpenseCreateRequestDto expenseCreate);
	
	ExpenseResponseDto getExpenseById(String userId,Long expenseId);
	
	Page<ExpenseResponseDto> getAllExpenses(String userId,LocalDate from,LocalDate to,Pageable pageable);
	
	public byte[] exportToCsv(String userId,LocalDate from,LocalDate to);
	
	void deleteExpense(String userId,Long expenseId);

}
