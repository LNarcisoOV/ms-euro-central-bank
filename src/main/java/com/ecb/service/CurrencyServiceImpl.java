package com.ecb.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecb.model.Currency;

@Service
public class CurrencyServiceImpl implements CurrencyService {

	@Autowired
	private List<Currency> currencyList;

	public List<Currency> getByDate(LocalDate date) {
		return currencyList.stream().filter(currency -> currency.getDate().equals(date)).collect(Collectors.toList());
	}

	@Override
	public BigDecimal getConvertedValueByParameters(LocalDate startDate, String originCurrency,
			String destinationCurrency, BigDecimal value) {
		BigDecimal conversionCalculation = BigDecimal.ZERO;

		List<Currency> currencyListByParameters = getByDate(startDate);

		Optional<Currency> originCurrencyOpt = currencyListByParameters.stream()
				.filter(currency -> currency.getName().equalsIgnoreCase(originCurrency)).findFirst();

		Optional<Currency> destinationCurrencyOpt = currencyListByParameters.stream()
				.filter(currency -> currency.getName().equalsIgnoreCase(destinationCurrency)).findFirst();

		if (originCurrencyOpt.isPresent() && destinationCurrencyOpt.isPresent()) {
			conversionCalculation = value.multiply(destinationCurrencyOpt.get().getValue());
		} else {
			throw new RuntimeException(
					"An error occurred when trying to convert values, please review the parameters.");
		}

		return conversionCalculation;
	}

	@Override
	public BigDecimal getConvertedValueByParameters(LocalDate startDate, LocalDate endDate, String originCurrency) {
		Optional<Currency> currencyOpt = currencyList.stream()
				.filter(currency -> currency.getName().equalsIgnoreCase(originCurrency)
						&& (currency.getDate().isAfter(startDate.minusDays(1)) && currency.getDate().isBefore(endDate.plusDays(1))))
				.max(Comparator.comparing(Currency::getValue));
		return currencyOpt.isPresent() ? currencyOpt.get().getValue() : BigDecimal.ZERO;
	}

	@Override
	public BigDecimal getAvarageValueBy(LocalDate startDate, LocalDate endDate, String originCurrency) {
		List<Currency> currencyListByParameters = currencyList.stream()
		.filter(currency -> currency.getName().equalsIgnoreCase(originCurrency)
				&& (currency.getDate().isAfter(startDate.minusDays(1)) && currency.getDate().isBefore(endDate.plusDays(1))))
		.collect(Collectors.toList());
		
		BigDecimal sum = currencyListByParameters.stream().map(Currency::getValue).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	
		return sum.divide(BigDecimal.valueOf(currencyListByParameters.size()));
	}

}
