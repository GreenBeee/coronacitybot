package com.lnu.coronacitybot.service;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.model.Button;
import com.lnu.coronacitybot.model.outgoing.generic.ListTemplateMessageElement;
import com.lnu.coronacitybot.model.outgoing.generic.MessageElement;
import com.lnu.coronacitybot.model.outgoing.quickreply.QuickReply;

import java.util.List;

public interface MessagesSender {
	void sendSimpleMessage(User recipient, String text);

	void sendGenericMessages(User recipient, List<MessageElement> elements);

	void sendButtonsMessage(User recipient, List<Button> buttons, String text);

	void sendQuickRepliesMessage(User recipient, List<QuickReply> quickReplies, String text);

	void sendListTemplate(User recipient, List<ListTemplateMessageElement> elements);
}
