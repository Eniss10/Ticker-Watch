package com.example.kotlin_app.domain.repository.model

import com.example.kotlin_app.common.tickers.StockTicker
import com.example.kotlin_app.common.tickers.StockTicker.IVALIDTICKER

data class StockItem (val ticker: StockTicker,
                      val longName: String,
                      val shortName: String,
                      var price: Double,
                      val logoUrl: String?,
                      val logoRes: Int?,
                      var prices: List<Double> = emptyList<Double>()
    )

fun YahooResult.toStockItem(ticker: StockTicker, logoUrl: String? = null, logoRes: Int? = null): StockItem {
    return StockItem(
        ticker = ticker,
        logoUrl = logoUrl,
        logoRes = logoRes,
        longName = chart.result.first().meta.longName,
        shortName =  chart.result.first().meta.shortName,
        price = chart.result.firstOrNull()?.meta?.regularMarketPrice ?: 0.0,
        prices = chart.result.firstOrNull()?.indicators?.quote?.firstOrNull()?.close ?: emptyList()
    )
}

fun createPlaceholderStockItem() = StockItem (
    ticker = IVALIDTICKER,
    longName = "",
    shortName = "",
    price = 0.0,
    logoUrl = null,
    logoRes = null )