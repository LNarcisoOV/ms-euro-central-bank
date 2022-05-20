package com.ecb.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.ecb.model.Currency;

public interface CurrencyService {
	
	List<Currency> getByDate(LocalDate date);

	BigDecimal getConvertedValueByParameter(LocalDate startDate, String originCurrency, String destinationCurrency,
			BigDecimal value);

}
