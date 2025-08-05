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
import com.example.kotlin_app.presentation.stock.StockFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logger.info("[onCreate]")
        supportFragmentManager.beginTransaction()
            .replace(R.id.stock_fragment_container, StockFragment())
            .commit()
        }
}