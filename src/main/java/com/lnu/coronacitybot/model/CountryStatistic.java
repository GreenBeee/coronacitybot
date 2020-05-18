package com.lnu.coronacitybot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryStatistic {

	private String countryName;

	private String cases;

	private String death;

	private String recovered;

	private String resource;
}
