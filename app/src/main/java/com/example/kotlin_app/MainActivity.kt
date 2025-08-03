package com.example.kotlin_app

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.kotlin_app.common.Logger
import com.example.kotlin_app.common.plotDiagram
import com.example.kotlin_app.databinding.ActivityMainBinding
import com.example.kotlin_app.presentation.viewmodel.StockViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var logger: Logger

    private lateinit var binding: ActivityMainBinding
    private lateinit var uiState: Flow<Triple<Double, List<Double>?, String>>
    private val viewModel: StockViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uiState = combine(
            viewModel.currentPrice,
            viewModel.lastYearClosePrices,
            viewModel.currentSymbol
        ) { price, closePrices, symbol ->
            Triple(price, closePrices, symbol)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uiState.collect { (price, closePrices, symbole) ->
                    logger.info("Price: $price, chart updated")
                    binding.txtPrice.text = "$$price"
                    binding.txtSymbol.text = symbole
                    plotDiagram(closePrices, binding.firstGraph)
                }
            }
        }
    }
}