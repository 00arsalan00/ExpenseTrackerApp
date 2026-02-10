package com.app.service;

import com.app.request.ExpenseUpdateRequestDto;
import com.app.response.ExpenseResponseDto;

import lombok.Getter;
import lombok.Setter;

public interface ExpenseUpdateService {
	
	ExpenseResponseDto updateExpenses(
			String userId,Long expenseId,ExpenseUpdateRequestDto request
			);

}
