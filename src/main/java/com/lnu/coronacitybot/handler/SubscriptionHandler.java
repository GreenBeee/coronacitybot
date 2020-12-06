package com.lnu.coronacitybot.handler;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.State;
import com.lnu.coronacitybot.entity.enums.SubscriptionRate;
import com.lnu.coronacitybot.entity.enums.SubscriptionType;
import com.lnu.coronacitybot.messages.MessageKey;
import com.lnu.coronacitybot.messages.i18n.MessagesHolder;
import com.lnu.coronacitybot.messages.i18n.UserLocale;
import com.lnu.coronacitybot.model.outgoing.quickreply.QuickReply;
import com.lnu.coronacitybot.service.CovidStatisticService;
import com.lnu.coronacitybot.service.MessagesSender;
import com.lnu.coronacitybot.service.SubscriptionService;
import com.lnu.coronacitybot.service.UserService;
import com.lnu.coronacitybot.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionHandler {
	private final MessagesSender messagesSender;
	private final MessagesHolder messagesHolder;
	private final DefaultHandler defaultHandler;
	private final UserService userService;
	private final CovidStatisticService covidStatisticService;

	public void handleSubscribe(User user) {
		if (user.getIsSubscribed()) {
			messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.ALREADY_SUBSCRIBED, UserLocale.getLocale(user.getLocale())));
			Util.sleep(1);
			defaultHandler.handleMenu(user);
			return;
		}
		messagesSender.sendQuickRepliesMessage(user, getSubscriptionRateQuickReplies(user),
				messagesHolder.getMessage(MessageKey.SUBSCRIPTION, UserLocale.getLocale(user.getLocale())));
		userService.setState(user, State.SUBSCRIPTION_RATE);
		userService.save(user);
	}

	public void handleUnsubscribe(User user) {
		if (!user.getIsSubscribed()) {
			messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.ALREADY_UNSUBSCRIBED, UserLocale.getLocale(user.getLocale())));
			Util.sleep(1);
			defaultHandler.handleMenu(user);
			return;
		}
		user.setIsSubscribed(Boolean.FALSE);
		messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.UNSUBSCRIBE, UserLocale.getLocale(user.getLocale())));
		Util.sleep(1);
		defaultHandler.handleMenu(user);
	}

	public void handleSubscriptionRate(SubscriptionRate postback, User user) {
		user.setSubscriptionRate(postback);
		userService.save(user);
		messagesSender.sendQuickRepliesMessage(user, getSubscriptionTypeQuickReplies(user),
				messagesHolder.getMessage(MessageKey.SUCCESS_SUBSCRIPTION_RATE, UserLocale.getLocale(user.getLocale())));
		userService.setState(user, State.SUBSCRIPTION_TYPE);
		userService.save(user);
	}

	public void handleSubscriptionType(SubscriptionType postback, User user) {
		user.setSubscriptionType(postback);

		if (postback == SubscriptionType.TOP) {
			user.setIsSubscribed(true);
			user.setSubscriptionCycle(user.getSubscriptionRate().ordinal() + 1L);
			messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.SUCCESS_SUBSCRIPTION_TYPE, UserLocale.getLocale(user.getLocale())));
			Util.sleep(1);
			defaultHandler.handleMenu(user);
		} else {
			messagesSender.sendSimpleMessage(user,
					messagesHolder.getMessage(MessageKey.NEED_LOCATION, UserLocale.getLocale(user.getLocale())));
			user.setState(State.LOCATION);
			userService.save(user);
		}
	}

	public void handleLocationCountry(String country, User user) {
		if (UserLocale.getLocale(user.getLocale()) == UserLocale.UK_UA ?
				covidStatisticService.getUACountries().contains(country.toLowerCase())
				: Util.ALL_COUNTRIES.contains(country.toLowerCase())) {
			user.setSubscriptionCountry(country);
			user.setIsSubscribed(true);
			user.setSubscriptionCycle(user.getSubscriptionRate().ordinal() + 1L);
			messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.SUCCESS_SUBSCRIPTION_TYPE, UserLocale.getLocale(user.getLocale())));
			defaultHandler.handleMenu(user);
		} else {
			messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.WRONG_LOCATION, UserLocale.getLocale(user.getLocale())));
			Util.sleep(1);
			messagesSender.sendSimpleMessage(user,
					messagesHolder.getMessage(MessageKey.NEED_LOCATION, UserLocale.getLocale(user.getLocale())));
			user.setState(State.LOCATION);
			userService.save(user);
		}
	}


	private List<QuickReply> getSubscriptionRateQuickReplies(User user) {
		return Arrays.stream(SubscriptionRate.values())
				.map(x -> QuickReply.createTextQuickReply(messagesHolder.getMessage(x.getMessage(), UserLocale.getLocale(user.getLocale())), x.name()))
				.collect(Collectors.toList());
	}

	public List<QuickReply> getSubscriptionTypeQuickReplies(User user) {
		return Arrays.stream(SubscriptionType.values())
				.map(x -> QuickReply.createTextQuickReply(messagesHolder.getMessage(x.getMessage(), UserLocale.getLocale(user.getLocale())), x.name()))
				.collect(Collectors.toList());
	}
}
