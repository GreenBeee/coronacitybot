package com.lnu.coronacitybot.service;

import com.lnu.coronacitybot.model.CountryStatistic;

import java.util.List;
import java.util.Map;

public interface CovidStatisticService {

	CountryStatistic getCountryStatisticFromResource(String country);

	List<CountryStatistic> getAllStatistic();

	List<CountryStatistic> getTopStatistic(int count);

	void updateAllCountriesStatistic();

	Map<String, CountryStatistic> updateStatistic();
}
