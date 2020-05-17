package com.lnu.coronacitybot.dao;

import com.lnu.coronacitybot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserDAO extends JpaRepository<User, Integer> {
	User findByChatId(String chatId);

	Set<User> findAllByIsSubscribed(Boolean isSubscribed);
}
