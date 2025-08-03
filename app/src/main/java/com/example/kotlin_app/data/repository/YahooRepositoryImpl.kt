package com.example.kotlin_app.data.repository

import com.example.kotlin_app.data.remote.YahooApi
import com.example.kotlin_app.domain.repository.YahooRepository
import com.example.kotlin_app.domain.repository.model.YahooResult
import javax.inject.Inject

class YahooRepositoryImpl @Inject constructor(
    private val api: YahooApi
) : YahooRepository {
    override suspend fun getChart(symbol: String): Result<YahooResult> {
      return try {
          val yahooResult = api.getChart(symbol)
          Result.success(yahooResult)
      } catch (e: Exception) {
          Result.failure(e)
      }
    }
}