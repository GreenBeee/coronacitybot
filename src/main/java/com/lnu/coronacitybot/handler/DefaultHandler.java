package com.lnu.coronacitybot.handler;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.Postbacks;
import com.lnu.coronacitybot.entity.enums.State;
import com.lnu.coronacitybot.messages.MessageKey;
import com.lnu.coronacitybot.messages.i18n.MessagesHolder;
import com.lnu.coronacitybot.messages.i18n.UserLocale;
import com.lnu.coronacitybot.model.outgoing.quickreply.QuickReply;
import com.lnu.coronacitybot.service.MessagesSender;
import com.lnu.coronacitybot.service.UserService;
import com.lnu.coronacitybot.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DefaultHandler {
	private final MessagesSender messagesSender;
	private final MessagesHolder messagesHolder;
	private final UserService userService;

	public void handleDefaultMessage(User user) {
		messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.DEFAULT, UserLocale.getLocale(user.getLocale())));
		handleMenu(user);
	}

	public void handleWelcomeMessage(User user) {
		messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.WELCOME, UserLocale.getLocale(user.getLocale())));
		Util.sleep(1);
		messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.WELCOME2, UserLocale.getLocale(user.getLocale())));
		Util.sleep(2);
		messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.WELCOME3, UserLocale.getLocale(user.getLocale())));
		Util.sleep(2);
		handleMenu(user);
	}

	public void handleMenu(User user) {
		messagesSender.sendQuickRepliesMessage(user,
				Arrays.asList(user.getIsSubscribed() ?
								QuickReply.createTextQuickReply(
										messagesHolder.getMessage(MessageKey.BUTTON_UNSUBSCRIBE, UserLocale.getLocale(user.getLocale())),
										Postbacks.UNSUBSCRIBE) :
								QuickReply.createTextQuickReply(
										messagesHolder.getMessage(MessageKey.BUTTON_SUBSCRIBE, UserLocale.getLocale(user.getLocale())),
										Postbacks.SUBSCRIBE),
						QuickReply.createTextQuickReply(
								messagesHolder.getMessage(MessageKey.BUTTON_STATISTICS, UserLocale.getLocale(user.getLocale())),
								Postbacks.STATISTICS),
						QuickReply.createTextQuickReply(
								messagesHolder.getMessage(MessageKey.BUTTON_CHANGE_LANG, UserLocale.getLocale(user.getLocale())),
								Postbacks.CHANGE_LANGUAGE)
				), messagesHolder.getMessage(MessageKey.MENU, UserLocale.getLocale(user.getLocale())));
		userService.setState(user, State.MENU);
		userService.save(user);
	}
}
