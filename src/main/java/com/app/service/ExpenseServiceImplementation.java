package com.app.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.request.*;
import com.app.response.*;
import com.app.entity.Expense;
import com.app.repository.ExpenseRepository;
import com.app.service.ExpenseService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ExpenseServiceImplementation implements ExpenseService {
	
	private final ExpenseRepository expenseRepository;
	
	@Override
	public ExpenseResponseDto createExpense(Long userId,ExpenseCreateRequestDto request) {
		
		Expense expense = new Expense();
		expense.setUserId(userId);
		expense.setAmount(request.getAmount());
		expense.setCategory(request.getCategory());
		expense.setType(request.getType());
		expense.setDescription(request.getDescription());
		expense.setExpenseDate(request.getExpenseDate());
		
		Expense saved = expenseRepository.save(expense);
		
		return mapToResponse(saved);
		
	}
	
	@Override
	public ExpenseResponseDto getExpenseById(Long userId,Long expenseId) {
		Expense expense = expenseRepository.findByIdAndUserId(expenseId, userId).orElseThrow(
				() -> new EntityNotFoundException("Expense Not Found")
				);
		return mapToResponse(expense);
	}
	
	@Override
	public Page<ExpenseResponseDto> getAllExpenses(Long userId,LocalDate from,LocalDate to,Pageable pageable){
		
		if(from!=null && to!=null) {
			return expenseRepository.findAllByUserIdAndExpenseDateBetween(userId, from, to, pageable)
					.map(this::mapToResponse);
		}
		
		return expenseRepository.findAllByUserId(userId, pageable)
				.map(this::mapToResponse);
	}
	
	@Override
	public void deleteExpense(Long userId,Long expenseId) {
		Expense expense = expenseRepository.findByIdAndUserId(expenseId, userId)
				.orElseThrow(()-> new EntityNotFoundException("Expense Not Found"));
		expenseRepository.delete(expense);
	}
	
	private ExpenseResponseDto mapToResponse(Expense expense) {
		ExpenseResponseDto response = new ExpenseResponseDto();
		response.setId(expense.getId());
		response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setType(expense.getType());
        response.setExpenseDate(expense.getExpenseDate());
        response.setDescription(expense.getDescription());
        response.setCreatedAt(expense.getCreatedAt());
        return response;
		
	}
}
