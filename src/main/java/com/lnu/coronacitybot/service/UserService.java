package com.lnu.coronacitybot.service;

import com.lnu.coronacitybot.entity.User;
import com.lnu.coronacitybot.entity.enums.State;
import com.lnu.coronacitybot.entity.enums.SubscriptionRate;
import com.lnu.coronacitybot.model.UserId;

import java.util.Optional;
import java.util.Set;

public interface UserService {

	User createIfNotExist(UserId chatId);

	void unlinkUserAccount(String id);

	Optional<String> saveLoginAndPasswordAndGetAuthCode(String username, String password, String userID);

	void save(User user);

	void setState(User user, String state);

	Set<User> getAllUsersWithActiveSubscription();

	void saveAll(Set<User> allUsersWithActiveSubscription);
}
