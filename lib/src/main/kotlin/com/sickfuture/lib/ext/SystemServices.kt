package com.sickfuture.lib.ext

import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Build


fun AlarmManager.setCompat(type: Int, triggerTime: Long, pendingIntent: PendingIntent) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
        setExact(type, triggerTime, pendingIntent)
    } else {
        set(type, triggerTime, pendingIntent)
    }
}
