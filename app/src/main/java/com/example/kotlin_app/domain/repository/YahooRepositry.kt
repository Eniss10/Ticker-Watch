package com.example.kotlin_app.domain.repository


import com.example.kotlin_app.common.tickers.StockTicker
import com.example.kotlin_app.domain.repository.model.YahooResult

interface YahooRepository {
    suspend fun getChart(ticker: StockTicker, range: String, interval: String): Result<YahooResult>
}