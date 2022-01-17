package com.crypto.harvester.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.knowm.xchange.dto.marketdata.Ticker;
import org.springframework.scheduling.annotation.Scheduled;

import com.crypto.harvester.config.InstrumentProps;
import com.crypto.harvester.data.Instrument;
import com.crypto.harvester.util.CurrencyUtil;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchange;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class HarvesterService
{
	public void readCryptoInfo(List<Instrument> instruments)
	{
		ProductSubscription.ProductSubscriptionBuilder productSubscriptionBuilder = ProductSubscription.create();
		for (Instrument instrument : instruments)
		{
			productSubscriptionBuilder.addTicker(CurrencyUtil.getCurrencyPair(instrument.getName()));
		}
		ProductSubscription subscription = productSubscriptionBuilder.build();

		getExchange().connect(subscription).blockingAwait();

		for (Instrument instrument : instruments)
		{
			String instName = instrument.getName();
			Disposable disposable = getExchange().getStreamingMarketDataService()
					.getTicker(CurrencyUtil.getCurrencyPair(instName))
					.subscribe(
							ticker ->
							{
								log.info("Create a crypto info.");
								if (getExchangeTickers().containsKey(instName))
								{
									LinkedList<Ticker> tickers = getExchangeTickers().get(instName);
									tickers.add(ticker);
								}
								else
								{
									LinkedList<Ticker> tickers = new LinkedList<>();
									tickers.add(ticker);
									getExchangeTickers().put(instName, tickers);
								}
							},
							throwable -> log.error("ERROR in getting ticker: ", throwable));

			setTickers(disposable);
		}
	}

	@Scheduled(cron = "0/${flush.period.s} * * * * ?")
	public void buildCryptoInfo()
	{
		List<Instrument> instruments = getInstrumentProps().getInstruments();
		instruments.forEach(i ->
		{
			String instrumentName = i.getName();
			if (getExchangeTickers().containsKey(instrumentName))
			{
				createCryptoInfo(getExchangeTickers().get(instrumentName).getLast(), instrumentName);
				getExchangeTickers().get(instrumentName).clear();
				log.info("CryptoInfo was saved to the DataBase.");
			}
		});
	}

	protected abstract void createCryptoInfo(Ticker ticker, String instrumentName);

	protected abstract StreamingExchange getExchange();

	protected abstract Disposable getTickers();

	protected abstract void setTickers(Disposable tickers);

	protected abstract Map<String, LinkedList<Ticker>> getExchangeTickers();

	protected abstract InstrumentProps getInstrumentProps();
}
