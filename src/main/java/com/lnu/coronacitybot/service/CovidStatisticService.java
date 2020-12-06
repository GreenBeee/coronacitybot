package com.lnu.coronacitybot.service;

import com.lnu.coronacitybot.messages.i18n.UserLocale;
import com.lnu.coronacitybot.model.CountryStatistic;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CovidStatisticService {

	CountryStatistic getCountryStatisticFromResource(UserLocale userLocale, String country);

	List<CountryStatistic> getAllStatistic(UserLocale userLocale);

	List<CountryStatistic> getTopStatistic(UserLocale userLocale, int count);

	void updateAllCountriesStatistic();

	Map<UserLocale, Map<String, CountryStatistic>> updateStatistic();

	Set<String> getUACountries();
}
