package com.example.kotlin_app.di

import com.example.kotlin_app.common.Logger
import com.example.kotlin_app.common.LoggerImpl
import com.example.kotlin_app.data.remote.YahooApi
import com.example.kotlin_app.data.repository.YahooRepositoryImpl
import com.example.kotlin_app.domain.repository.YahooRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideYahooApi(): YahooApi {
        return Retrofit.Builder()
            .baseUrl("https://query1.finance.yahoo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YahooApi::class.java)
    }

    @Provides
    fun provideYahooRepository(api: YahooApi): YahooRepository {
        return YahooRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideLogger(): Logger = LoggerImpl()
}