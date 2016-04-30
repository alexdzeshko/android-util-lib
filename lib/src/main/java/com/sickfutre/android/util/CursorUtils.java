package com.sickfutre.android.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CursorUtils {

    public static final int FIELD_TYPE_NULL = 0;

    public static final int FIELD_TYPE_INTEGER = 1;

    public static final int FIELD_TYPE_FLOAT = 2;
    public static final int FIELD_TYPE_STRING = 3;

    public static final int FIELD_TYPE_BLOB = 4;
    public static final String DIVIDER = "___________________________________________________";

    public static String getString(String columnName, Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return (columnIndex == -1) ? null : cursor.getString(columnIndex);
    }

    public static Integer getInt(String columnName, Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return (columnIndex == -1) ? -1 : cursor.getInt(columnIndex);
    }

    public static byte getByte(String columnName, Cursor cursor) {
        return getInt(columnName, cursor).byteValue();
    }

    public static Double getDouble(String columnName, Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return (columnIndex == -1) ? -1 : cursor.getDouble(columnIndex);
    }

    public static Float getFloat(String columnName, Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return (columnIndex == -1) ? -1 : cursor.getFloat(columnIndex);
    }

    public static Long getLong(String columnName, Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return (columnIndex == -1) ? -1 : cursor.getLong(columnIndex);
    }

    public static Short getShort(String columnName, Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex == -1) {
            return null;
        }
        return cursor.getShort(columnIndex);
    }

    public static byte[] getBlob(String columnName, Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex == -1) {
            return null;
        }
        return cursor.getBlob(columnIndex);
    }

    public static Date getDate(String columnName, Cursor cursor) {
        Long millis = getLong(columnName, cursor);
        if (millis > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            return calendar.getTime();
        } else {
            return null;
        }

    }

    public static boolean isEmpty(Cursor cursor) {
        return cursor == null || cursor.getCount() == 0;
    }

    public static boolean notEmpty(Cursor cursor) {
        return cursor != null && cursor.getCount() > 0;
    }

    public static void close(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    public static boolean isClosed(Cursor cursor) {
        return cursor == null || cursor.isClosed();
    }

    public static boolean getBoolean(String columnName, Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return columnIndex != -1 && cursor.getInt(columnIndex) == 1;
    }

    public static void convertToContentValues(Cursor cursor, List<ContentValues> list) {
        if (isEmpty(cursor)) {
            return;
        }
        cursor.moveToFirst();
        do {
            ContentValues contentValues = new ContentValues();
            list.add(contentValues);
        } while (cursor.moveToNext());
    }

    public static String getNotificationUri(Cursor cursor) {
        if (Platform.hasKK() && cursor != null) {
            if (cursor.getNotificationUri() != null) {
                return cursor.getNotificationUri().toString();
            }
        }
        return "";
    }

    public static void dump(Cursor cursor) {
        String uri = getNotificationUri(cursor);
        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                StringBuilder builder = new StringBuilder(">>>>> Dumping cursor for ");
                builder.append(uri).append("\n");
                do {
                    builder.append(cursor.getPosition() + 1).append(DIVIDER).append("\n");
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        String columnName = cursor.getColumnName(i);
                        String value = "";
                        int type = cursor.getType(i);
                        switch (type) {
                            case CursorUtils.FIELD_TYPE_BLOB:
                                value = ">>BLOB<<";
                                break;
                            case CursorUtils.FIELD_TYPE_INTEGER:
                                value = String.valueOf(cursor.getInt(i));
                                break;
                            case CursorUtils.FIELD_TYPE_FLOAT:
                                value = String.valueOf(cursor.getFloat(i));
                                break;
                            case CursorUtils.FIELD_TYPE_STRING:
                                value = cursor.getString(i);
                                break;
                            case CursorUtils.FIELD_TYPE_NULL:
                                value = "null";
                                break;
                        }
                        builder.append(columnName).append(" ---- ").append(value).append("\n");
                    }
                    builder.append(DIVIDER).append("\n");
                } while (cursor.moveToNext());
                Log.v("db", builder.toString());
            } else {

                Log.v("db", "cursor for " + uri + " is empty");
            }
        } else {
            Log.w("db", "cursor " + uri + " null or closed");
        }
    }

    public static String dumpToString(Cursor cursor) {
        String uri = getNotificationUri(cursor);
        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                StringBuilder builder = new StringBuilder(">>>>> Dumping cursor for ");
                builder.append(uri).append("\n");
                do {
                    builder.append(cursor.getPosition() + 1).append(DIVIDER).append("\n");
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        String columnName = cursor.getColumnName(i);
                        String value = "";
                        int type = cursor.getType(i);
                        switch (type) {
                            case CursorUtils.FIELD_TYPE_BLOB:
                                value = ">>BLOB<<";
                                break;
                            case CursorUtils.FIELD_TYPE_INTEGER:
                                value = String.valueOf(cursor.getInt(i));
                                break;
                            case CursorUtils.FIELD_TYPE_FLOAT:
                                value = String.valueOf(cursor.getFloat(i));
                                break;
                            case CursorUtils.FIELD_TYPE_STRING:
                                value = cursor.getString(i);
                                break;
                            case CursorUtils.FIELD_TYPE_NULL:
                                value = "null";
                                break;
                        }
                        builder.append(columnName).append(" ---- ").append(value).append("\n");
                    }
                    builder.append(DIVIDER).append("\n");
                } while (cursor.moveToNext());
                return builder.toString();
            } else {
                return "cursor for " + uri + " is empty";
            }
        } else {
            return "cursor " + uri + " null or closed";
        }
    }
}
