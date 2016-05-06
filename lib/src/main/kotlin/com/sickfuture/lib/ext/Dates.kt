package com.sickfuture.lib.ext

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Alex Dzeshko on 06-May-16.
 */
fun Date.format(format: String) = SimpleDateFormat(format).format(this)
