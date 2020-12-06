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
	FROM_BEGINNING("from.beginning"),
	DEFAULT("message.default"),
	CHANGE_LANG_FINISHED("message.change.lang.finished"),
	CHANGE_LANG("message.change.lang"),
	CHANGE_LANG_WRONG("message.change.lang.wrong"),


	BUTTON_UNSUBSCRIBE("button.unsubscribe"),
	BUTTON_SUBSCRIBE("button.subscribe"),
	BUTTON_STATISTICS("button.statistics"),
	BUTTON_CHANGE_LANG("button.change.lang"),
	BUTTON_CHANGE_LANG_UK("button.change.lang.uk"),
	BUTTON_CHANGE_LANG_EN("button.change.lang.en"),

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
