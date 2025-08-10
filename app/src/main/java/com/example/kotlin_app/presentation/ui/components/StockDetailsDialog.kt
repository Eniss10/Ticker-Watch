package com.example.kotlin_app.presentation.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.kotlin_app.R
import com.example.kotlin_app.presentation.viewmodel.MarketViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailsDialog(
    viewModel: MarketViewModel = hiltViewModel(),
    onDismiss: () -> Unit = {}
) {
    val currentItem by viewModel.currentItem.collectAsStateWithLifecycle()

    ModalBottomSheet(
        onDismissRequest = {onDismiss()}
    ) {
        Box(
            Modifier
                .size(500.dp)
                .background(Color.White)

        ) {
            Header(
                currentSymbol = currentItem?.ticker?.symbol,
                currentName = currentItem?.shortName,
                urlLogo = currentItem?.logoUrl
                )
        }

}
}

@Composable
private fun Header(
    currentSymbol: String? = null,
    currentName: String? = null,
    urlLogo: String? = null,
) {
    Row (
        Modifier.fillMaxWidth().padding(start = 18.dp, end = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        StockTitle(currentSymbol, currentName,urlLogo)

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
    ) {
    Row() {
        Image(
            painter = rememberAsyncImagePainter(urlLogo),
            contentDescription = "Apple stock icon",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Box(
            Modifier.padding(start = 10.dp).height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                if (currentSymbol != null && currentName != null) {
                    Text(text = currentName )
                    Text(text = currentSymbol)
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

