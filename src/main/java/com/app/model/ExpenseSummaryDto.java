package com.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import com.app.domain.ExpenseCategory;
import com.app.domain.ExpenseType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExpenseSummaryDto {

    private Long id;
    private BigDecimal amount;
    private ExpenseCategory category;
    private ExpenseType type;
    private String description;
    private LocalDate expenseDate;
    private OffsetDateTime createdAt;

}
