package pl.rmakowiecki.eventhub.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import pl.rmakowiecki.eventhub.background.Constants;

public final class BitmapUtils {

    private BitmapUtils() {}

    public static BitmapDescriptor createBitmapDescriptor(Context context, int resourceId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, resourceId);
        int width = vectorDrawable.getIntrinsicWidth();
        int height = vectorDrawable.getIntrinsicHeight();
        vectorDrawable.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static Bitmap cropAndScaleBitmapFromCamera(Intent dataIntent) {
        Bitmap resultBitmap;
        Bundle bundle = dataIntent.getExtras();
        resultBitmap = BitmapUtils.createSquareProfilePicBitmap((Bitmap) bundle.get(Constants.PROFILE_PIC_BUNDLE_NAME));
        return BitmapUtils.scaleBitmap(resultBitmap, Constants.PROFILE_PIC_BITMAP_WIDTH, Constants.PROFILE_PIC_BITMAP_HEIGHT);
    }

    public static Bitmap cropAndScaleBitmapFromGallery(Intent dataIntent, Context context) {
        try {
            Bitmap resultBitmap;
            InputStream optionsInputStream = context.getContentResolver().openInputStream(dataIntent.getData());
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(optionsInputStream, null, options);
            options.inSampleSize = calculateInSampleSize(options, Constants.PROFILE_PIC_BITMAP_WIDTH, Constants.PROFILE_PIC_BITMAP_HEIGHT);
            options.inJustDecodeBounds = false;
            InputStream bitmapInputStream = context.getContentResolver().openInputStream(dataIntent.getData());
            resultBitmap = BitmapFactory.decodeStream(bitmapInputStream, null, options);
            return BitmapUtils.createSquareProfilePicBitmap(resultBitmap);
        } catch (IOException ex) {
            return null;
        }
    }

    public static byte[] createCompressedJpegByteArray(Bitmap bitmap, int compressionQuality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, stream);
        return stream.toByteArray();
    }

    public static Bitmap createSquareProfilePicBitmap(Bitmap sourceBitmap) {
        if (isLandscapePicture(sourceBitmap)) {
            return cutBitmapFromLandscape(sourceBitmap);
        } else {
            return cutBitmapFromPortrait(sourceBitmap);
        }
    }

    public static Bitmap scaleBitmap(Bitmap sourceBitmap, int destinationWidth, int destinationHeight) {
        return Bitmap.createScaledBitmap(sourceBitmap, destinationWidth, destinationHeight, false);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int requiredWidth, int requiredHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > requiredHeight || width > requiredWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= requiredHeight
                    && (halfWidth / inSampleSize) >= requiredWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private static Bitmap cutBitmapFromLandscape(Bitmap sourceBitmap) {
        return Bitmap.createBitmap(sourceBitmap,
                calculateCutLandscapeXStart(sourceBitmap), 0,
                sourceBitmap.getHeight(), sourceBitmap.getHeight());
    }

    private static int calculateCutLandscapeXStart(Bitmap sourceBitmap) {
        return sourceBitmap.getWidth() / 2 - sourceBitmap.getHeight() / 2;
    }

    private static Bitmap cutBitmapFromPortrait(Bitmap sourceBitmap) {
        return Bitmap.createBitmap(sourceBitmap,
                0, calculateCutPortraitYStart(sourceBitmap),
                sourceBitmap.getWidth(), sourceBitmap.getWidth());
    }

    private static int calculateCutPortraitYStart(Bitmap sourceBitmap) {
        return sourceBitmap.getHeight() / 2 - sourceBitmap.getWidth() / 2;
    }

    private static boolean isLandscapePicture(Bitmap sourceBitmap) {
        return sourceBitmap.getWidth() >= sourceBitmap.getHeight();
    }
}
