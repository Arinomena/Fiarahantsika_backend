package com.fiarahantsika.backend.stats.dto;

import java.math.BigDecimal;
import java.sql.Date;

public record SalesByDayDTO(Date day, BigDecimal totalSales) {

}
