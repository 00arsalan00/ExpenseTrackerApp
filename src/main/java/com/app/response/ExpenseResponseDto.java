package com.app.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.app.domain.ExpenseCategory;
import com.app.domain.ExpenseType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseResponseDto {

    private Long id;
    private BigDecimal amount;
    private ExpenseCategory category;
    private ExpenseType type;
    private LocalDate expenseDate;
    private String description;
    private OffsetDateTime createdAt;
}
