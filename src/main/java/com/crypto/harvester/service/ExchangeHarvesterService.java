package com.crypto.harvester.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.crypto.harvester.config.InstrumentProps;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExchangeHarvesterService
{
	private final InstrumentProps instrumentProps;
	private final BinanceHarvesterService binanceHarvesterService;
	private final PoloneixHarvesterService poloneixHarvesterService;

	@EventListener(ApplicationReadyEvent.class)
	public void startExchangeReading()
	{
		binanceHarvesterService.readCryptoInfo(instrumentProps.getInstruments());
		poloneixHarvesterService.readCryptoInfo(instrumentProps.getInstruments());
	}
}
