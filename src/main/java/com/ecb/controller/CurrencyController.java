package com.ecb.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecb.model.Currency;
import com.ecb.service.CurrencyService;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

	@Autowired
	private CurrencyService currencyService;

	@GetMapping("/{startDate}")
	public List<Currency> getByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate) {
		return currencyService.getByDate(startDate);
	}

	@GetMapping("/{startDate}/{originCurrency}/{destinationCurrency}/{value}")
	public BigDecimal getConvertedValueByParameter(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@PathVariable String originCurrency, @PathVariable String destinationCurrency,
			@PathVariable BigDecimal value) {
		return currencyService.getConvertedValueByParameter(startDate, originCurrency, destinationCurrency, value);
	}

}
