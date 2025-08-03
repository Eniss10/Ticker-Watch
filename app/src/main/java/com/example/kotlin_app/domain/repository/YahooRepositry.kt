package com.example.kotlin_app.domain.repository


import com.example.kotlin_app.domain.repository.model.YahooResult

interface YahooRepository {
    suspend fun getChart(symbol: String): Result<YahooResult>
}