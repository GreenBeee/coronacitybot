package com.lnu.coronacitybot.entity.enums;

import lombok.Getter;

@Getter
public enum SubscriptionType {
	TOP("button.top.country"),
	COUNTRY("button.my.country"),
	FULL("button.full.statistics"),
	ZONE_COUNTRY("button.zone.country");

	private final String message;

	SubscriptionType(String s) {
		this.message = s;
	}
}
