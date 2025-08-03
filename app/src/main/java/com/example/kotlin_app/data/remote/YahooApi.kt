package com.example.kotlin_app.data.remote


import com.example.kotlin_app.domain.repository.model.YahooResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface YahooApi {
    @GET("v8/finance/chart/{symbol}")
    suspend fun getChart(
        @Path("symbol") symbol: String,
        @Query("interval") interval: String = "1d",
        @Query("range") range: String = "1y",
        @Query("includePrePost") includePrePost: Boolean = false
    ): YahooResult
}