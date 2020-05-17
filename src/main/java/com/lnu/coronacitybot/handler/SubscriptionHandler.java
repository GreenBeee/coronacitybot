package com.lnu.coronacitybot.handler;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.State;
import com.lnu.coronacitybot.entity.enums.SubscriptionRate;
import com.lnu.coronacitybot.entity.enums.SubscriptionType;
import com.lnu.coronacitybot.messages.MessageKey;
import com.lnu.coronacitybot.messages.MessagesHolder;
import com.lnu.coronacitybot.model.outgoing.GeoCoordinates;
import com.lnu.coronacitybot.model.outgoing.quickreply.QuickReply;
import com.lnu.coronacitybot.service.MessagesSender;
import com.lnu.coronacitybot.service.SubscriptionService;
import com.lnu.coronacitybot.service.UserService;
import com.lnu.coronacitybot.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionHandler {
	private final MessagesSender messagesSender;
	private final MessagesHolder messagesHolder;
	private final DefaultHandler defaultHandler;
	private final UserService userService;
	private final SubscriptionService subscriptionService;

	public void handleSubscribe(User user) {
		if (user.getIsSubscribed()) {
			messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.ALREADY_SUBSCRIBED));
			Util.sleep(1);
			defaultHandler.handleMenu(user);
			return;
		}
		messagesSender.sendQuickRepliesMessage(user, getSubscriptionRateQuickReplies(),
				messagesHolder.getMessage(MessageKey.SUBSCRIPTION));
		userService.setState(user, State.SUBSCRIPTION_RATE);
		userService.save(user);
	}

	public void handleUnsubscribe(User user) {
		if (!user.getIsSubscribed()) {
			messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.ALREADY_UNSUBSCRIBED));
			Util.sleep(1);
			defaultHandler.handleMenu(user);
			return;
		}
		user.setIsSubscribed(Boolean.FALSE);
		messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.UNSUBSCRIBE));
		Util.sleep(1);
		defaultHandler.handleMenu(user);
	}

	public void handleSubscriptionRate(SubscriptionRate postback, User user) {
		user.setSubscriptionRate(postback);
		userService.save(user);
		messagesSender.sendQuickRepliesMessage(user, getSubscriptionTypeQuickReplies(),
				messagesHolder.getMessage(MessageKey.SUCCESS_SUBSCRIPTION_RATE));
		userService.setState(user, State.SUBSCRIPTION_TYPE);
		userService.save(user);
	}

	public void handleSubscriptionType(SubscriptionType postback, User user) {
		user.setSubscriptionType(postback);

		if (postback == SubscriptionType.TOP) {
			user.setIsSubscribed(true);
			user.setSubscriptionCycle(user.getSubscriptionRate().ordinal() + 1L);
			messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.SUCCESS_SUBSCRIPTION_TYPE));
			Util.sleep(1);
			defaultHandler.handleMenu(user);
		} else {
			messagesSender.sendSimpleMessage(user,
					messagesHolder.getMessage(MessageKey.NEED_LOCATION));
			user.setState(State.LOCATION);
			userService.save(user);
		}
	}

	public void handleLocationCountry(String country, User user) {
		if (Util.ALL_COUNTRIES.contains(country.toLowerCase())) {
			user.setSubscriptionCountry(country);
			user.setIsSubscribed(true);
			user.setSubscriptionCycle(user.getSubscriptionRate().ordinal() + 1L);
			messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.SUCCESS_SUBSCRIPTION_TYPE));
			defaultHandler.handleMenu(user);
		} else {
			messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.WRONG_LOCATION));
			Util.sleep(1);
			messagesSender.sendSimpleMessage(user,
					messagesHolder.getMessage(MessageKey.NEED_LOCATION));
			user.setState(State.LOCATION);
			userService.save(user);
		}
	}


	private List<QuickReply> getSubscriptionRateQuickReplies() {
		return Arrays.stream(SubscriptionRate.values())
				.map(x -> QuickReply.createTextQuickReply(x.getMessage(), x.name()))
				.collect(Collectors.toList());
	}

	public List<QuickReply> getSubscriptionTypeQuickReplies() {
		return Arrays.stream(SubscriptionType.values())
				.map(x -> QuickReply.createTextQuickReply(x.getMessage(), x.name()))
				.collect(Collectors.toList());
	}
}
