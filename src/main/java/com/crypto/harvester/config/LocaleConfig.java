package com.crypto.harvester.config;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class LocaleConfig
{
	/**
	 * Set default timezone to UTC
	 */
	@PostConstruct
	private void init()
	{
		log.info("Set default timezone to UTC.");
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}
