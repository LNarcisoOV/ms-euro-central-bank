package com.ecb.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecb.model.Currency;
import com.ecb.util.Constants;

@Configuration
public class CsvConfig {

	@Bean
	public List<Currency> loadCsv() throws IOException {
		List<Currency> currencyList = new ArrayList<Currency>();

		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(Constants.CSV_FILE_NAME).getFile());
			InputStream inputStream = new FileInputStream(file);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			currencyList = fillByCsv(bufferedReader.lines());
			bufferedReader.close();
		} catch (IOException e) {
			throw new IOException();
		}

		return currencyList;
	}

	private List<Currency> fillByCsv(Stream<String> lines) {
		int counterLines = 0;
		List<Currency> currencyList = new ArrayList<Currency>();

		List<String> listCsvValues = lines.collect(Collectors.toList());
		String headerInCsv = listCsvValues.stream().findFirst().get();
		String[] headerSplitted = headerInCsv.split(",");

		for (String line : listCsvValues) {
			if (counterLines > 0) {
				String[] columns = line.split(",");
				for (int i = 1; i < 42; i++) {
					Currency currency = new Currency();
					currency.setDate(LocalDate.parse(columns[0]));
					currency.setName(headerSplitted[i]);
					if (columns[i].equals("N/A")) {
						currency.setValue(BigDecimal.ZERO);
					} else {
						currency.setValue(new BigDecimal(columns[i]));
					}

					currencyList.add(currency);
				}
			}
			counterLines++;
		}

		return currencyList;
	}
}
