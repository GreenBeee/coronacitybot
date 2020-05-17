package com.lnu.coronacitybot.entity.enums;

import lombok.Getter;

@Getter
public enum SubscriptionRate {
	S4H("Every 4h"),
	S8H("Every 8h"),
	S12H("Every 12h"),
	S24H("Every 24h"),
	;

	private final String message;

	SubscriptionRate(String s) {
		this.message = s;
	}
}
