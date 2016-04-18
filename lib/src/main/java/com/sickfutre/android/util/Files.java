package com.sickfutre.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Files {

    public static boolean writeBitmapToFile(final File tempFile, final Bitmap bitmap) {
        boolean result = true;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return result;
    }

    public static void writeBitmapToFileAsync(final File file, final Bitmap bitmap, final Procedure<Boolean> onComplete) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return writeBitmapToFile(file, bitmap);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (onComplete != null) {
                    onComplete.apply(result);
                }
            }
        }.execute();
    }

    public static File getTempFile(Context context) {
        File storageDir;
        File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
        if (externalCacheDirs.length > 0) {
            storageDir = externalCacheDirs[0];
        } else {
            storageDir = context.getCacheDir();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TMP_" + timeStamp;
        File file = null;
        try {
            file = File.createTempFile(
                    imageFileName,  /* prefix */
                    null,         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
