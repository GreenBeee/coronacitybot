package com.lnu.coronacitybot.messages;

public enum MessageKey {
	//@formatter:off
	WELCOME("message.welcome"),
	WELCOME2("message.welcome2"),
	WELCOME3("message.welcome3"),
	MENU("message.menu"),
	DEFAULT("message.default"),
	;
	//@formatter:on
	private String value;

	public String value() {
		return value;
	}

	private MessageKey(String value) {
		this.value = value;
	}
}
