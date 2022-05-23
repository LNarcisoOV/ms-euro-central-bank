package com.ecb.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.ecb.model.Currency;

@RunWith(SpringRunner.class)
public class CurrencyServiceImplTest {

	private static final String INVALID_CURRENCY_NAME = "ABC";
	private static final String USD_NAME = "USD";
	private static final String EUR_NAME = "EUR";
	private static final String JPY_NAME = "JPY";
	private static final String BRL_NAME = "BRL";
	private static final String CZK_NAME = "CZK";
	private static final BigDecimal USD_VALUE = new BigDecimal("1.5");
	private static final BigDecimal EUR_VALUE = new BigDecimal("1.8");
	private static final BigDecimal JPY_VALUE = new BigDecimal("3.5");
	private static final BigDecimal BRL_VALUE = new BigDecimal("6.5");
	private static final BigDecimal CZK_VALUE = new BigDecimal("1.3");
	private static final LocalDate MAY_FIRST = LocalDate.of(2022, Month.MAY, 01);

	@Mock
	private List<Currency> currencyList;

	@InjectMocks
	private CurrencyServiceImpl currencyService;

	@Test
	public void getByDate() throws Exception {
		when(currencyList.stream()).thenReturn(generalCurrencyList().stream());

		List<Currency> currencies = currencyService.getByDate(MAY_FIRST);

		assertEquals(1, currencies.size());
		assertEquals(currencies.get(0).getName(), USD_NAME);
		assertEquals(currencies.get(0).getValue(), USD_VALUE);

		when(currencyList.stream()).thenReturn(currencyListMayFirst().stream());

		currencies = currencyService.getByDate(MAY_FIRST);

		assertEquals(4, currencies.size());
		assertEquals(currencies.get(0).getName(), USD_NAME);
		assertEquals(currencies.get(1).getName(), EUR_NAME);
		assertEquals(currencies.get(2).getName(), JPY_NAME);
		assertEquals(currencies.get(3).getName(), BRL_NAME);
		assertEquals(currencies.get(0).getValue(), USD_VALUE);
		assertEquals(currencies.get(1).getValue(), EUR_VALUE);
		assertEquals(currencies.get(2).getValue(), JPY_VALUE);
		assertEquals(currencies.get(3).getValue(), BRL_VALUE);
	}

	@Test
	public void getConvertedValueBy() throws Exception {
		when(currencyList.stream()).thenReturn(currencyListMayFirst().stream());

		BigDecimal valueToBeConverted = new BigDecimal("5.0");

		BigDecimal convertedValue = currencyService.getConvertedValueBy(MAY_FIRST, USD_NAME, EUR_NAME,
				valueToBeConverted);

		assertEquals(EUR_VALUE.multiply(valueToBeConverted), convertedValue);
	}

	@Test
	public void getConvertedValueByInvalidParameters_ThrowRuntimeException() throws Exception {
		try {
			when(currencyList.stream()).thenReturn(currencyListMayFirst().stream());
			BigDecimal valueToBeConverted = new BigDecimal("5.0");
			currencyService.getConvertedValueBy(MAY_FIRST, INVALID_CURRENCY_NAME, EUR_NAME, valueToBeConverted);
		} catch (RuntimeException e) {
			assertEquals("An error occurred when trying to convert values, please review the parameters.",
					e.getMessage());
		}

	}
	
	@Test
	public void getHigherValueBy() throws Exception {
		when(currencyList.stream()).thenReturn(currencyListForHigherValue().stream());

		BigDecimal higherValue = currencyService.getHigherValueBy(LocalDate.of(2022, Month.JANUARY, 01), MAY_FIRST, USD_NAME);

		assertEquals(USD_VALUE.add(new BigDecimal("5")), higherValue);
	}
	
	@Test
	public void getHigherValueInvalidPeriod_shouldReturnZERO() throws Exception {
		when(currencyList.stream()).thenReturn(currencyListForHigherValue().stream());

		BigDecimal higherValue = currencyService.getHigherValueBy(LocalDate.of(2021, Month.JANUARY, 01),
				LocalDate.of(2021, Month.DECEMBER, 01), USD_NAME);

		assertEquals(BigDecimal.ZERO, higherValue);
	}

	@Test
	public void getHigherValueInvalidCurrencyName_shouldReturnZERO() throws Exception {
		when(currencyList.stream()).thenReturn(currencyListForHigherValue().stream());

		BigDecimal higherValue = currencyService.getHigherValueBy(LocalDate.of(2022, Month.JANUARY, 01), MAY_FIRST,
				"ABC");

		assertEquals(BigDecimal.ZERO, higherValue);
	}

	@Test
	public void getAvarageValueBy() throws Exception {
		when(currencyList.stream()).thenReturn(currencyListForHigherValue().stream());

		BigDecimal avarageValue = currencyService.getAvarageValueBy(LocalDate.of(2022, Month.JANUARY, 01), MAY_FIRST,
				USD_NAME);

		assertEquals(new BigDecimal("5.0"), avarageValue);
	}

	private List<Currency> currencyListMayFirst() {
		List<Currency> currencies = new ArrayList<>();

		currencies.add(new Currency(MAY_FIRST, USD_NAME, USD_VALUE));
		currencies.add(new Currency(MAY_FIRST, EUR_NAME, EUR_VALUE));
		currencies.add(new Currency(MAY_FIRST, JPY_NAME, JPY_VALUE));
		currencies.add(new Currency(MAY_FIRST, BRL_NAME, BRL_VALUE));

		return currencies;
	}

	private List<Currency> currencyListForHigherValue() {
		List<Currency> currencies = new ArrayList<>();

		currencies.add(new Currency(LocalDate.of(2022, Month.JANUARY, 01), USD_NAME, USD_VALUE.add(new BigDecimal("2"))));
		currencies.add(new Currency(LocalDate.of(2022, Month.FEBRUARY, 01), USD_NAME, USD_VALUE.add(new BigDecimal("3"))));
		currencies.add(new Currency(LocalDate.of(2022, Month.MARCH, 01), USD_NAME, USD_VALUE.add(new BigDecimal("4"))));
		currencies.add(new Currency(LocalDate.of(2022, Month.APRIL, 01), USD_NAME, USD_VALUE.add(new BigDecimal("5"))));

		return currencies;
	}

	private List<Currency> generalCurrencyList() {
		List<Currency> currencies = new ArrayList<>();

		currencies.add(new Currency(LocalDate.of(2021, Month.DECEMBER, 01), BRL_NAME, BRL_VALUE));
		currencies.add(new Currency(LocalDate.of(2022, Month.JANUARY, 25), JPY_NAME, JPY_VALUE));
		currencies.add(new Currency(LocalDate.of(2022, Month.FEBRUARY, 12), CZK_NAME, CZK_VALUE));
		currencies.add(new Currency(LocalDate.of(2022, Month.APRIL, 05), EUR_NAME, EUR_VALUE));
		currencies.add(new Currency(LocalDate.of(2022, Month.MAY, 01), USD_NAME, USD_VALUE));

		return currencies;
	}
}
