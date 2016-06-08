package com.sickfutre.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ImagePicker {

    private static final int DEFAULT_MIN_WIDTH_QUALITY = 400;        // min pixels
    private static final String TAG = "ImagePicker";
    private static final String TEMP_IMAGE_NAME = "tempImage";
    public static final int REQUEST_CODE_IMAGE = 4321;

    public static int maxImageSize = DEFAULT_MIN_WIDTH_QUALITY;


    public static Intent getPickImageIntent(Context context, String dialogTitle) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(context)));
        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1), dialogTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[intentList.size()]));
        }

        return chooserIntent;
    }

    public static void startImagePicker(Fragment context, String dialogTitle) {
        Intent pickImageIntent = getPickImageIntent(context.getActivity(), dialogTitle);
        context.startActivityForResult(pickImageIntent, REQUEST_CODE_IMAGE);
    }

    @Deprecated
    public static ImageResult getImageFromResult(Context context, int requestCode, int resultCode, Intent imageReturnedIntent, Properties properties) {
        if (requestCode == REQUEST_CODE_IMAGE) {
            return getImageFromResult(context, resultCode, imageReturnedIntent, properties);
        } else {
            return null;
        }
    }

    public static boolean getImageFromResultAsync(Context context, int requestCode, int resultCode, Intent imageReturnedIntent,
                                                  Properties properties, @NonNull Procedure<ImageResult> onSelected, @NonNull Procedure<ImageResult> onComplete) {
        if (requestCode == REQUEST_CODE_IMAGE) {
            getImageFromResultAsync(context, resultCode, imageReturnedIntent, properties, onSelected, onComplete);
            return true;
        } else {
            return false;
        }
    }

    public static void getImageFromResultAsync(final Context context, int resultCode, Intent imageReturnedIntent,
                                               Properties properties, Procedure<ImageResult> onSelected, @NonNull final Procedure<ImageResult> onComplete) {
        if (resultCode == Activity.RESULT_OK) {

            final Uri selectedImage;
            final File file;
            int minSize = -1, maxSize = -1;
            if (properties != null) {
                file = properties.destinationFile;
                minSize = properties.minSize;
                maxSize = properties.maxSize;
            } else {
                file = Files.getTempFile(context);
            }
            final boolean isCamera = (imageReturnedIntent == null ||
                    imageReturnedIntent.getData() == null ||
                    imageReturnedIntent.getData().toString().contains(file.toString()));
            if (isCamera) {     /** CAMERA **/
                selectedImage = Uri.fromFile(file);
            } else {            /** ALBUM **/
                selectedImage = imageReturnedIntent.getData();
            }
            Log.d(TAG, "selectedImage: " + selectedImage);

            final int finalMinSize = minSize;
            final int finalMaxSize = maxSize;
            if (onSelected != null) {
                onSelected.apply(new ImageResult(null, file));
            }
            new AsyncTask<Void, Void, ImageResult>() {
                @Override
                protected ImageResult doInBackground(Void... params) {
                    Bitmap bm = getImageResized(context, selectedImage, finalMinSize, finalMaxSize);
                    int rotation = getRotation(context, selectedImage, isCamera);
                    bm = rotate(bm, rotation);
                    Files.writeBitmapToFile(file, bm);
                    return new ImageResult(bm, file);
                }

                @Override
                protected void onPostExecute(ImageResult imageResult) {
                    onComplete.apply(imageResult);
                }
            }.execute();
        }
    }

    @Deprecated
    public static ImageResult getImageFromResult(Context context, int resultCode, Intent imageReturnedIntent, Properties properties) {
        Log.d(TAG, "getImageFromResult, resultCode: " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage;
            File file;
            int minSize = -1, maxSize = -1;
            if (properties != null) {
                file = properties.destinationFile;
                minSize = properties.minSize;
                maxSize = properties.maxSize;
            } else {
                file = getTempFile(context);
            }
            boolean isCamera = (imageReturnedIntent == null ||
                    imageReturnedIntent.getData() == null ||
                    imageReturnedIntent.getData().toString().contains(file.toString()));
            if (isCamera) {     /** CAMERA **/
                selectedImage = Uri.fromFile(file);
            } else {            /** ALBUM **/
                selectedImage = imageReturnedIntent.getData();
            }
            Log.d(TAG, "selectedImage: " + selectedImage);

            Bitmap bm = getImageResized(context, selectedImage, minSize, maxSize);
            int rotation = getRotation(context, selectedImage, isCamera);
            bm = rotate(bm, rotation);
            Files.writeBitmapToFileAsync(file, bm, null);
            return new ImageResult(bm, file);
        }
        return null;
    }


    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
            Log.d(TAG, "Intent: " + intent.getAction() + " package: " + packageName);
        }
        return list;
    }

    public static class ImageResult {
        public final Bitmap bitmap;
        public final File file;

        public ImageResult(Bitmap bitmap, File file) {
            this.bitmap = bitmap;
            this.file = file;
        }
    }

    public static class Properties {
        File destinationFile;
        int minSize = 300, maxSize = 1000;

        public Properties(File destinationFile) {
            this.destinationFile = destinationFile;
        }

        public Properties minSize(int minSize) {
            this.minSize = minSize;
            return this;
        }

        public Properties maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }
    }

    @Deprecated
    private static File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }

    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        try {
            AssetFileDescriptor fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
            Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                    fileDescriptor.getFileDescriptor(), null, options);
            Log.d(TAG, options.inSampleSize + " sample method bitmap ... " +
                    actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());

            return actuallyUsableBitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Resize to avoid using too much memory loading big images (e.g.: 2560*1920)
     **/
    private static Bitmap getImageResized(Context context, Uri selectedImage, int minSize, int maxSize) {
        Bitmap bm = decodeBitmap(context, selectedImage, 1);
        if (bm != null && (minSize > 0 && maxSize > 0)) {
            int height = bm.getHeight();
            int width = bm.getWidth();
            boolean byWidth = width > height;
            int measuredDimension = byWidth ? width : height;
            double aspectRatio = byWidth ? (double) height / width : (double) width / height;
            if (measuredDimension < minSize) {
                int newWidth = byWidth ? minSize : (int) (aspectRatio * minSize);
                int newHeight = !byWidth ? minSize : (int) (aspectRatio * minSize);
                bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            } else if (measuredDimension > maxSize) {
                int newWidth = byWidth ? maxSize : (int) (aspectRatio * maxSize);
                int newHeight = !byWidth ? maxSize : (int) (aspectRatio * maxSize);
                bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            }
            Log.d(TAG, "getImageResized: w: " + bm.getWidth() + " h: " + bm.getHeight());
        }
        return bm;
    }


    private static int getRotation(Context context, Uri imageUri, boolean isCamera) {
        int rotation;
        if (isCamera) {
            rotation = getRotationFromCamera(context, imageUri);
        } else {
            rotation = getRotationFromGallery(context, imageUri);
        }
        Log.d(TAG, "Image rotation: " + rotation);
        return rotation;
    }

    private static int getRotationFromCamera(Context context, Uri imageFile) {
        int rotate = 0;
        try {

            context.getContentResolver().notifyChange(imageFile, null);
            ExifInterface exif = new ExifInterface(imageFile.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    private static int getRotationFromGallery(Context context, Uri imageUri) {
        int result = 0;
        String[] columns = {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int orientationColumnIndex = cursor.getColumnIndex(columns[0]);
                result = cursor.getInt(orientationColumnIndex);
            }
        } catch (Exception e) {
            //Do nothing
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }


    private static Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        }
        return bm;
    }
}
