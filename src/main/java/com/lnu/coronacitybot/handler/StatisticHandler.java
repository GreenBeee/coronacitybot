package com.lnu.coronacitybot.handler;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.State;
import com.lnu.coronacitybot.entity.enums.SubscriptionType;
import com.lnu.coronacitybot.messages.MessageKey;
import com.lnu.coronacitybot.messages.i18n.MessagesHolder;
import com.lnu.coronacitybot.messages.i18n.UserLocale;
import com.lnu.coronacitybot.service.CovidStatisticService;
import com.lnu.coronacitybot.service.StatisticSenderService;
import com.lnu.coronacitybot.service.MessagesSender;
import com.lnu.coronacitybot.service.UserService;
import com.lnu.coronacitybot.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatisticHandler {
	private final MessagesSender messagesSender;
	private final MessagesHolder messagesHolder;
	private final DefaultHandler defaultHandler;
	private final SubscriptionHandler subscriptionHandler;
	private final UserService userService;
	private final StatisticSenderService statisticSenderService;
	private final CovidStatisticService covidStatisticService;

	public void handleStatistic(User user) {
		messagesSender.sendQuickRepliesMessage(user, subscriptionHandler.getSubscriptionTypeQuickReplies(user),
				messagesHolder.getMessage(MessageKey.SUCCESS_SUBSCRIPTION_RATE, UserLocale.getLocale(user.getLocale())));
		userService.setState(user, State.SUBSCRIPTION_TYPE_FOR_STATISTIC);
		userService.save(user);
	}

	public void handleTypeStatistic(SubscriptionType statisticType, User user) {
		if (statisticType != SubscriptionType.TOP) {
			messagesSender.sendSimpleMessage(user,
					messagesHolder.getMessage(MessageKey.NEED_LOCATION, UserLocale.getLocale(user.getLocale())));
			user.setState(State.LOCATION_FOR_STATISTIC + "?statistic=" + statisticType.name().toLowerCase());
			userService.save(user);
		} else {
			statisticSenderService.sendUserTopStatistic(user);
			defaultHandler.handleMenu(user);
		}
	}

	public void handleLocationStatistic(String country, User user) {
		try {
			if (UserLocale.getLocale(user.getLocale()) == UserLocale.UK_UA) {
				if (covidStatisticService.getUACountries().contains(country.toLowerCase())) {
					SubscriptionType subscriptionType = SubscriptionType.valueOf(user.getState().split("=")[1].toUpperCase());
					statisticSenderService.sendUserStatisticWithSubscriptionType(user, subscriptionType, country);
				} else {
					Util.sleep(1);
					messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.WRONG_LOCATION, UserLocale.getLocale(user.getLocale())));
				}
			} else {
				if (Util.ALL_COUNTRIES.contains(country.toLowerCase())) {
					SubscriptionType subscriptionType = SubscriptionType.valueOf(user.getState().split("=")[1].toUpperCase());
					statisticSenderService.sendUserStatisticWithSubscriptionType(user, subscriptionType, country);
				} else {
					Util.sleep(1);
					messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.WRONG_LOCATION, UserLocale.getLocale(user.getLocale())));
				}
			}
		} catch (Exception e) {
			defaultHandler.handleDefaultMessage(user);
			System.out.println("Problems with statistic to user: " + user.getChatId());
			e.printStackTrace();
		}
	}

}
