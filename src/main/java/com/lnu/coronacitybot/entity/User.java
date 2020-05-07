package com.lnu.coronacitybot.entity;

import com.lnu.coronacitybot.converter.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = "chat_id")
	private String chatId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "registered_at")
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime registeredAt;

	@Column
	private String state;

}
