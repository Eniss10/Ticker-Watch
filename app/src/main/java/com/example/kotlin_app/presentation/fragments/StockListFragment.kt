package com.example.kotlin_app.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.kotlin_app.common.Logger
import com.example.kotlin_app.common.tickers.StockTicker
import com.example.kotlin_app.domain.repository.model.StockItem
import com.example.kotlin_app.presentation.ui.components.LoadingState
import com.example.kotlin_app.presentation.ui.components.StockList
import com.example.kotlin_app.presentation.viewmodel.MarketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StockListFragment: Fragment() {
    private val marketViewModel: MarketViewModel by viewModels()
    @Inject
    lateinit var logger: Logger

//    private var streamJob: Job? = null
//    fun observeSymbol(symbol: String) {
//        // Cancel any previous stream
//        streamJob?.cancel()
//        streamJob = viewLifecycleOwner.lifecycleScope.launch {
//            marketViewModel.startStreaming(symbol).collect { tick ->
//                logger.info("${tick?.symbol} ${tick?.price}")
//            }
//        }
//    }
//
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        viewLifecycleOwner.lifecycleScope.launch {
            marketViewModel.currentStockList.collect { stockList ->
                setContent {
                    if (stockList.isEmpty()) {
                        LoadingState()
                    } else {
                        StockList(stockList, marketViewModel)
                    }
                }
            }
        }

    }
}