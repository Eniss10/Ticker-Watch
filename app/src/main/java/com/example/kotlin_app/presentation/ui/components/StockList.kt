package com.example.kotlin_app.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlin_app.common.tickers.StockTicker
import com.example.kotlin_app.domain.repository.model.StockItem

@Composable
fun StockList(list: List<StockItem>) {
    LazyColumn (
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(list) { stock ->
            StockUiItem(stock = stock)
        }
    }
}

@Preview
@Composable
fun StockListPreview() {
    val stockList = listOf(
        StockItem(StockTicker.APPLE, 100.0),
        StockItem(StockTicker.MICROSOFT, 100.0),
        StockItem(StockTicker.AMAZON, 100.0))
    StockList(list = stockList)
}