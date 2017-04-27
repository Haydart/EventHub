package pl.rmakowiecki.eventhub.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public final class UnitConversionUtils {

    private UnitConversionUtils() {
    }

    public static float convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
