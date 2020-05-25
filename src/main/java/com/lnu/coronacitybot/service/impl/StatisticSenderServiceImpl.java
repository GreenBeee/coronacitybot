package com.lnu.coronacitybot.service.impl;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.SubscriptionType;
import com.lnu.coronacitybot.handler.DefaultHandler;
import com.lnu.coronacitybot.messages.MessageKey;
import com.lnu.coronacitybot.messages.MessagesHolder;
import com.lnu.coronacitybot.model.CountryStatistic;
import com.lnu.coronacitybot.service.CovidStatisticService;
import com.lnu.coronacitybot.service.MessagesSender;
import com.lnu.coronacitybot.service.StatisticSenderService;
import com.lnu.coronacitybot.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticSenderServiceImpl implements StatisticSenderService {
	private final MessagesSender messagesSender;
	private final MessagesHolder messagesHolder;
	private final DefaultHandler defaultHandler;
	private final CovidStatisticService covidStatisticService;

	@Value("${country.statistic.top.count}")
	private Integer count;

	@Override
	public void sendUserTopStatistic(User user) {
		messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.HAVE_A_STATISTIC));
		Util.sleep(2);
		List<CountryStatistic> topStatistic = covidStatisticService.getTopStatistic(count);
		for (int i = 0; i < topStatistic.size(); i++) {
			messagesSender.sendSimpleMessage(user, (i + 1) + ". " + topStatistic.get(i).toString());
			Util.sleep(2);
		}
	}

	@Override
	public void sendUserCountryStatistic(User user, String country) {
		messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.HAVE_A_COUNTRY_STATISTIC));
		Util.sleep(2);
		CountryStatistic countryStatistic = covidStatisticService.getCountryStatisticFromResource(country);
		messagesSender.sendSimpleMessage(user,  countryStatistic.toString());
		Util.sleep(2);
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
				defaultHandler.handleMenu(user);
				break;
			}
			case COUNTRY: {
				sendUserCountryStatistic(user, country);
				defaultHandler.handleMenu(user);
				break;
			}
			case FULL: {
				sendUserFullStatistic(user, country);
				defaultHandler.handleMenu(user);
				break;
			}
			default: {
				System.out.println("User: " + user.getChatId() + " has problems with subscription");
				defaultHandler.handleDefaultMessage(user);
			}
		}
	}
}
