package com.example.kotlin_app.framework.network

import android.content.Context
import android.net.ConnectivityManager
import com.example.kotlin_app.domain.network.NetworkMonitor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class NetworkMonitorImpl @Inject constructor(
    @ApplicationContext private val context: Context
): NetworkMonitor {
    private val _isOnline = MutableStateFlow(false)
    override val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val networkReceiver: NetworkReceiver = NetworkReceiver(
        onConnect = { _isOnline.value = true },
        onDisconnect = { _isOnline.value = false }
    )
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    override fun registerNetworkCallback() {
        connectivityManager.registerDefaultNetworkCallback(networkReceiver)
    }

    override fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkReceiver)
    }
}