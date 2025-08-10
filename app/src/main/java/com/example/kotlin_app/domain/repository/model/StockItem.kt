package com.example.kotlin_app.domain.repository.model

import com.example.kotlin_app.common.tickers.StockTicker

data class StockItem (val ticker: StockTicker,
                      val name: String,
                      var price: Double,
                      var prices: List<Double> = emptyList<Double>()
    )

fun YahooResult.toStockItem(ticker: StockTicker): StockItem {
    return StockItem(
        ticker = ticker,
        name = chart.result.first().meta.longName,
        price = chart.result.firstOrNull()?.meta?.regularMarketPrice ?: 0.0,
        prices = chart.result.firstOrNull()?.indicators?.quote?.firstOrNull()?.close ?: emptyList()
    )
}