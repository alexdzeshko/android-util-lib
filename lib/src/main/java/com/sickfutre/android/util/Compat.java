package com.sickfutre.android.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Created by Alex Dzeshko on 04-Apr-16.
 */
public class Compat {

    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
