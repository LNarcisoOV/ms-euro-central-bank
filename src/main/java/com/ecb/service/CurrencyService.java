package com.ecb.service;

import java.time.LocalDate;
import java.util.List;

import com.ecb.model.Currency;

public interface CurrencyService {
	
	List<Currency> getByDate(LocalDate date);

}
