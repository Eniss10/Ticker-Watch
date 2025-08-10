package com.example.kotlin_app.domain.repository.model

import com.example.kotlin_app.common.tickers.StockTicker

data class StockItem (val ticker: StockTicker,
                      val longName: String,
                      val shortName: String,
                      var price: Double,
                      val logoUrl: String?,
                      var prices: List<Double> = emptyList<Double>()
    )

fun YahooResult.toStockItem(ticker: StockTicker, logoUrl: String?): StockItem {
    return StockItem(
        ticker = ticker,
        logoUrl = logoUrl,
        longName = chart.result.first().meta.longName,
        shortName =  chart.result.first().meta.shortName,
        price = chart.result.firstOrNull()?.meta?.regularMarketPrice ?: 0.0,
        prices = chart.result.firstOrNull()?.indicators?.quote?.firstOrNull()?.close ?: emptyList()
    )
}