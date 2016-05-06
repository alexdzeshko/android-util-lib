package com.sickfuture.lib.ext

import android.view.View

/**
 * Created by Alex Dzeshko on 06-May-16.
 */

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}