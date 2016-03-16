package com.sickfutre.android.util;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Arrays;

public class Logger {

    /**
     * @see <a href="http://stackoverflow.com/a/8899735" />
     */
    private static final int ENTRY_MAX_LEN = 4000;

    /**
     * @param args If the last argument is an exception than it prints out the stack trace, and there should be no {}
     *             or %s placeholder for it.
     */
    public static void d(String tag, @NonNull String message, Object... args) {
        log(tag, Log.DEBUG, false, message, args);
    }

    /**
     * Display the entire message, showing multiple lines if there are over 4000 characters rather than truncating it.
     */
    public static void debugEntire(String tag, @NonNull String message, Object... args) {
        log(tag, Log.DEBUG, true, message, args);
    }

    public static void i(String tag, @NonNull String message, Object... args) {
        log(tag, Log.INFO, false, message, args);
    }

    public static void w(String tag, @NonNull String message, Object... args) {
        log(tag, Log.WARN, false, message, args);
    }

    public static void e(String tag, @NonNull String message, Object... args) {
        log(tag, Log.ERROR, false, message, args);
        //Log.getStackTraceString(ex); // TODO: 15-Feb-16 use this with throwable param
    }

    private static void log(String tag, int priority, boolean ignoreLimit, String message, Object... args) {
        String print;
        if (args != null && args.length > 0) {
            try {
                print = String.format(message, args);
            } catch (Exception e) {
                print = message + ":" + Arrays.toString(args);
            }
        } else {
            print = message;
        }
        if (ignoreLimit) {
            while (!print.isEmpty()) {
                int nextEnd = Math.min(ENTRY_MAX_LEN, print.length());
                String next = print.substring(0, nextEnd /*exclusive*/);
                Log.println(priority, tag, next);
                print = print.substring(nextEnd);
            }
        } else {
            Log.println(priority, tag, print);
        }
    }

}
