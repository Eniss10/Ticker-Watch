package com.example.kotlin_app.presentation.stock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import com.example.kotlin_app.R
import com.example.kotlin_app.common.Logger
import com.example.kotlin_app.presentation.viewmodel.StockViewModel
import javax.inject.Inject
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.kotlin_app.common.plotDiagram
import com.example.kotlin_app.databinding.FragmentStockBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StockFragment : Fragment() {
    @Inject
    lateinit var logger: Logger

    private val viewModel: StockViewModel by viewModels()
    private var _binding: FragmentStockBinding? = null
    private val binding get() = _binding!!
    private lateinit var uiState: Flow<Triple<Double, List<Double>?, String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiState = combine(
            viewModel.currentPrice,
            viewModel.lastYearClosePrices,
            viewModel.currentSymbol
        ) { price, closePrices, symbol ->
            Triple(price, closePrices, symbol)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uiState.collect { (price, closePrices, symbol) ->
                    logger.info("Price: $price, chart updated")
                    binding.txtPrice.text = "$$price"
                    binding.txtSymbol.text = symbol
                    plotDiagram(closePrices, binding.firstGraph)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
