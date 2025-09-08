package com.example.kotlin_app.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.example.kotlin_app.common.Logger
import com.example.kotlin_app.common.tickers.StockTicker
import com.example.kotlin_app.common.tickers.StockTicker.Companion.allTickers
import com.example.kotlin_app.domain.repository.YahooRepository
import com.example.kotlin_app.domain.repository.model.StockItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.kotlin_app.common.tickers.StockTicker.Companion.toStockTicker
import com.example.kotlin_app.data.local.toDomain
import com.example.kotlin_app.data.local.toEntity
import com.example.kotlin_app.data.repository.DbRepository
import com.example.kotlin_app.domain.repository.FinnHubRepository
import com.example.kotlin_app.domain.network.NetworkMonitor
import com.example.kotlin_app.domain.repository.model.Interval
import com.example.kotlin_app.domain.repository.model.Range
import com.example.kotlin_app.domain.repository.model.createPlaceholderStockItem
import com.example.kotlin_app.domain.repository.model.toStockItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.Period


@HiltViewModel
class MarketViewModel @Inject constructor(
    private val logger: Logger,
    private val yahooRepository: YahooRepository,
    private val finnHubRepository: FinnHubRepository,
    private val dbRepository: DbRepository,
    private val networkMonitor: NetworkMonitor
): ViewModel() {
    private val _currentTicker = MutableStateFlow<StockTicker>(allTickers.first())
    val currentTicker: StateFlow<StockTicker> = _currentTicker

    private val _currentStockItem = MutableStateFlow<StockItem>(createPlaceholderStockItem())
    val currentItem: StateFlow<StockItem> = _currentStockItem

    private val _currentRange = MutableStateFlow<Range>(Range.ONE_YEAR)
    val currentRange: StateFlow<Range> = _currentRange

    private val _currentStockList = MutableStateFlow<List<StockItem>>(emptyList())
    val currentStockList: StateFlow<List<StockItem>> = _currentStockList

    private var networkJob: Job? = null

    @SuppressLint("SuspiciousIndentation")
    private suspend fun fetchLogoUrl(ticker: StockTicker): String? {
        val result = finnHubRepository.getCompanyProfile(ticker.symbol)
        return result.getOrNull()?.logo
    }

    private suspend fun saveAllTickersInDb(stocks: List<StockItem>) {
        logger.info("Saving all tickers in DB")
        dbRepository.saveStocks(stocks.map { it.toEntity() })
    }

    private fun fetchStockList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val stockList = coroutineScope {
                    allTickers.map { ticker ->
                        async {
                            val chartResult = yahooRepository.getChart(ticker = ticker,range =  Range.ONE_YEAR.value, interval = Interval.ONE_DAY.value)
                            val chart = chartResult.getOrNull()

                            if (chart != null && chartResult.isSuccess) {
                                val logoUrl = if (ticker.logoRes != null) null else runCatching {
                                    fetchLogoUrl(ticker)
                                }.getOrNull()

                                chart.toStockItem(
                                    ticker = ticker,
                                    logoRes = ticker.logoRes,
                                    logoUrl = logoUrl
                                )


                            } else {
                                logger.error("Failed to fetch chart for ${ticker.symbol}: ${chartResult.exceptionOrNull()?.message}")
                                createPlaceholderStockItem()
                            }
                        }
                    }.awaitAll()
                }
                _currentStockList.value = stockList
                saveAllTickersInDb(stockList)
            } catch (e: Exception) {
                logger.error("Error fetching stock list: ${e.message}")
            }
        }
    }

    private fun fetchCurrentItem() {
        viewModelScope.launch {
            val result = yahooRepository.getChart(ticker = _currentTicker.value, range =  Range.ONE_YEAR.value, interval = Interval.ONE_DAY.value )
            val stockData = result.getOrNull()?.toStockItem(_currentTicker.value, fetchLogoUrl(_currentTicker.value))

            if (stockData != null) {
                _currentStockItem.value = stockData
                logger.info("Stock data updated for symbol: ${_currentTicker.value}")
            } else {
                logger.error("Failed to convert YahooResult to StockData for symbol: ${_currentTicker.value}")
            }

        }
    }

    fun updateCurrentSymbol(stockTicker: StockTicker) {
        _currentTicker.value = stockTicker
        fetchCurrentItem()
    }

    fun unregisterNetworkObserver() {
        networkMonitor.unregisterNetworkCallback()
        networkJob?.cancel()
        networkJob = null
    }

    fun registerNetworkObserver() {
        if(networkJob != null) return

        networkJob = viewModelScope.launch {
            networkMonitor.registerNetworkCallback()

            networkMonitor.isOnline.collect { isOnline ->
                if (isOnline) {
                    logger.info("Device is online")
                    fetchStockList()
                }
                else {
                    logger.info("Device is offline")
                    if (_currentStockList.value.isEmpty() && dbRepository.getAllStocks().isNotEmpty()) {
                        val dbStocks = dbRepository.getAllStocks()
                            .map { it.toDomain(toStockTicker(it.symbol)) }
                            .filter { it.ticker != StockTicker.IVALIDTICKER }
                        _currentStockList.value = dbStocks
                    }
                }
            }
        }
    }
}