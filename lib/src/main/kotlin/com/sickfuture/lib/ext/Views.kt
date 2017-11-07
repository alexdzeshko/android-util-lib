package com.sickfuture.lib.ext

import android.view.View
import android.widget.TextView

/**
 * Created by Alex Dzeshko on 06-May-16.
 */

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun TextView.drawableLeft(drawable: Int): Unit {
    setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}
