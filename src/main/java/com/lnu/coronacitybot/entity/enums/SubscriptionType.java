package com.lnu.coronacitybot.entity.enums;

import lombok.Getter;

@Getter
public enum SubscriptionType {
	TOP("Top countries"),
	COUNTRY("My country"),
	FULL("Full statistic");

	private final String message;

	SubscriptionType(String s) {
		this.message = s;
	}
}
