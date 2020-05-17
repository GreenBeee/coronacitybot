package com.lnu.coronacitybot.service.impl;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.SubscriptionType;
import com.lnu.coronacitybot.handler.DefaultHandler;
import com.lnu.coronacitybot.service.StatisticSenderService;
import com.lnu.coronacitybot.service.MessagesSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticSenderServiceImpl implements StatisticSenderService {
	private final MessagesSender messagesSender;
	private final DefaultHandler defaultHandler;

	@Override
	public void sendUserTopStatistic(User user) {
		defaultHandler.handleMenu(user);
	}

	@Override
	public void sendUserCountryStatistic(User user, String country) {
		defaultHandler.handleMenu(user);
	}

	@Override
	public void sendUserFullStatistic(User user, String country) {
		sendUserTopStatistic(user);
		sendUserCountryStatistic(user, country);
	}

	@Override
	public void sendUserStatisticWithSubscriptionType(User user, SubscriptionType subscriptionType, String country) {
		switch (subscriptionType) {
			case TOP: {
				sendUserTopStatistic(user);
				break;
			}
			case COUNTRY: {
				sendUserCountryStatistic(user, country);
				break;
			}
			case FULL: {
				sendUserFullStatistic(user, country);
				break;
			}
			default: {
				System.out.println("User: " + user.getChatId() + " has problems with subscription");
			}
		}
	}
}
