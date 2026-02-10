package com.app.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.app.request.ExpenseCreateRequestDto;
import com.app.request.ExpenseUpdateRequestDto;
import com.app.response.ExpenseResponseDto;
import com.app.service.ExpenseService;
import com.app.service.ExpenseUpdateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Validated
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseUpdateService expenseUpdateService;

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> createExpense(
            @Valid @RequestBody ExpenseCreateRequestDto request,
            Authentication authentication
    ) {
    	String userId = authentication.getName();
        ExpenseResponseDto response = expenseService.createExpense(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> getExpenseById(
            @PathVariable Long id,
            Authentication authentication
    ) {
    	String userId = authentication.getName();
        return ResponseEntity.ok(
                expenseService.getExpenseById(userId, id)
        );
    }

    @GetMapping
    public Page<ExpenseResponseDto> getAllExpenses(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to,

            Pageable pageable,
            Authentication authentication
    ) {
    	String userId = authentication.getName();
        return expenseService.getAllExpenses(userId, from, to, pageable);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> updateExpenses(
    		@PathVariable Long id,
    		@Valid @RequestBody ExpenseUpdateRequestDto request,
    		Authentication authentication
    		){
    	
    	String userId = authentication.getName();
    	
    	ExpenseResponseDto response = expenseUpdateService.updateExpenses(userId, id, request);
    	
    	return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        expenseService.deleteExpense(userId, id);
    }
}
