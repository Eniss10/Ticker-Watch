package com.example.kotlin_app.common.tickers

import androidx.annotation.DrawableRes
import com.example.kotlin_app.R
import com.example.kotlin_app.domain.repository.model.StockItem

enum class StockTicker(val symbol: String, @DrawableRes val logoRes: Int? = null) {
    IVALIDTICKER("INVALIDTICKER"),
    APPLE("AAPL", R.drawable.aapl),
    MICROSOFT("MSFT", R.drawable.msft),
    AMAZON("AMZN", R.drawable.amzn),
    ALPHABET("GOOGL", R.drawable.googl),
    META("META", R.drawable.meta),
    NVIDIA("NVDA", R.drawable.nvda),
    TESLA("TSLA", R.drawable.tsla),
    AMD("AMD", R.drawable.amd),
    INTEL("INTC", R.drawable.intc),
    IBM("IBM", R.drawable.ibm),
    VISA("V", R.drawable.v),
    MASTERCARD("MA", R.drawable.ma),
    JPMORGAN("JPM", R.drawable.jpm),
    BANK_OF_AMERICA("BAC", R.drawable.bac),
    GOLDMAN_SACHS("GS", R.drawable.gs);

    companion object {
        val allTickers = values().toList().filterNot { it == IVALIDTICKER }
    }
}
