package com.sickfuture.lib.ext

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.sickfutre.android.util.CursorUtils
import com.sickfutre.android.util.Platform
import java.util.*

inline fun SQLiteDatabase.inTransaction(func: SQLiteDatabase.() -> Unit) {
    beginTransaction()
    try {
        func()
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
}

fun Cursor.cloze() {
    if (!isClosed) {
        close()
    }
}

fun Cursor.string(columnName: String): String? {
    val columnIndex = getColumnIndex(columnName)
    return if (columnIndex == -1) null else getString(columnIndex)
}

fun Cursor.int(columnName: String): Int {
    val columnIndex = getColumnIndex(columnName)
    return if (columnIndex == -1) -1 else getInt(columnIndex)
}

fun Cursor.byte(columnName: String): Byte {
    return int(columnName).toByte()
}

fun Cursor.double(columnName: String): Double {
    val columnIndex = getColumnIndex(columnName)
    return if (columnIndex == -1) -1.toDouble() else getDouble(columnIndex)
}

fun Cursor.float(columnName: String): Float {
    val columnIndex = getColumnIndex(columnName)
    return if (columnIndex == -1) -1.toFloat() else getFloat(columnIndex)
}

fun Cursor.long(columnName: String): Long {
    val columnIndex = getColumnIndex(columnName)
    return if (columnIndex == -1) -1 else getLong(columnIndex)
}

fun Cursor.short(columnName: String): Short {
    val columnIndex = getColumnIndex(columnName)
    return if (columnIndex == -1) -1 else getShort(columnIndex)
}

fun Cursor.blob(columnName: String): ByteArray? {
    val columnIndex = getColumnIndex(columnName)
    if (columnIndex == -1) {
        return null
    }
    return getBlob(columnIndex)
}

fun Cursor.date(columnName: String): Date? {
    val millis = long(columnName)
    if (millis > 0) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return calendar.time
    } else {
        return null
    }
}

fun Cursor.isEmpty(): Boolean = count == 0

fun Cursor.notEmpty(): Boolean = count > 0

fun Cursor.boolean(columnName: String): Boolean {
    val columnIndex = getColumnIndex(columnName)
    return columnIndex != -1 && getInt(columnIndex) == 1
}

fun Cursor.getNotificationUri(): String {
    if (Platform.hasKK()) {
        if (notificationUri != null) {
            return notificationUri.toString()
        }
    }
    return ""
}

fun Cursor.dump() {
    val uri = notificationUri
    if (!isClosed) {
        if (moveToFirst()) {
            val builder = StringBuilder(">>>>> Dumping cursor for ")
            builder.append(uri).append("\n")
            do {
                builder.append(position + 1).append(CursorUtils.DIVIDER).append("\n")
                for (i in 0..columnCount - 1) {
                    val columnName = getColumnName(i)
                    var value = ""
                    val type = getType(i)
                    when (type) {
                        CursorUtils.FIELD_TYPE_BLOB -> value = ">>BLOB<<"
                        CursorUtils.FIELD_TYPE_INTEGER -> value = getInt(i).toString()
                        CursorUtils.FIELD_TYPE_FLOAT -> value = getFloat(i).toString()
                        CursorUtils.FIELD_TYPE_STRING -> value = getString(i)
                        CursorUtils.FIELD_TYPE_NULL -> value = "null"
                    }
                    builder.append(columnName).append(" ---- ").append(value).append("\n")
                }
                builder.append(CursorUtils.DIVIDER).append("\n")
            } while (moveToNext())
            Log.v("db", builder.toString())
        } else {

            Log.v("db", "cursor for $uri is empty")
        }
    } else {
        Log.w("db", "cursor $uri null or closed")
    }
}

fun Cursor.dumpToString(): String {
    val uri = notificationUri
    if (!isClosed) {
        if (moveToFirst()) {
            val builder = StringBuilder(">>>>> Dumping cursor for ")
            builder.append(uri).append("\n")
            do {
                builder.append(position + 1).append(CursorUtils.DIVIDER).append("\n")
                for (i in 0..columnCount - 1) {
                    val columnName = getColumnName(i)
                    var value = ""
                    val type = getType(i)
                    when (type) {
                        CursorUtils.FIELD_TYPE_BLOB -> value = ">>BLOB<<"
                        CursorUtils.FIELD_TYPE_INTEGER -> value = getInt(i).toString()
                        CursorUtils.FIELD_TYPE_FLOAT -> value = getFloat(i).toString()
                        CursorUtils.FIELD_TYPE_STRING -> value = getString(i)
                        CursorUtils.FIELD_TYPE_NULL -> value = "null"
                    }
                    builder.append(columnName).append(" ---- ").append(value).append("\n")
                }
                builder.append(CursorUtils.DIVIDER).append("\n")
            } while (moveToNext())
            return builder.toString()
        } else {
            return "cursor for $uri is empty"
        }
    } else {
        return "cursor $uri null or closed"
    }
}