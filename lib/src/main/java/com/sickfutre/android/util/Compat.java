package com.sickfutre.android.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Menu;
import android.view.MenuItem;
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

    public static void tintMenu(Context context, Menu menu, @ColorRes int color) {
        for (int i = 0; i < menu.size(); ++i) {
            final MenuItem item = menu.getItem(i);
            final Drawable drawable = item.getIcon();
            item.setIcon(tintDrawable(context, color, drawable));
        }
    }


    public static Drawable tintDrawable(Context context, @ColorRes int color, Drawable drawable) {
        if (drawable != null) {
            int c = ContextCompat.getColor(context, color);
            final Drawable wrapped = DrawableCompat.wrap(drawable);
            drawable.mutate();
            DrawableCompat.setTint(wrapped, c);
        }
        return drawable;
    }

    public static Drawable tintDrawable(Context context, @ColorRes int color, @DrawableRes int drawableRes) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableRes);
        if (drawable != null) {
            int c = ContextCompat.getColor(context, color);
            final Drawable wrapped = DrawableCompat.wrap(drawable);
            drawable.mutate();
            DrawableCompat.setTint(wrapped, c);
        }
        return drawable;
    }
}
