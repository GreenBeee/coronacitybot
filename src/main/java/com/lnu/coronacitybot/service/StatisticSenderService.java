package com.lnu.coronacitybot.service;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.SubscriptionType;

public interface StatisticSenderService {

	void sendUserTopStatistic(User user);

	void sendUserCountryStatistic(User user, String country);

	void sendUserFullStatistic(User user, String country);

	void sendUserZoneStatistic(User user, String country);

	void sendUserStatisticWithSubscriptionType(User user, SubscriptionType subscriptionType, String country);
}
