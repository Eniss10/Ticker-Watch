package com.example.kotlin_app.domain.repository.model

data class StockData(
    val currentPrice: Double,
    val closePrices: List<Double>
)

fun YahooResult.toStockData(): StockData {
    return StockData(
        currentPrice = chart.result.firstOrNull()?.meta?.regularMarketPrice ?: 0.0,
        closePrices = chart.result.firstOrNull()?.indicators?.quote?.firstOrNull()?.close ?: emptyList()
    )
}