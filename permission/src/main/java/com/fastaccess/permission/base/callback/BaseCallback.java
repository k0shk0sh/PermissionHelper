package com.fastaccess.permission.base.callback;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;


public interface BaseCallback {

    void onSkip(boolean isSkipped, String permissionName);

    void onStatusBarColorChange(@ColorInt int color);

    void onPermissionRequest(@NonNull String permission);

    void onNext(boolean skipped, @NonNull String permissionName);
}
