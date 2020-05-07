package com.lnu.coronacitybot.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lnu.coronacitybot.service.MessengerService;

@Service
public class MessengerServiceImpl implements MessengerService {

	@Value("${facebook.verify.token}")
	private String verifyToken;

	@Override
	public boolean checkToken(String token) {
		return verifyToken.equals(token);
	}
}
