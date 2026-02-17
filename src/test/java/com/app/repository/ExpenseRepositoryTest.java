package com.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.app.domain.ExpenseCategory;
import com.app.domain.ExpenseType;
import com.app.entity.Expense;
import com.app.model.MonthlyReportDto;


@DataJpaTest
public class ExpenseRepositoryTest {
	
	@Autowired
	private ExpenseRepository expenseRepository;
	
	//Date range query test
	@Test
	void shouldReturnExpensesWithinDateRange() {
		Expense expense1 = createExpense("user1",
				LocalDate.of(2025, 1, 1),
				BigDecimal.valueOf(100)
				);
		Expense expense2 = createExpense("user1",
				LocalDate.of(2024, 5, 1),
				BigDecimal.valueOf(200)
				);
		Expense expense3 = createExpense("user1",
				LocalDate.of(2024, 1, 1),
				BigDecimal.valueOf(300)
				);
		Expense expense4 = createExpense("user1",
				LocalDate.of(2023, 1, 1),
				BigDecimal.valueOf(400)
				);
		
		expenseRepository.saveAll(List.of(expense1,expense2,expense3,expense4));
		
		var page = expenseRepository
				.findAllByUserIdAndExpenseDateBetween("user1",LocalDate.of(2024, 1, 1) , 
						LocalDate.of(2025, 1, 1), 
						org.springframework.data.domain.Pageable.unpaged());
		
		assertEquals(3, page.getContent().size());
		
		
	}
	
	//Monthly range test
	
	@Test
	void shouldGroupAndSumMonthlyExpenses() {
		Expense jan1 = createExpense("user1", 
				LocalDate.of(2024, 1, 25), BigDecimal.valueOf(100));
		Expense feb1 = createExpense("user1", 
				LocalDate.of(2024, 2, 2), BigDecimal.valueOf(200));
		Expense feb2 = createExpense("user1", 
				LocalDate.of(2024, 2, 25), BigDecimal.valueOf(300));
		
		expenseRepository.saveAll(List.of(jan1,feb1,feb2));
		
		List<MonthlyReportDto> report = expenseRepository.getMonthlyReport("user1");
		
		assertEquals(2, report.size());
		
		assertTrue(
	            report.stream().anyMatch(r ->
	                    r.getMonth() == 2 &&
	                    r.getTotalAmount()
	                            .compareTo(BigDecimal.valueOf(500))==0
	            )
	    );
		
	}
	
	
	//Helper method
	private Expense createExpense(String userId, LocalDate date, BigDecimal amount) {
		Expense expense = new Expense();
		expense.setUserId(userId);
		expense.setAmount(amount);
		expense.setExpenseDate(date);
		expense.setCategory(ExpenseCategory.FOOD);
		expense.setType(ExpenseType.DEBIT);
		expense.setDescription("Hello");
		
		return expense;
		
	}

}
