package com.fastaccess.permission.base.utils;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.TypedValue;

import com.fastaccess.permission.R;

/**
 * Created by Kosh on 20/11/15 10:58 PM. copyrights @ Innov8tif
 */
public class ThemeUtil {

    private ThemeUtil() {}

    @ColorInt public static int getPrimaryColor(Context context) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    @ColorInt public static int getThemeAccentColor(Context context) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

}
