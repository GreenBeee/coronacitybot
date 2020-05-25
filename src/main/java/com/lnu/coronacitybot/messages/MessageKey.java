package com.lnu.coronacitybot.messages;

public enum MessageKey {
	//@formatter:off
	WELCOME("message.welcome"),
	WELCOME2("message.welcome2"),
	WELCOME3("message.welcome3"),
	MENU("message.menu"),
	ALREADY_SUBSCRIBED("message.already.subscribed"),
	ALREADY_UNSUBSCRIBED("message.already.unsubscribed"),
	SUBSCRIPTION("message.subscription"),
	UNSUBSCRIBE("message.unsubscribe"),
	SUCCESS_SUBSCRIPTION_RATE("message.subscription.rate"),
	SUCCESS_SUBSCRIPTION_TYPE("message.subscription.type"),
	NEED_LOCATION("message.location"),
	WRONG_LOCATION("message.wrong.location"),
	HAVE_A_STATISTIC("message.here.statistic"),
	HAVE_A_COUNTRY_STATISTIC("message.here.country.statistic"),
	DEFAULT("message.default"),
	;
	//@formatter:on
	private final String value;

	public String value() {
		return value;
	}

	private MessageKey(String value) {
		this.value = value;
	}
}
