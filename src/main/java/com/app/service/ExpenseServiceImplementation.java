package com.app.service;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.request.*;
import com.app.response.*;
import com.app.entity.Expense;
import com.app.exception.InvalidDateRangeException;
import com.app.repository.ExpenseRepository;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ExpenseServiceImplementation implements ExpenseService {
	
	private final ExpenseRepository expenseRepository;
	
	@CacheEvict(value = "dashboard", key = "#userId")
	@Override
	public ExpenseResponseDto createExpense(String userId,ExpenseCreateRequestDto request) {
		
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
	public ExpenseResponseDto getExpenseById(String userId,Long expenseId) {
		Expense expense = expenseRepository.findByIdAndUserId(expenseId, userId).orElseThrow(
				() -> new EntityNotFoundException("Expense Not Found")
				);
		return mapToResponse(expense);
	}
	
	@Override
	public Page<ExpenseResponseDto> getAllExpenses(String userId,LocalDate from,LocalDate to,Pageable pageable) {

	    
	    if (from != null && to != null) {

	        if (from.isAfter(to)) {
	            throw new InvalidDateRangeException(
	                "From date cannot be after To date"
	            );
	        }

	        return expenseRepository
	                .findAllByUserIdAndExpenseDateBetween(
	                        userId, from, to, pageable)
	                .map(this::mapToResponse);
	    }

	    if (from != null || to != null) {
	        throw new InvalidDateRangeException(
	            "Both from and to dates must be provided"
	        );
	    }

	    return expenseRepository
	            .findAllByUserId(userId, pageable)
	            .map(this::mapToResponse);
	}

	@CacheEvict(value = "dashboard",key = "#userId")
	@Override
	public void deleteExpense(String userId,Long expenseId) {
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
	
	public byte[] exportToCsv(String userId, LocalDate from, LocalDate to) {

	    if ((from != null && to == null) || (from == null && to != null)) {
	        throw new InvalidDateRangeException("Both from and to dates must be provided");
	    }

	    if (from != null && from.isAfter(to)) {
	        throw new InvalidDateRangeException("From date cannot be after To date");
	    }

	    List<Expense> expenses;

	    if (from != null && to != null) {
	        expenses = expenseRepository
	                .findAllByUserIdAndExpenseDateBetween(
	                        userId, from, to, Pageable.unpaged())
	                .getContent();
	    } else {
	        expenses = expenseRepository
	                .findAllByUserId(userId, Pageable.unpaged())
	                .getContent();
	    }

	    try (
	        StringWriter writer = new StringWriter();
	        org.apache.commons.csv.CSVPrinter csvPrinter =
	                new org.apache.commons.csv.CSVPrinter(
	                        writer,
	                        org.apache.commons.csv.CSVFormat.DEFAULT
	                                .withHeader(
	                                        "ID",
	                                        "Amount",
	                                        "Category",
	                                        "Type",
	                                        "Expense Date",
	                                        "Description",
	                                        "Created At"
	                                )
	                )
	    ) {

	        for (Expense expense : expenses) {
	            csvPrinter.printRecord(
	                    expense.getId(),
	                    expense.getAmount(),
	                    expense.getCategory(),
	                    expense.getType(),
	                    expense.getExpenseDate(),
	                    expense.getDescription(),
	                    expense.getCreatedAt()
	            );
	        }

	        csvPrinter.flush();
	        return writer.toString().getBytes();

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to export to CSV", e);
	    }
	}
}
