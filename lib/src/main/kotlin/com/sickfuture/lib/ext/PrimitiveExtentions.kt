package com.sickfuture.lib.ext

/**
 * Created by Alex Dzeshko on 05-May-16.
 */
fun Float.abs(): Float = Math.abs(this)

val Boolean.intValue: Int
    get() = if (this) 1 else 0