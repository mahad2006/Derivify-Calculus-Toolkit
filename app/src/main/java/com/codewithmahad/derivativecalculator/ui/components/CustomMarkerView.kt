package com.codewithmahad.derivativecalculator.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.codewithmahad.derivativecalculator.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.DecimalFormat

@SuppressLint("ViewConstructor")
class CustomMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val tvContent: TextView = findViewById(R.id.tvContent)
    private val format = DecimalFormat("#.##")

    // This method is called every time the MarkerView is redrawn
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            val text = "x: ${format.format(it.x)}, y: ${format.format(it.y)}"
            tvContent.text = text
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        // You can adjust the offset of the marker view if needed
        return MPPointF(-(width / 2f), -height.toFloat())
    }
}