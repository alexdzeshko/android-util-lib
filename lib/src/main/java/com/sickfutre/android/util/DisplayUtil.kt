package com.sickfutre.android.util

import android.content.Context
import android.util.TypedValue

/**
 * Created by Alex Dzeshko on 20-Mar-16.
 */
object DisplayUtil {

    fun dp(context: Context, dp: Int): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics)
        return px.toInt()
    }
}
