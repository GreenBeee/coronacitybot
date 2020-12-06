package com.lnu.coronacitybot.entity;

import com.lnu.coronacitybot.converter.LocalDateTimeConverter;
import com.lnu.coronacitybot.entity.enums.SubscriptionRate;
import com.lnu.coronacitybot.entity.enums.SubscriptionType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

	@Column(name = "is_subscribed")
	private Boolean isSubscribed;

	@Column(name = "subscription_rate")
	@Enumerated(value = EnumType.STRING)
	private SubscriptionRate subscriptionRate;

	@Column(name = "subscription_cycle")
	private Long subscriptionCycle;

	@Column(name = "subscription_type")
	@Enumerated(value = EnumType.STRING)
	private SubscriptionType subscriptionType;

	@Column(name = "subscription_country")
	private String subscriptionCountry;

	@Column(name = "registered_at")
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime registeredAt;

	@Column
	private String state;

	@Column
	private String locale;

}
