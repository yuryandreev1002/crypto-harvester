package com.crypto.harvester.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.knowm.xchange.dto.marketdata.Ticker;
import org.springframework.stereotype.Service;

import com.crypto.harvester.config.InstrumentProps;
import com.crypto.harvester.model.CryptoInfo;
import com.crypto.harvester.repository.CryptoInfoRepository;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.poloniex.PoloniexStreamingExchange;
import io.reactivex.disposables.Disposable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PoloneixHarvesterService extends HarvesterService
{
	private final StreamingExchange exchange =
			StreamingExchangeFactory.INSTANCE.createExchange(PoloniexStreamingExchange.class);
	private Disposable tickers;
	private Map<String, LinkedList<Ticker>> exchangeTickers = new HashMap<>();

	private final CryptoInfoRepository cryptoInfoRepository;
	private final InstrumentProps instrumentProps;

	protected void createCryptoInfo(Ticker ticker, String instrumentName)
	{
		CryptoInfo cryptoInfo = new CryptoInfo();
		cryptoInfo.setName(instrumentName);
		cryptoInfo.setExchange("Poloneix");
		cryptoInfo.setTime(LocalDateTime.now());
		cryptoInfo.setAsk(ticker.getAsk());
		cryptoInfo.setBid(ticker.getBid());

		cryptoInfoRepository.save(cryptoInfo);
	}

	@Override
	protected StreamingExchange getExchange()
	{
		return exchange;
	}

	@Override
	protected Disposable getTickers()
	{
		return tickers;
	}

	@Override
	protected void setTickers(Disposable tickers)
	{
		this.tickers = tickers;
	}

	@Override
	protected Map<String, LinkedList<Ticker>> getExchangeTickers()
	{
		return exchangeTickers;
	}

	@Override
	protected InstrumentProps getInstrumentProps()
	{
		return instrumentProps;
	}

	@PreDestroy
	public void destroy()
	{
		if (getTickers() != null)
		{
			getTickers().dispose();
		}
		if (getExchange() != null)
		{
			getExchange().disconnect().blockingAwait();
		}
	}
}
