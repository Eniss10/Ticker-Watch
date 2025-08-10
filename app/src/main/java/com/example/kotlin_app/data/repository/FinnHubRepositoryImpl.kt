package com.example.kotlin_app.data.repository

import com.example.kotlin_app.common.Logger
import com.example.kotlin_app.data.remote.CompanyProfile
import com.example.kotlin_app.data.remote.FinnHubApi
import com.example.kotlin_app.domain.repository.FinnHubRepository
import javax.inject.Inject
import javax.inject.Named

class FinnHubRepositoryImpl @Inject constructor(
    private val finnHubApi: FinnHubApi,
    @Named("finnhubToken") private val apiKey: String
): FinnHubRepository {
    override suspend fun getCompanyProfile(symbol: String): Result<CompanyProfile> {
        return try {
            val profile = finnHubApi.getCompanyProfile(symbol, apiKey)
            return Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}