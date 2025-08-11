package com.example.kotlin_app.presentation.ui.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.kotlin_app.R
import com.example.kotlin_app.common.plotDiagram
import com.example.kotlin_app.domain.repository.model.StockItem
import com.github.mikephil.charting.charts.LineChart

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailsDialog(
    onDismiss: () -> Unit = {},
    displayedItem: StockItem
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
    ) {
        Box(
            Modifier
                .height(800.dp)
                .background(Color.White)

        ) {
            Column (modifier = Modifier.verticalScroll(rememberScrollState())) {
                Header(
                    currentSymbol = displayedItem.ticker.symbol,
                    currentName = displayedItem.shortName,
                    urlLogo = displayedItem.logoUrl,
                    logoRes = displayedItem.logoRes
                )

                Spacer(modifier = Modifier.height(30.dp))

                AndroidView(
                    factory = { context ->
                        val view = LayoutInflater.from(context)
                            .inflate(R.layout.frame_linechart, null, false)
                        val chart = view.findViewById<LineChart>(R.id.line_chart)

                        chart.description.isEnabled = false
                        chart.setNoDataText("Loading…")

                        view
                    },
                    update = { view ->
                        val chart = view.findViewById<LineChart>(R.id.line_chart)
                        plotDiagram(displayedItem.prices, chart)
                    }
                )

            }
        }
    }
}

@Composable
private fun Header(
    currentSymbol: String? = null,
    currentName: String? = null,
    urlLogo: String? = null,
    logoRes: Int? = null
) {
    Row (
        Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        StockTitle(currentSymbol, currentName, logoRes = logoRes, urlLogo = urlLogo)

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_share),
            contentDescription = "share image",
            Modifier.size(30.dp)
        )
    }
}

@Composable
private fun StockTitle(
    currentSymbol: String? = null,
    currentName: String? = null,
    urlLogo: String? = null,
    logoRes: Int? = null
    ) {
    Row() {
        Image(
            painter = logoRes?.let { painterResource(id = it) } ?: rememberAsyncImagePainter(urlLogo),
            contentDescription = "stock icon",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Box(
            Modifier
                .padding(start = 10.dp)
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                if (currentSymbol != null && currentName != null) {
                    Text(text = currentSymbol)
                    Text(text = currentName)
                }  else {
                    Text(text = "Invalid")
                }
            }
        }
    }
}

@Preview
@Composable
fun StockDetailsDialogPreview() {
    Header()
}

