package com.ecb.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<Currency>> getByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate) {
		return new ResponseEntity<>(currencyService.getByDate(startDate), HttpStatus.OK);
	}

	@GetMapping("/{startDate}/{originCurrency}/{destinationCurrency}/{value}")
	public ResponseEntity<BigDecimal> getConvertedValueByParameter(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@PathVariable String originCurrency, @PathVariable String destinationCurrency,
			@PathVariable BigDecimal value) {
		try {
			BigDecimal convertedValue = currencyService.getConvertedValueBy(startDate, originCurrency, destinationCurrency, value);
			return new ResponseEntity<>(convertedValue, HttpStatus.OK);
		} catch (RuntimeException runtimeException) {
			throw runtimeException;
		}
	}
	
	@GetMapping("/{startDate}/{endDate}/{originCurrency}")
	public ResponseEntity<BigDecimal> getHigherValueByParameters(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
			@PathVariable String originCurrency) {
		BigDecimal higherValue = currencyService.getHigherValueBy(startDate, endDate, originCurrency);
		return new ResponseEntity<>(higherValue, HttpStatus.OK);
	}
	
	@GetMapping("/avarage/{startDate}/{endDate}/{originCurrency}")
	public ResponseEntity<BigDecimal>  getAvarageValue(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
			@PathVariable String originCurrency) {
		BigDecimal avarageValue = currencyService.getAvarageValueBy(startDate, endDate, originCurrency);
		return new ResponseEntity<>(avarageValue, HttpStatus.OK);
	}

}
