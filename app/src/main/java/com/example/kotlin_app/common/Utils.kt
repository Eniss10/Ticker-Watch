package com.example.kotlin_app.common

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

fun plotDiagram(closePrices: List<Double>?, chart: LineChart) {
    val entries = closePrices?.mapIndexed { index, price ->
        Entry(index.toFloat(), price.toFloat())
    }

    val dataSet = LineDataSet(entries, "Close Prices").apply {
        color = Color.BLUE
        valueTextColor = Color.BLACK
        setDrawCircles(false)
        lineWidth = 2f
        mode = LineDataSet.Mode.CUBIC_BEZIER
    }

    val lineData = LineData(dataSet)

    chart.data = lineData
    chart.description.text = "Stock close prices"
    chart.invalidate()
}