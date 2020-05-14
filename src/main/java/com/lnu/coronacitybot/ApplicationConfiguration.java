package com.lnu.coronacitybot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@PropertySource("classpath:custom-${spring.profiles.active}.properties")
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:general.properties")
public class ApplicationConfiguration {
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("locale/messages");
		return messageSource;
	}
}