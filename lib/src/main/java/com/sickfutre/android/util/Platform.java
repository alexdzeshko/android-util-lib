package com.sickfutre.android.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

public class Platform {

    /**
     * The default prefix used to resolve layout resources ids.
     */
    public final static String RESOURCE_TYPE_LAYOUT = "layout";

    /**
     * The default prefix used to resolve drawable resources ids.
     */
    public final static String RESOURCE_TYPE_DRAWABLE = "drawable";

    /**
     * The default prefix used to resolve view resources ids.
     */
    public final static String RESOURCE_TYPE_ID = "id";

    /**
     * The default prefix used to resolve menu resources ids.
     */
    public static final String RESOURCE_TYPE_MENU = "menu";

    /**
     * Gets application meta value by name.
     *
     * @param key      meta name
     * @param defValue default value
     * @return meta valye ir defValue if not found.
     */
    public static String meta(Context context, String packageName, String key, String defValue) {
        String result = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            result = bundle == null ? null : bundle.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            //STAConst.Log.e(TAG, "Failed to load meta-data, NameNotFound: " + key, e);
        } catch (NullPointerException e) {
            //STAConst.Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage(), e);
        }
        return result == null ? defValue : result;
    }


    public static boolean hasKK() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasJB43() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasJB() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJB42() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
}
