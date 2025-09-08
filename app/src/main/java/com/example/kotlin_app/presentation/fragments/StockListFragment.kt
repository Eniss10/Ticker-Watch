package com.example.kotlin_app.presentation.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.kotlin_app.common.Logger
import com.example.kotlin_app.presentation.ui.components.LoadingState
import com.example.kotlin_app.presentation.ui.components.StockList
import com.example.kotlin_app.presentation.viewmodel.MarketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StockListFragment: Fragment() {
    private val marketViewModel: MarketViewModel by viewModels()
    @Inject
    lateinit var logger: Logger

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

    override fun onStart() {
        super.onStart()
        marketViewModel.registerNetworkObserver()
    }

    override fun onStop() {
        super.onStop()
        marketViewModel.unregisterNetworkObserver()
    }
}