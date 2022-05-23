package com.ecb.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.ecb.model.Currency;
import com.ecb.service.CurrencyService;

@RunWith(SpringRunner.class)
@WebMvcTest(CurrencyController.class)
public class CurrencyControllerITTest {

	private static final String USD_NAME = "USD";
	private static final String EUR_NAME = "EUR";
	private static final BigDecimal USD_VALUE = new BigDecimal("1.5");
	private static final BigDecimal EUR_VALUE = new BigDecimal("1.8");
	private static final BigDecimal USD_EUR_CONVERTED_VALUE = new BigDecimal("0.7");

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CurrencyService currencyService;

	@Test
	public void getByDate() throws Exception {
		given(currencyService.getByDate(Mockito.any())).willReturn(currencies1());

		mockMvc.perform(get("/currency/2022-01-01")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name", is(USD_NAME)))
				.andExpect(jsonPath("$[0].value", is(USD_VALUE.doubleValue())))
				.andExpect(jsonPath("$[1].name", is(EUR_NAME)))
				.andExpect(jsonPath("$[1].value", is(EUR_VALUE.doubleValue())));
	}

	@Test
	public void getByDateWithInvalidaDate() throws Exception {
		given(currencyService.getByDate(Mockito.any())).willReturn(currencies1());

		mockMvc.perform(get("/currency/ABC")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void getConvertedValueByParameters() throws Exception {
		given(currencyService.getConvertedValueBy(Mockito.any(), Mockito.anyString(), Mockito.anyString(),
				Mockito.any())).willReturn(USD_EUR_CONVERTED_VALUE);

		mockMvc.perform(get("/currency/2022-01-01/USD/EUR/1.0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(USD_EUR_CONVERTED_VALUE.toString()));
	}
	
	@Test
	public void getConvertedValueByInvalidValue() throws Exception {
		given(currencyService.getConvertedValueBy(Mockito.any(), Mockito.anyString(), Mockito.anyString(),
				Mockito.any())).willReturn(USD_EUR_CONVERTED_VALUE);

		mockMvc.perform(get("/currency/2022-01-01/USD/EUR/ABC")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void getConvertedValueByInvalidDate() throws Exception {
		given(currencyService.getConvertedValueBy(Mockito.any(), Mockito.anyString(), Mockito.anyString(),
				Mockito.any())).willReturn(USD_EUR_CONVERTED_VALUE);

		mockMvc.perform(get("/currency/2022-01/ABC/EUR/1.0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void getMostHighValueByParameters() throws Exception {
		given(currencyService.getHigherValueBy(Mockito.any(), Mockito.any(), Mockito.anyString()))
		.willReturn(USD_EUR_CONVERTED_VALUE);

		mockMvc.perform(get("/currency/2022-01-01/2022-01-10/EUR")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(USD_EUR_CONVERTED_VALUE.toString()));
	}
	
	@Test
	public void getMostHighValueByParametersInvalidOriginDate() throws Exception {
		given(currencyService.getHigherValueBy(Mockito.any(), Mockito.any(), Mockito.anyString()))
		.willReturn(USD_EUR_CONVERTED_VALUE);

		mockMvc.perform(get("/currency/2022-01/2022-01-10/EUR")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void getMostHighValueByParametersInvalidDestinationDate() throws Exception {
		given(currencyService.getHigherValueBy(Mockito.any(), Mockito.any(), Mockito.anyString()))
		.willReturn(USD_EUR_CONVERTED_VALUE);

		mockMvc.perform(get("/currency/2022-01-01/2022-10/EUR")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void getAvarageValueByParameters() throws Exception {
		given(currencyService.getAvarageValueBy(Mockito.any(), Mockito.any(), Mockito.anyString()))
		.willReturn(USD_EUR_CONVERTED_VALUE);

		mockMvc.perform(get("/currency/avarage/2022-01-01/2022-01-10/EUR")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(USD_EUR_CONVERTED_VALUE.toString()));
	}
	
	@Test
	public void getAvarageValueByParametersInvalidOriginDate() throws Exception {
		given(currencyService.getAvarageValueBy(Mockito.any(), Mockito.any(), Mockito.anyString()))
		.willReturn(USD_EUR_CONVERTED_VALUE);

		mockMvc.perform(get("/currency/avarage/2022-01/2022-01-10/EUR")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void getAvarageValueByParametersInvalidDestinationDate() throws Exception {
		given(currencyService.getAvarageValueBy(Mockito.any(), Mockito.any(), Mockito.anyString()))
		.willReturn(USD_EUR_CONVERTED_VALUE);

		mockMvc.perform(get("/currency/avarage/2022-01-01/2022-10/EUR")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	private List<Currency> currencies1() {
		List<Currency> currencies = new ArrayList<>();

		currencies.add(new Currency(LocalDate.now(), USD_NAME, USD_VALUE));
		currencies.add(new Currency(LocalDate.now(), EUR_NAME, EUR_VALUE));
		
		return currencies;
	}

}
