package com.lnu.coronacitybot.processor.impl;

import com.lnu.coronacitybot.containers.LocationContainer;
import com.lnu.coronacitybot.containers.PostbackContainer;
import com.lnu.coronacitybot.containers.TextContainer;
import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.model.incomming.Account;
import com.lnu.coronacitybot.model.incomming.Attachment;
import com.lnu.coronacitybot.model.incomming.Coordinates;
import com.lnu.coronacitybot.model.incomming.MessageReceived;
import com.lnu.coronacitybot.model.incomming.Messaging;
import com.lnu.coronacitybot.model.incomming.Payload;
import com.lnu.coronacitybot.model.incomming.Referral;
import com.lnu.coronacitybot.model.outgoing.GeoCoordinates;
import com.lnu.coronacitybot.model.payment.Payment;
import com.lnu.coronacitybot.processor.MessagesProcessor;
import com.lnu.coronacitybot.service.MessagesSender;
import com.lnu.coronacitybot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessagesProcessorImpl implements MessagesProcessor {
	private static final Logger LOGGER = LogManager.getLogger(MessagesProcessorImpl.class);

	private final MessagesSender sendMessageService;
	private final UserService userService;

	private final PostbackContainer postbackContainer;
	private final TextContainer textContainer;
	private final LocationContainer locationContainer;

	@Override
	public void processMessage(MessageReceived message) {
		try {
			message.getEntry().forEach(entry -> {
				entry.getMessaging().forEach(messaging -> {
					User user = userService.createIfNotExist(messaging.getSender());
					Optional<String> quickReplyOpt = getQuickReply(messaging);
					Optional<String> textMessageOpt = getTextMessage(messaging);
					Optional<String> payloadOpt = getPayload(messaging);
					Optional<Coordinates> coordinatesOpt = getCoordinates(messaging);
					Optional<Referral> referral = getReferral(messaging);
					Optional<Account> account = getAccount(messaging);
					Optional<Payment> paymentOpt = getPayment(messaging);

					if (account.isPresent()) {
						if (account.get().getStatus().equals("linked")) {
							//save authorization code into db
						}
						if (account.get().getStatus().equals("unlinked")) {
							//delete user login and password from db
						}
					}

					if (referral.isPresent()) {
						//do something with referral
					}
					if (coordinatesOpt.isPresent()) {
						locationContainer.processLocation(new GeoCoordinates(coordinatesOpt.get().getLongitude(),
								coordinatesOpt.get().getLatitude()), user);
						return;
					}
					if (quickReplyOpt.isPresent()) {
						postbackContainer.processPostback(quickReplyOpt.get(), user);
						return;
					}
					if (textMessageOpt.isPresent()) {
						// do something with text
						textContainer.processText(textMessageOpt.get(), user);
						return;
					}
					if (payloadOpt.isPresent()) {
						// do something with payload
						postbackContainer.processPostback(payloadOpt.get(), user);
					}
					if (paymentOpt.isPresent()) {
						//do something with payment
					}
				});
			});
		} catch (HttpClientErrorException e) {
			LOGGER.error(e.getResponseBodyAsString(), e);
		} catch (Exception e) {
			LOGGER.error("System exception", e);
		}
	}

	private Optional<String> getPayload(Messaging messaging) {
		if (messaging.getPostback() != null) {
			return Optional.ofNullable(messaging.getPostback().getPayload());
		}
		return Optional.empty();
	}

	private Optional<String> getQuickReply(Messaging messaging) {
		if (messaging.getMessage() != null
				&& messaging.getMessage().getQuickReply() != null) {
			return Optional.ofNullable(messaging.getMessage().getQuickReply().getPayload());
		}
		return Optional.empty();
	}

	private Optional<String> getTextMessage(Messaging messaging) {
		if (messaging.getMessage() != null) {
			return Optional.ofNullable(messaging.getMessage().getText());
		}
		return Optional.empty();
	}

	private Optional<Coordinates> getCoordinates(Messaging messaging) {
		if (messaging.getMessage() != null) {
			List<Attachment> attachments = messaging.getMessage().getAttachments();
			if (!CollectionUtils.isEmpty(attachments)) {
				Payload payload = attachments.get(0).getPayload();
				if (payload != null) {
					return Optional.ofNullable(payload.getCoordinates());
				}
			}
		}
		return Optional.empty();
	}

	private Optional<Referral> getReferral(Messaging messaging) {
			if (messaging.getReferral() != null) {
				return Optional.ofNullable(messaging.getReferral());
			}
			if (messaging.getPostback() != null && messaging.getPostback().getReferral() != null) {
				return Optional.ofNullable(messaging.getPostback().getReferral());
			}
		return Optional.empty();
	}

	private Optional<Account> getAccount(Messaging messaging) {
		if (messaging.getAccount() != null) {
			return Optional.ofNullable(messaging.getAccount());
		}
		return Optional.empty();
	}

	private Optional<Payment> getPayment(Messaging messaging) {
		if (messaging.getMessage() != null
				&& messaging.getPayment() != null) {
			return Optional.ofNullable(messaging.getPayment());
		}
		return Optional.empty();
	}
}
