package com.app.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyReportDto {
    private Integer year;
    private Integer month;
    private BigDecimal totalAmount;
}

