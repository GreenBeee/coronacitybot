package com.lnu.coronacitybot.model;

import com.lnu.coronacitybot.messages.i18n.UserLocale;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
@AllArgsConstructor
public class CountryStatistic {

	private String countryName;

	private String cases;

	private String death;

	private String recovered;

	private String resource;

	public String toString(UserLocale userLocale) {
		String country = countryName.substring(0, 1).toUpperCase() + countryName.substring(1)
				+ ": " + "total cases: " + cases + "; total deaths: " + death + "; total recovered: " + recovered + ".";
		if (userLocale == UserLocale.UK_UA)
			country = countryName.substring(0, 1).toUpperCase() + countryName.substring(1)
					+ ": " + "кількість випадків: " + cases + "; смертей: " + death + "; одужали: " + recovered + ".";
		return country + (StringUtils.isEmpty(resource) ? "" : " Resource: " + resource);
	}
}
