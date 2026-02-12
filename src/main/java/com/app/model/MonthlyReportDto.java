package com.app.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyReportDto {
	
	private String month;
	private BigDecimal totalAmount;
	
}
