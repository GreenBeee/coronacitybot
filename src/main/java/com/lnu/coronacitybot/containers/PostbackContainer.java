package com.lnu.coronacitybot.containers;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.Postbacks;
import com.lnu.coronacitybot.handler.DefaultHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostbackContainer {
	private final DefaultHandler defaultHandler;

	public void processPostback(String postback, User user) {
		switch (postback) {
			case Postbacks.INIT: {
				defaultHandler.handleWelcomeMessage(user);
				break;
			}
			case Postbacks.SUBSCRIBE: {

			}
			default: {
				defaultHandler.handleDefaultMessage(user);
			}
		}
	}

}
