package com.example.cyclofit.ui.utils

import com.github.mikephil.charting.utils.ColorTemplate

object ChartUtils {

    val colors = ArrayList<Int>().apply {
        for (c in ColorTemplate.VORDIPLOM_COLORS) add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) add(c)
        for (c in ColorTemplate.PASTEL_COLORS) add(c)
        add(ColorTemplate.getHoloBlue())
    }
}