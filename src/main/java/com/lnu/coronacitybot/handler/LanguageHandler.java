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
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class LanguageHandler {
	private final MessagesSender messagesSender;
	private final MessagesHolder messagesHolder;
	private final UserService userService;
	private final DefaultHandler defaultHandler;

	public void changeLanguageSetup(User user) {
		UserLocale locale = UserLocale.getLocale(user.getLocale());
		messagesSender.sendQuickRepliesMessage(user, locale != UserLocale.UK_UA ?
						Collections.singletonList(QuickReply.createTextQuickReply(
								messagesHolder.getMessage(MessageKey.BUTTON_CHANGE_LANG_UK, locale),
								Postbacks.UK_LANG
						)) :
						Collections.singletonList(QuickReply.createTextQuickReply(
								messagesHolder.getMessage(MessageKey.BUTTON_CHANGE_LANG_EN, locale),
								Postbacks.EN_LANG
						)),
				messagesHolder.getMessage(MessageKey.CHANGE_LANG, locale));
		user.setState(State.LANGUAGE);
		userService.save(user);
	}

	public void changeLanguageUK(User user) {
		user.setLocale(UserLocale.UK_UA.toString());
		setLanguageToUser(user);
	}

	public void changeLanguageEN(User user) {
		user.setLocale(UserLocale.EN_US.toString());
		setLanguageToUser(user);
	}

	public void handleTextChangeLang(String lang, User user){
		if ("ukrainian".equalsIgnoreCase(lang) || "українська".equalsIgnoreCase(lang)){
			changeLanguageUK(user);
		} else if ("english".equalsIgnoreCase(lang) || "англійська".equalsIgnoreCase(lang)){
			changeLanguageEN(user);
		} else {
			messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.CHANGE_LANG_WRONG, UserLocale.getLocale(user.getLocale())));
			Util.sleep(1);
			changeLanguageSetup(user);
		}
	}

	private void setLanguageToUser(User user) {
		UserLocale locale = UserLocale.getLocale(user.getLocale());
		messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.CHANGE_LANG_FINISHED, locale));
		Util.sleep(2);
		defaultHandler.handleMenu(user);
	}

}
