package com.lnu.coronacitybot.containers;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.Postbacks;
import com.lnu.coronacitybot.entity.enums.State;
import com.lnu.coronacitybot.entity.enums.SubscriptionRate;
import com.lnu.coronacitybot.entity.enums.SubscriptionType;
import com.lnu.coronacitybot.handler.DefaultHandler;
import com.lnu.coronacitybot.handler.StatisticHandler;
import com.lnu.coronacitybot.handler.SubscriptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostbackContainer {
	private final DefaultHandler defaultHandler;
	private final SubscriptionHandler subscriptionHandler;
	private final StatisticHandler statisticHandler;

	public void processPostback(String postback, User user) {
		switch (postback) {
			case Postbacks.INIT: {
				defaultHandler.handleWelcomeMessage(user);
				break;
			}
			case Postbacks.SUBSCRIBE: {
				subscriptionHandler.handleSubscribe(user);
				break;
			}
			case Postbacks.UNSUBSCRIBE: {
				subscriptionHandler.handleUnsubscribe(user);
				break;
			}
			case Postbacks.STATISTICS: {
				statisticHandler.handleStatistic(user);
				break;
			}
			default: {
				Optional<Enum<SubscriptionRate>> subscriptionRateFromPostback = getSubscriptionEnumFromPostback(SubscriptionRate.class, postback);
				Optional<Enum<SubscriptionType>> subscriptionTypeFromPostback = getSubscriptionEnumFromPostback(SubscriptionType.class, postback);
				if (subscriptionRateFromPostback.isPresent()) {
					subscriptionHandler.handleSubscriptionRate((SubscriptionRate) subscriptionRateFromPostback.get(), user);
				} else if (subscriptionTypeFromPostback.isPresent()) {
					if (State.SUBSCRIPTION_TYPE.equals(user.getState()))
						subscriptionHandler.handleSubscriptionType((SubscriptionType) subscriptionTypeFromPostback.get(), user);
					else if (State.SUBSCRIPTION_TYPE_FOR_STATISTIC.equals(user.getState()))
						statisticHandler.handleTypeStatistic((SubscriptionType) subscriptionTypeFromPostback.get(), user);
				} else {
					defaultHandler.handleDefaultMessage(user);
				}
			}
		}
	}

	private <T extends Enum<T>> Optional<Enum<T>> getSubscriptionEnumFromPostback(Class<T> enumType, String postback) {
		try {
			return Optional.of(T.valueOf(enumType, postback));
		} catch (IllegalArgumentException e) {
			return Optional.empty();
		}
	}

}
