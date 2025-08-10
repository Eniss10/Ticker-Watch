package com.example.kotlin_app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlin_app.domain.repository.model.StockItem

@Composable
fun StockUiItem(
    stock: StockItem,
     onClickListener: () -> Unit = {}
    ) {
    Card(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth().padding(start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        onClick = onClickListener
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            Text(text = stock.ticker.symbol)
            Text(text = "$${stock.price.toString()}")
        }
    }
}
