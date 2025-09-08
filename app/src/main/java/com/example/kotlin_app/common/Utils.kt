package com.example.kotlin_app.common

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
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

    chart.xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        setDrawGridLines(true)
        granularity = 1f
        isGranularityEnabled = true
        textColor = Color.BLACK
        gridColor = Color.LTGRAY
    }

    chart.axisLeft.apply {
        setDrawGridLines(true)
        gridColor = Color.LTGRAY
        textColor = Color.BLACK
    }

    chart.axisRight.isEnabled = false
    chart.description.text = "Stock close prices"
    chart.invalidate()
}
