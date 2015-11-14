package com.fastaccess.permission;

public interface OnPermissionCallback {

    void onPermissionGranted(String[] permissionName);

    void onPermissionDeclined(String[] permissionName);

    void onPermissionPreGranted(String permissionsName);

    void onPermissionNeedExplanation(String permissionName);

    void onNoPermissionNeeded();
}
