package com.fastaccess.permission.base.callback;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Created by Kosh on 16/11/15 10:28 PM. copyrights @ Innov8tif
 */
public interface BaseCallback {

    void onSkip(boolean isSkipped, String permissionName);

    void onStatusBarColorChange(@ColorInt int color);

    void onPermissionRequest(@NonNull String permission);
}
