package com.lnu.coronacitybot.service;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.model.UserId;

import java.util.Optional;

public interface UserService {

	User createIfNotExist(UserId chatId);

	void unlinkUserAccount(String id);

	Optional<String> saveLoginAndPasswordAndGetAuthCode(String username, String password, String userID);

	void save(User user);
}
