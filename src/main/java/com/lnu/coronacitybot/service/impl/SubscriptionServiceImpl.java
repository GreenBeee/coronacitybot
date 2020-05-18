package com.lnu.coronacitybot.service.impl;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.SubscriptionRate;
import com.lnu.coronacitybot.service.CovidStatisticService;
import com.lnu.coronacitybot.service.StatisticSenderService;
import com.lnu.coronacitybot.service.SubscriptionService;
import com.lnu.coronacitybot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
	private final StatisticSenderService statisticSenderService;
	private final UserService userService;
	private final CovidStatisticService covidStatisticService;

	//	@Scheduled(cron = "0 0 */4 * * ?")
	@Scheduled(cron = "0 */1 * * * ?")
	public void scheduledSubscription() {
		updateStatistic();
		Set<User> allUsersWithActiveSubscription = userService.getAllUsersWithActiveSubscription();
		for (User entry : allUsersWithActiveSubscription) {
			if (entry.getSubscriptionRate() == SubscriptionRate.S4H
					|| (entry.getSubscriptionRate() == SubscriptionRate.S8H && entry.getSubscriptionCycle() % 2 == 0)
					|| (entry.getSubscriptionRate() == SubscriptionRate.S12H && entry.getSubscriptionCycle() % 3 == 0)
					|| (entry.getSubscriptionRate() == SubscriptionRate.S24H && entry.getSubscriptionCycle() % 6 == 0)) {
				statisticSenderService.sendUserStatisticWithSubscriptionType(entry,
						entry.getSubscriptionType(),
						entry.getSubscriptionCountry());
			}

			Long value = entry.getSubscriptionCycle();
			entry.setSubscriptionCycle(++value);
		}

		userService.saveAll(allUsersWithActiveSubscription);
	}

	private void updateStatistic() {
		try {
			covidStatisticService.updateAllCountriesStatistic();
		} catch (Exception e) {
			System.out.println("Error with updating countries");
			e.printStackTrace();
		}
	}

}
