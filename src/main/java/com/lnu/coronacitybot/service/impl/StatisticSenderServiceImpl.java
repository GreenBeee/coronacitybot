package com.lnu.coronacitybot.service.impl;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.SubscriptionType;
import com.lnu.coronacitybot.handler.DefaultHandler;
import com.lnu.coronacitybot.messages.MessageKey;
import com.lnu.coronacitybot.messages.i18n.MessagesHolder;
import com.lnu.coronacitybot.messages.i18n.UserLocale;
import com.lnu.coronacitybot.model.CountryStatistic;
import com.lnu.coronacitybot.model.CountryZone;
import com.lnu.coronacitybot.model.CountryZoneStatistic;
import com.lnu.coronacitybot.service.CovidStatisticService;
import com.lnu.coronacitybot.service.MessagesSender;
import com.lnu.coronacitybot.service.StatisticSenderService;
import com.lnu.coronacitybot.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
		messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.HAVE_A_STATISTIC, UserLocale.getLocale(user.getLocale())));
		Util.sleep(2);
		List<CountryStatistic> topStatistic = covidStatisticService.getTopStatistic(UserLocale.getLocale(user.getLocale()), count);
		for (int i = 0; i < topStatistic.size(); i++) {
			messagesSender.sendSimpleMessage(user, (i + 1) + ". " + topStatistic.get(i).toString(UserLocale.getLocale(user.getLocale())));
			Util.sleep(2);
		}
	}

	@Override
	public void sendUserCountryStatistic(User user, String country) {
		messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.HAVE_A_COUNTRY_STATISTIC, UserLocale.getLocale(user.getLocale())));
		Util.sleep(2);
		CountryStatistic countryStatistic = covidStatisticService.getCountryStatisticFromResource(UserLocale.getLocale(user.getLocale()), country);
		messagesSender.sendSimpleMessage(user, countryStatistic.toString(UserLocale.getLocale(user.getLocale())));
		Util.sleep(2);
	}

	@Override
	public void sendUserFullStatistic(User user, String country) {
		sendUserTopStatistic(user);
		sendUserCountryStatistic(user, country);
	}

	@Override
	public void sendUserZoneStatistic(User user, String country) {
		Optional<CountryZoneStatistic> countryZoneStatisticOptional = covidStatisticService.getCountryZoneStatistic(country);
		if (countryZoneStatisticOptional.isPresent()) {
			CountryZoneStatistic countryZoneStatistic = countryZoneStatisticOptional.get();
			Map<String, String> params = new HashMap<>();
			params.put("{country}", UserLocale.getLocale(user.getLocale()) == UserLocale.EN_US ? countryZoneStatistic.getCountryNameEn() : countryZoneStatistic.getCountryNameUa());
			if (CountryZone.RED == countryZoneStatistic.getCountryZone()) {
				messagesSender.sendSimpleMessage(user, messagesHolder.getTemplateMessage(MessageKey.ZONE_COUNTRY_RED, UserLocale.getLocale(user.getLocale()), params));
			} else {
				messagesSender.sendSimpleMessage(user, messagesHolder.getTemplateMessage(MessageKey.ZONE_COUNTRY_GREEN, UserLocale.getLocale(user.getLocale()), params));
			}
		} else {
			messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.WRONG_LOCATION, UserLocale.getLocale(user.getLocale())));
		}
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
			case ZONE_COUNTRY: {
				sendUserZoneStatistic(user, country);
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
