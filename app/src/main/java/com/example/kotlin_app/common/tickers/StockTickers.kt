package com.example.kotlin_app.common.tickers

enum class StockTicker(val symbol: String) {
    APPLE("AAPL"),
    MICROSOFT("MSFT"),
    AMAZON("AMZN"),
    ALPHABET("GOOGL"),
    META("META"),
    NVIDIA("NVDA"),
    TESLA("TSLA"),
    AMD("AMD"),
    INTEL("INTC"),
    IBM("IBM"),
    VISA("V"),
    MASTERCARD("MA"),
    JPMORGAN("JPM"),
    BANK_OF_AMERICA("BAC"),
    GOLDMAN_SACHS("GS"),
    COCA_COLA("KO"),
    PEPSICO("PEP"),
    NIKE("NKE"),
    MCDONALDS("MCD"),
    DISNEY("DIS"),
    WALMART("WMT"),
    COSTCO("COST"),
    HOME_DEPOT("HD"),
    PROCTER_GAMBLE("PG"),
    JOHNSON_JOHNSON("JNJ"),
    BITCOIN("BTC-USD"),
    ETHEREUM("ETH-USD"),
    SOLANA("SOL-USD"),
    DOGECOIN("DOGE-USD"),
    CARDANO("ADA-USD");

    companion object {
        val allTickers = values().toList()
    }
}
