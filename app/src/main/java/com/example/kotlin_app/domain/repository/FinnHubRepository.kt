package com.example.kotlin_app.domain.repository

import com.example.kotlin_app.data.remote.CompanyProfile

interface FinnHubRepository {
    suspend fun getCompanyProfile(symbol: String): Result<CompanyProfile>
}