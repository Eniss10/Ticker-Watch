package com.example.kotlin_app.presentation.viewmodel

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
import com.example.kotlin_app.domain.repository.model.toStockItem

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val logger: Logger,
    private val yahooRepository: YahooRepository
): ViewModel() {
    private val _currentTicker = MutableStateFlow<StockTicker>(allTickers.first())
    val currentTicker: StateFlow<StockTicker> = _currentTicker

    private val _currentStockItem = MutableStateFlow<StockItem?>(null)
    val currentItem: StateFlow<StockItem?> = _currentStockItem

    private val _currentStockList = MutableStateFlow<List<StockItem?>>(emptyList())
    val currentStockList: StateFlow<List<StockItem?>> = _currentStockList

    init {
          fetchStockList()
    }

    private fun fetchStockList() {
        viewModelScope.launch {
            try {
                _currentStockList.value = allTickers.map { ticker ->
                    logger.info("Fetching stock data for ticker: $ticker")
                    val retrievedTicker = yahooRepository.getChart(ticker = ticker)
                    retrievedTicker.getOrNull()?.toStockItem(ticker)
                }
            }catch (e : Exception) {
                logger.error("Error fetching stock list: ${e.message}")
            }

        }
    }

    private fun fetchCurrentItem() {
        viewModelScope.launch {
            val result = yahooRepository.getChart(ticker = _currentTicker.value)
            val stockData = result.getOrNull()?.toStockItem(_currentTicker.value)

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