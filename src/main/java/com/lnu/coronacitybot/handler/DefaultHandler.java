package com.lnu.coronacitybot.handler;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.Postbacks;
import com.lnu.coronacitybot.entity.enums.State;
import com.lnu.coronacitybot.messages.MessageKey;
import com.lnu.coronacitybot.messages.MessagesHolder;
import com.lnu.coronacitybot.model.outgoing.quickreply.QuickReply;
import com.lnu.coronacitybot.service.MessagesSender;
import com.lnu.coronacitybot.service.UserService;
import com.lnu.coronacitybot.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DefaultHandler {
    private final MessagesSender messagesSender;
    private final MessagesHolder messagesHolder;
    private final UserService userService;

    public void handleDefaultMessage(User user) {
        messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.DEFAULT));
        handleMenu(user);
    }

    public void handleWelcomeMessage(User user) {
        messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.WELCOME));
        Util.sleep(1);
        messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.WELCOME2));
        Util.sleep(2);
        messagesSender.sendSimpleMessage(user, messagesHolder.getMessage(MessageKey.WELCOME3));
        Util.sleep(2);
        handleMenu(user);
    }

    public void handleMenu(User user) {
        messagesSender.sendQuickRepliesMessage(user,
                Arrays.asList(user.getIsSubscribed() ?
                                QuickReply.createTextQuickReply("Unsubscribe", Postbacks.UNSUBSCRIBE) :
                                QuickReply.createTextQuickReply("Subscribe", Postbacks.SUBSCRIBE),
                        QuickReply.createTextQuickReply("Statistics", Postbacks.STATISTICS)
                ), messagesHolder.getMessage(MessageKey.MENU));
        userService.setState(user, State.MENU);
        userService.save(user);
    }
}
