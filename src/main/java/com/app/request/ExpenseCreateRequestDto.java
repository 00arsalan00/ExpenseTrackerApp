package com.app.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;

import com.app.domain.ExpenseCategory;
import com.app.domain.ExpenseType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseCreateRequestDto {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Category is required")
    private ExpenseCategory category;

    @NotNull(message = "Expense type is required")
    private ExpenseType type;

    @NotNull(message = "Expense date is required")
    @PastOrPresent(message = "Expense date cannot be in the future")
    private LocalDate expenseDate;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
}
