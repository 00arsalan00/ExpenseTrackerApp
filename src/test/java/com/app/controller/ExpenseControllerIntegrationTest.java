package com.app.controller;

import com.app.domain.ExpenseCategory;
import com.app.domain.ExpenseType;
import com.app.entity.Expense;
import com.app.repository.ExpenseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ExpenseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Create Expense API Test

    @Test
    @WithMockUser(username = "user1")
    void shouldCreateExpenseSuccessfully() throws Exception {
    	expenseRepository.deleteAll();

        String requestJson = """
                {
                    "amount": 500,
                    "category": "FOOD",
                    "type": "DEBIT",
                    "expenseDate": "2025-01-01",
                    "description": "Integration Test"
                }
                """;

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(500))
                .andExpect(jsonPath("$.description").value("Integration Test"));
    }

    // Get Expenses API Test

    @Test
    @WithMockUser(username = "user1")
    void shouldReturnExpenses() throws Exception {
    	expenseRepository.deleteAll();

        Expense expense = new Expense();
        expense.setUserId("user1");
        expense.setAmount(BigDecimal.valueOf(300));
        expense.setCategory(ExpenseCategory.FOOD);
        expense.setType(ExpenseType.DEBIT);
        expense.setExpenseDate(LocalDate.of(2025, 1, 1));
        expense.setDescription("Test Expense");

        expenseRepository.save(expense);

        mockMvc.perform(get("/api/expenses/range?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].amount").value(300))
                .andExpect(jsonPath("$.content[0].description")
                        .value("Test Expense"));
    }
}
