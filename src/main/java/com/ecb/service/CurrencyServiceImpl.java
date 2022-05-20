package com.ecb.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecb.model.Currency;

@Service
public class CurrencyServiceImpl implements CurrencyService {
	
	@Autowired
	private List<Currency> currencyList;
	
	  
	public List<Currency> getByDate(LocalDate date){
		return currencyList.stream().filter(currency -> currency.getDate().equals(date)).collect(Collectors.toList());
	}
	
	

}
