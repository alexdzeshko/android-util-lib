package com.sickfutre.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Alex Dzeshko on 14-Jun-16.
 */
public class Intents {

    public static boolean call(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        return dispatchIntentExternal(context, intent);
    }

    public static void email(Context context, String emailAddress) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("mailto:" + emailAddress));
        dispatchIntentExternal(context, intent);
    }

    public static void web(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        dispatchIntentExternal(context, intent);
    }

    public static void rateApp(Context context) {
        String packageName = context.getPackageName();
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);

        if (queryIntents(context, rateAppIntent)) {
            context.startActivity(rateAppIntent);
        } else {
            web(context, "http://play.google.com/store/apps/details?id=" + packageName);
        }
    }

    public static void takePhoto(Activity context, File imageFile, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageFile != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            dispatchIntentExternalForResult(context, takePictureIntent, requestCode);
        }
    }

    private static boolean dispatchIntentExternal(Context context, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (queryIntents(context, intent)) {
            context.startActivity(intent);
            return true;
        } else {
            Toast.makeText(context, "No such feature", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private static boolean dispatchIntentExternalForResult(Activity context, Intent intent, int requestCode) {
        if (queryIntents(context, intent)) {
            context.startActivityForResult(intent, requestCode);
            return true;
        } else {
            Toast.makeText(context, "No such feature", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private static boolean queryIntents(Context context, Intent rateAppIntent) {
        return context.getPackageManager().queryIntentActivities(rateAppIntent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    public static void calendarEvent(Context context, long startMillis, long endMillis, String title, String description, String location) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, description)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location);
        dispatchIntentExternal(context, intent);
    }
}
