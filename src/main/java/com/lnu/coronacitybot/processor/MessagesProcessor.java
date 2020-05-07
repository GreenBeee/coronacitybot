package com.lnu.coronacitybot.processor;

import com.lnu.coronacitybot.model.incomming.MessageReceived;
import org.springframework.scheduling.annotation.Async;

public interface MessagesProcessor {
	@Async
	void processMessage(MessageReceived message);
}
