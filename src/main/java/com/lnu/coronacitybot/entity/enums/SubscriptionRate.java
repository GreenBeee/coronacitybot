package com.lnu.coronacitybot.entity.enums;

import lombok.Getter;

@Getter
public enum SubscriptionRate {
	S4H("button.rate.four"),
	S8H("button.rate.eight"),
	S12H("button.rate.twelve"),
	S24H("button.rate.day"),
	;

	private final String message;

	SubscriptionRate(String s) {
		this.message = s;
	}
}
