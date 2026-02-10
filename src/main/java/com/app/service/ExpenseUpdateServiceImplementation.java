package com.app.service;

import org.springframework.stereotype.Service;

import com.app.entity.Expense;
import com.app.repository.ExpenseRepository;
import com.app.request.ExpenseUpdateRequestDto;
import com.app.response.ExpenseResponseDto;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseUpdateServiceImplementation
        implements ExpenseUpdateService {

    private final ExpenseRepository expenseRepository;

    @Override
    public ExpenseResponseDto updateExpenses(
            String userId,
            Long expenseId,
            ExpenseUpdateRequestDto request) {

        Expense expense = expenseRepository
                .findByIdAndUserId(expenseId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Expense Not Found"));

        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setType(request.getType());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setDescription(request.getDescription());

        Expense updated = expenseRepository.save(expense);
        return mapToResponse(updated);
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
