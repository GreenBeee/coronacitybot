package com.lnu.coronacitybot.service.impl;

import com.lnu.coronacitybot.messages.i18n.UserLocale;
import com.lnu.coronacitybot.model.CountryStatistic;
import com.lnu.coronacitybot.model.CountryZone;
import com.lnu.coronacitybot.model.CountryZoneStatistic;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CovidStatisticServiceImpl implements CovidStatisticService {
	@Value("${wiki.covid.url}")
	private String wikiPageUrl;
	@Value("${wiki.covid.zone.url}")
	private String wikiZonePageUrl;

	private Map<UserLocale, Map<String, CountryStatistic>> allCountries = new HashMap<>();

	private List<CountryZoneStatistic> allCountriesZone = new ArrayList<>();

	private Map<String, String> UA_COUNTRIES = new HashMap<>();

	@PostConstruct
	public void setAllCountries() {
		UA_COUNTRIES.put("afghanistan", "афганістан");
		UA_COUNTRIES.put("albania", "албанія");
		UA_COUNTRIES.put("algeria", "алжир");
		UA_COUNTRIES.put("andorra", "андорра");
		UA_COUNTRIES.put("argentina", "аргентина");
		UA_COUNTRIES.put("armenia", "вірменія");
		UA_COUNTRIES.put("australia", "австралія");
		UA_COUNTRIES.put("austria", "австрія");
		UA_COUNTRIES.put("azerbaijan", "азербайджан");
		UA_COUNTRIES.put("bahamas", "багами");
		UA_COUNTRIES.put("belarus", "білорусь");
		UA_COUNTRIES.put("belgium", "бельгія");
		UA_COUNTRIES.put("benin", "бенін");
		UA_COUNTRIES.put("brazil", "бразилія");
		UA_COUNTRIES.put("bulgaria", "болгарія");
		UA_COUNTRIES.put("cambodia", "камбоджа");
		UA_COUNTRIES.put("cameroon", "камерун");
		UA_COUNTRIES.put("canada", "канада");
		UA_COUNTRIES.put("cayman islands", "кайманові острови");
		UA_COUNTRIES.put("central african republic", "центральна африканська республіка");
		UA_COUNTRIES.put("chad", "чад");
		UA_COUNTRIES.put("chile", "чилі");
		UA_COUNTRIES.put("china", "китай");
		UA_COUNTRIES.put("colombia", "колумбія");
		UA_COUNTRIES.put("croatia", "хорватія");
		UA_COUNTRIES.put("cyprus", "кіпр");
		UA_COUNTRIES.put("czech republic", "чеська республіка");
		UA_COUNTRIES.put("denmark", "данія");
		UA_COUNTRIES.put("ecuador", "еквадор");
		UA_COUNTRIES.put("egypt", "єгипет");
		UA_COUNTRIES.put("england", "англія");
		UA_COUNTRIES.put("espana", "іспанія");
		UA_COUNTRIES.put("estonia", "естонія");
		UA_COUNTRIES.put("ethiopia", "ефіопія");
		UA_COUNTRIES.put("france", "франція");
		UA_COUNTRIES.put("georgia", "грузія");
		UA_COUNTRIES.put("germany", "німеччина");
		UA_COUNTRIES.put("ghana", "ґана");
		UA_COUNTRIES.put("gibraltar", "ґібралтар");
		UA_COUNTRIES.put("great britain", "велика британія");
		UA_COUNTRIES.put("greece", "греція");
		UA_COUNTRIES.put("honduras", "гондурас");
		UA_COUNTRIES.put("hong kong", "гон-конґ");
		UA_COUNTRIES.put("hungary", "угорщина");
		UA_COUNTRIES.put("iceland", "ісландія");
		UA_COUNTRIES.put("india", "індія");
		UA_COUNTRIES.put("indonesia", "індонезія");
		UA_COUNTRIES.put("iran", "іран");
		UA_COUNTRIES.put("iraq", "ірак");
		UA_COUNTRIES.put("ireland", "ірландія");
		UA_COUNTRIES.put("israel", "ізраїль");
		UA_COUNTRIES.put("italy", "італія");
		UA_COUNTRIES.put("jamaica", "ямайка");
		UA_COUNTRIES.put("japan", "японія");
		UA_COUNTRIES.put("jordan", "іорданія");
		UA_COUNTRIES.put("kazakhstan", "казахстан");
		UA_COUNTRIES.put("kenya", "кенія");
		UA_COUNTRIES.put("kiribati", "кірібаті");
		UA_COUNTRIES.put("korea, republic of", "корейська республіка");
		UA_COUNTRIES.put("korea (south)", "південна корея");
		UA_COUNTRIES.put("kuwait", "кувейт");
		UA_COUNTRIES.put("kyrgyzstan", "киргизстан");
		UA_COUNTRIES.put("latvia", "латвія");
		UA_COUNTRIES.put("lebanon", "ліван");
		UA_COUNTRIES.put("lesotho", "лесото");
		UA_COUNTRIES.put("lithuania", "литва");
		UA_COUNTRIES.put("luxembourg", "люксембург");
		UA_COUNTRIES.put("macedonia", "македонія");
		UA_COUNTRIES.put("malta", "мальта");
		UA_COUNTRIES.put("mexico", "мексика");
		UA_COUNTRIES.put("monaco", "монако");
		UA_COUNTRIES.put("mongolia", "монголія");
		UA_COUNTRIES.put("morocco", "марокко");
		UA_COUNTRIES.put("nepal", "непал");
		UA_COUNTRIES.put("netherlands", "нідерланди");
		UA_COUNTRIES.put("new zealand", "нова зеландія");
		UA_COUNTRIES.put("nicaragua", "нікарагуа");
		UA_COUNTRIES.put("nigeria", "нігерія");
		UA_COUNTRIES.put("northern ireland", "північна ірландія");
		UA_COUNTRIES.put("norway", "норвегія");
		UA_COUNTRIES.put("oman", "оман");
		UA_COUNTRIES.put("pakistan", "пакістан");
		UA_COUNTRIES.put("palau", "палау");
		UA_COUNTRIES.put("panama", "панама");
		UA_COUNTRIES.put("paraguay", "парагвай");
		UA_COUNTRIES.put("peru", "перу");
		UA_COUNTRIES.put("philippines", "філіппіни");
		UA_COUNTRIES.put("poland", "польща");
		UA_COUNTRIES.put("portugal", "португалія");
		UA_COUNTRIES.put("puerto rico", "пуерто ріко");
		UA_COUNTRIES.put("qatar", "катар");
		UA_COUNTRIES.put("romania", "румунія");
		UA_COUNTRIES.put("russia", "росія");
		UA_COUNTRIES.put("san marino", "сан маріно");
		UA_COUNTRIES.put("saudi arabia", "саудівська аравія");
		UA_COUNTRIES.put("scotland", "шотландія");
		UA_COUNTRIES.put("singapore", "сингапур");
		UA_COUNTRIES.put("slovakia", "словаччина");
		UA_COUNTRIES.put("slovenia", "словенія");
		UA_COUNTRIES.put("somalia", "сомалі");
		UA_COUNTRIES.put("spain", "іспанія");
		UA_COUNTRIES.put("sweden", "швеція");
		UA_COUNTRIES.put("switzerland", "швейцарія");
		UA_COUNTRIES.put("taiwan", "тайвань");
		UA_COUNTRIES.put("tajikistan", "таджикістан");
		UA_COUNTRIES.put("thailand", "таїланд");
		UA_COUNTRIES.put("tunisia", "туніс");
		UA_COUNTRIES.put("turkey", "туреччина");
		UA_COUNTRIES.put("turkmenistan", "туркменістан");
		UA_COUNTRIES.put("ukraine", "україна");
		UA_COUNTRIES.put("united kingdom", "об'єднане королівство");
		UA_COUNTRIES.put("united states", "сполучені штати америки");
		UA_COUNTRIES.put("uruguay", "уругвай");
		UA_COUNTRIES.put("usa", "сша");
		UA_COUNTRIES.put("uzbekistan", "узбекістан");
		UA_COUNTRIES.put("venezuela", "венесуела");
		UA_COUNTRIES.put("viet nam", "в'єтнам");
		this.allCountries = updateStatistic();
		this.allCountriesZone = updateAllCountriesZoneStatistic();
	}

	@Override
	public CountryStatistic getCountryStatisticFromResource(UserLocale userLocale, String country) {
		return allCountries.get(userLocale).get(country.toLowerCase());
	}

	@Override
	public List<CountryStatistic> getAllStatistic(UserLocale userLocale) {
		return new ArrayList<>(allCountries.get(userLocale).values());
	}

	@Override
	public List<CountryStatistic> getTopStatistic(UserLocale userLocale, int count) {
		return allCountries.get(userLocale).values().stream()
				.sorted(new CountryStatisticComparator())
				.limit(count)
				.collect(Collectors.toList());
	}

	@Override
	public void updateAllCountriesStatistic() {
		this.allCountries = updateStatistic();
		this.allCountriesZone = updateAllCountriesZoneStatistic();
	}

	@SneakyThrows
	@Override
	public List<CountryZoneStatistic> updateAllCountriesZoneStatistic() {
		Document doc = Jsoup.connect(wikiZonePageUrl).get();
		Elements table = doc.select("#tablepress-11");
		return table.select("tbody tr").stream().map(x -> {
			Elements column = x.select("td");
			String html = column.get(0).html();
			String color = html.substring(html.indexOf("color"), html.indexOf("color") + 15).split(":")[1].trim();
			return new CountryZoneStatistic(column.get(1).text().toLowerCase(),
					column.get(0).text().toLowerCase(),
					"#ff0000;".equalsIgnoreCase(color) ? CountryZone.RED : CountryZone.GREEN);
		}).collect(Collectors.toList());
	}

	@Override
	public Optional<CountryZoneStatistic> getCountryZoneStatistic(String country) {
		return this.allCountriesZone.stream().filter(x -> country.equalsIgnoreCase(x.getCountryNameEn())
				|| country.equalsIgnoreCase(x.getCountryNameUa())).findFirst();
	}

	@Override
	@SneakyThrows
	public Map<UserLocale, Map<String, CountryStatistic>> updateStatistic() {
		Map<UserLocale, Map<String, CountryStatistic>> resultMap = new HashMap<>();
		Document doc = Jsoup.connect(wikiPageUrl).get();
		Elements covidDiv = doc.select("#covid19-container");
		Elements rows = covidDiv.select("table tbody tr");
		rows.remove(rows.size() - 1);
		rows.remove(rows.size() - 1);
		Map<String, CountryStatistic> enCountries = rows.stream().skip(2).map(x -> {
			Elements column = x.select("td");
			return new CountryStatistic(x.getElementsByIndexEquals(1).select("a").get(0).text().toLowerCase(),
					column.get(0).text().toLowerCase(), column.get(1).text().toLowerCase(), column.get(2).text().toLowerCase(),
					null);
		}).collect(Collectors.toMap(CountryStatistic::getCountryName, x -> x));
		resultMap.put(UserLocale.EN_US, enCountries);

		Map<String, CountryStatistic> ukCountries = enCountries.entrySet().stream()
				.filter(x -> findLocale(x.getKey(), Locale.forLanguageTag("en-US")).isPresent())
				.filter(x -> UA_COUNTRIES.containsKey(findLocale(x.getKey(), Locale.forLanguageTag("en-US")).get().getDisplayCountry().toLowerCase()))
				.map(entry -> {
					Locale inLocale = Locale.forLanguageTag("en-US");
					Optional<Locale> first = findLocale(entry.getKey(), inLocale);
					String displayCountryUK = UA_COUNTRIES.get(first.get().getDisplayCountry().toLowerCase());
					return new CountryStatistic(displayCountryUK, entry.getValue().getCases(), entry.getValue().getDeath(), entry.getValue().getRecovered(), entry.getValue().getResource());
				})
				.collect(Collectors.toMap(CountryStatistic::getCountryName, x -> x));
		resultMap.put(UserLocale.UK_UA, ukCountries);
		return resultMap;
	}

	@Override
	public Set<String> getUACountries() {
		return new HashSet<>(UA_COUNTRIES.values());
	}

	private Optional<Locale> findLocale(String entry, Locale inLocale) {
		return Arrays.stream(Locale.getAvailableLocales()).filter(x -> x.getDisplayCountry(inLocale).equalsIgnoreCase(entry)).findFirst();
	}


	private class CountryStatisticComparator implements Comparator<CountryStatistic> {

		@Override
		public int compare(final CountryStatistic o1, final CountryStatistic o2) {
			final int cases1 = getCases(o1);
			final int cases2 = getCases(o2);
			return cases2 - cases1;
		}

		private int getCases(final CountryStatistic countryStatistic) {
			try {
				return Integer.parseInt(countryStatistic.getCases().replaceAll(",", ""));
			} catch (NumberFormatException e) {
				return -1;
			}
		}
	}
}
