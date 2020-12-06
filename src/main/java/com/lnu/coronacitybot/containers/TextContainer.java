package com.lnu.coronacitybot.containers;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.State;
import com.lnu.coronacitybot.handler.DefaultHandler;
import com.lnu.coronacitybot.handler.LanguageHandler;
import com.lnu.coronacitybot.handler.StatisticHandler;
import com.lnu.coronacitybot.handler.SubscriptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TextContainer {
    private final DefaultHandler defaultHandler;
    private final SubscriptionHandler subscriptionHandler;
    private final StatisticHandler statisticHandler;
    private final LanguageHandler languageHandler;

    public void processText(String text, User user) {
        switch (user.getState().split("\\?")[0]){
            case State.INITIAL: {
                defaultHandler.handleWelcomeMessage(user);
                break;
            }
            case State.MENU: {
                //TODO: add here handling text input in menu
                defaultHandler.handleDefaultMessage(user);
                break;
            }
            case State.LOCATION: {
                subscriptionHandler.handleLocationCountry(text, user);
                break;
            }
            case State.LOCATION_FOR_STATISTIC: {
                statisticHandler.handleLocationStatistic(text, user);
                break;
            }
            case State.LANGUAGE: {
                languageHandler.handleTextChangeLang(text, user);
                break;
            }
            default: {
                defaultHandler.handleDefaultMessage(user);
            }
        }
    }
}
