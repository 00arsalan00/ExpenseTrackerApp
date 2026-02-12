package com.app.model;

import java.math.BigDecimal;

import com.app.domain.ExpenseCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryReportDto {
	
	private ExpenseCategory categoryName;
	private BigDecimal totalAmount;
}
