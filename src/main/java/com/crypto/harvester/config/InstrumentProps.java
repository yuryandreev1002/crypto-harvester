package com.crypto.harvester.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.crypto.harvester.data.Instrument;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "application")
@Data
public class InstrumentProps
{
	private List<Instrument> instruments;
}
