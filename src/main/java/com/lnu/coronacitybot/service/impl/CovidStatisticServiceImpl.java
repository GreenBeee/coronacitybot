package com.lnu.coronacitybot.service.impl;

import com.lnu.coronacitybot.model.CountryStatistic;
import com.lnu.coronacitybot.service.CovidStatisticService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CovidStatisticServiceImpl implements CovidStatisticService {
	@Value("${wiki.covid.url}")
	private String wikiPageUrl;

	private Map<String, CountryStatistic> allCountries;

	@PostConstruct
	public void setAllCountries() {
		this.allCountries = updateStatistic();
	}

	@Override
	public CountryStatistic getCountryStatisticFromResource(String country) {
		return allCountries.get(country.toLowerCase());
	}

	@Override
	public List<CountryStatistic> getAllStatistic() {
		return new ArrayList<>(allCountries.values());
	}

	@Override
	public List<CountryStatistic> getTopStatistic(int count) {
		return allCountries.values().stream()
				.sorted((o1, o2) -> Integer.parseInt(o2.getCases().replace(",", "")) -
						Integer.parseInt(o1.getCases().replace(",", "")))
				.limit(count)
				.collect(Collectors.toList());
	}

	@Override
	public void updateAllCountriesStatistic() {
		this.allCountries = updateStatistic();
	}

	@Override
	@SneakyThrows
	public Map<String, CountryStatistic> updateStatistic() {
		Document doc = Jsoup.connect(wikiPageUrl).get();
		Elements covidDiv = doc.select("#covid19-container");
		Elements rows = covidDiv.select("table tbody tr");
		rows.remove(rows.size() - 1);
		rows.remove(rows.size() - 1);
		return rows.stream().skip(2).map(x -> {
			Elements column = x.select("td");
			return new CountryStatistic(x.getElementsByIndexEquals(1).select("a").get(0).text().toLowerCase(),
					column.get(0).text().toLowerCase(), column.get(1).text().toLowerCase(), column.get(2).text().toLowerCase(),
					null);
		}).collect(Collectors.toMap(CountryStatistic::getCountryName, x -> x));
	}
}
