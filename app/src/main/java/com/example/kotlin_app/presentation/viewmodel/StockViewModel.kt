package com.example.kotlin_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_app.common.Logger
import com.example.kotlin_app.common.tickers.StockTicker
import com.example.kotlin_app.domain.repository.YahooRepository
import com.example.kotlin_app.domain.repository.model.toStockData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val logger: Logger,
    private val yahooRepository: YahooRepository
) : ViewModel() {
    private val _currentPrice = MutableStateFlow<Double>(0.0)
    val currentPrice: StateFlow<Double> = _currentPrice

    private val _currentSymbol = MutableStateFlow<String>(StockTicker.NVIDIA.symbol)
    val currentSymbol: StateFlow<String> = _currentSymbol

    private val _lastYearClosePrices = MutableStateFlow<List<Double>?>(emptyList())
    val lastYearClosePrices: StateFlow<List<Double>?> = _lastYearClosePrices

    init {
        fetch()
    }

    private fun fetch() {
        viewModelScope.launch {
            val result = yahooRepository.getChart(symbol = _currentSymbol.value)
            val stockData = result.getOrNull()?.toStockData()

            if (stockData != null) {
                _currentPrice.value = stockData.currentPrice
                _lastYearClosePrices.value = stockData.closePrices
                logger.info("Stock data updated for symbol: ${_currentSymbol.value}")
            } else {
                logger.error("Failed to convert YahooResult to StockData for symbol: ${_currentSymbol.value}")
            }

        }
    }

    fun updateCurrentSymbol(stockTicker: StockTicker) {
        _currentSymbol.value = stockTicker.symbol
        fetch()
    }
}