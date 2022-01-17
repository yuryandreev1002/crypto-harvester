package com.crypto.harvester.util;

import org.knowm.xchange.currency.CurrencyPair;

public class CurrencyUtil
{
	public static CurrencyPair getCurrencyPair(String instrumentName)
	{
		switch (instrumentName)
		{
			case "ETHUSD": return CurrencyPair.ETH_USDT;
			case "BTCUSD":
			default: return CurrencyPair.BTC_USDT;
		}
	}
}
