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
import com.example.kotlin_app.domain.repository.FinnHubRepository
import com.example.kotlin_app.domain.repository.model.createPlaceholderStockItem
import com.example.kotlin_app.domain.repository.model.toStockItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val logger: Logger,
    private val yahooRepository: YahooRepository,
    private val finnHubRepository: FinnHubRepository
): ViewModel() {
    private val _currentTicker = MutableStateFlow<StockTicker>(allTickers.first())
    val currentTicker: StateFlow<StockTicker> = _currentTicker

    private val _currentStockItem = MutableStateFlow<StockItem>(createPlaceholderStockItem())
    val currentItem: StateFlow<StockItem> = _currentStockItem

    private val _currentStockList = MutableStateFlow<List<StockItem>>(emptyList())
    val currentStockList: StateFlow<List<StockItem>> = _currentStockList

    init {
          fetchStockList()
    }

    @SuppressLint("SuspiciousIndentation")
    private suspend fun fetchLogoUrl(ticker: StockTicker): String? {
        val result = finnHubRepository.getCompanyProfile(ticker.symbol)
        return result.getOrNull()?.logo
    }

    private fun fetchStockList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _currentStockList.value = coroutineScope {
                    allTickers.map { ticker ->
                        async {
                            val chartResult = yahooRepository.getChart(ticker = ticker)
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
            } catch (e: Exception) {
                logger.error("Error fetching stock list: ${e.message}")
            }
        }
    }

    private fun fetchCurrentItem() {
        viewModelScope.launch {
            val result = yahooRepository.getChart(ticker = _currentTicker.value)
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
}