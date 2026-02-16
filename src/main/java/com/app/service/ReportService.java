package com.app.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.domain.ExpenseCategory;
import com.app.entity.Expense;
import com.app.model.*;
import com.app.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
	
	private final ExpenseRepository expenseRepository;
	
	public List<MonthlyReportDto> getMonthlyReport(String userId){
		return expenseRepository.getMonthlyReport(userId);
	}
	
	public List<CategoryReportDto> getCategoryReport(String userId){
		return expenseRepository.getCategoryReport(userId);
	}
	
	@Cacheable(value = "dashboard", key = "#userId")
	public DashboardDto getDashboard(String userId) {
		BigDecimal total = expenseRepository.getTotalExpenses(userId);
		BigDecimal thisMonth = expenseRepository.getMonthlyExpenses(userId);
		
		List<ExpenseCategory> topCategories = expenseRepository.getTopCategory(userId);
		
		String topCategory = topCategories.isEmpty() ? null: topCategories.get(0).name();
		
		BigDecimal avgDaily = calculateDailyAverage(userId);
		
		return new DashboardDto(total, thisMonth, topCategory, avgDaily);
	}
	
	private BigDecimal calculateDailyAverage(String userId) {
		
		List<Expense> expenses = expenseRepository.findAllByUserId(userId, Pageable.unpaged()).getContent();
		
		
		Map<LocalDate, BigDecimal> dailyTotals = expenses.stream()
				.collect(Collectors.groupingBy(
						Expense::getExpenseDate,
						Collectors.reducing(
								BigDecimal.ZERO,
								Expense::getAmount,
								BigDecimal::add
								)
						));
		
		if (dailyTotals.isEmpty()) {
		    return BigDecimal.ZERO;
		}

		BigDecimal sumOfDailyTotals = dailyTotals.values().stream()
		        .reduce(BigDecimal.ZERO, BigDecimal::add);

		return sumOfDailyTotals.divide(
		        BigDecimal.valueOf(dailyTotals.size()),
		        2,
		        RoundingMode.HALF_UP
		);

	}


}
