package com.example.kotlin_app.data.repository

import com.example.kotlin_app.common.tickers.StockTicker
import com.example.kotlin_app.data.remote.YahooApi
import com.example.kotlin_app.domain.repository.YahooRepository
import com.example.kotlin_app.domain.repository.model.YahooResult
import javax.inject.Inject

class YahooRepositoryImpl @Inject constructor(
    private val api: YahooApi
) : YahooRepository {
    override suspend fun getChart(ticker: StockTicker): Result<YahooResult> {
      return try {
          val yahooResult = api.getChart(ticker.symbol.toString())
          Result.success(yahooResult)
      } catch (e: Exception) {
          Result.failure(e)
      }
    }
}