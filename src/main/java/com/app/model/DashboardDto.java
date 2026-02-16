package com.app.model;

import java.math.BigDecimal;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardDto {
	private BigDecimal totalExpenses;
	private BigDecimal thisMonthExpenses;
	private String topCategory;
	private BigDecimal averageDailyExpense;
	
}
