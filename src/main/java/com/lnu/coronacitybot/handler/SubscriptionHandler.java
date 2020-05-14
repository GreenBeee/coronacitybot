package com.lnu.coronacitybot.handler;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.Postbacks;
import com.lnu.coronacitybot.messages.MessageKey;
import com.lnu.coronacitybot.messages.MessagesHolder;
import com.lnu.coronacitybot.model.outgoing.quickreply.QuickReply;
import com.lnu.coronacitybot.service.MessagesSender;
import com.lnu.coronacitybot.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class SubscriptionHandler {
	private final MessagesSender messagesSender;
	private final MessagesHolder messagesHolder;
	private final DefaultHandler defaultHandler;

	public void handleSubscribe(User user) {


	}

}
