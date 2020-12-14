package com.lnu.coronacitybot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryZoneStatistic {

	private String countryNameEn;

	private String countryNameUa;

	private CountryZone countryZone;

}
