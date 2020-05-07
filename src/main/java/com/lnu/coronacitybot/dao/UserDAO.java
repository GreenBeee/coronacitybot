package com.lnu.coronacitybot.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lnu.coronacitybot.entity.User;

public interface UserDAO extends JpaRepository<User, Integer> {
	User findByChatId(String chatId);
}
