package com.fastaccess.permission.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.fastaccess.permission.base.callback.OnActivityPermissionCallback;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.fastaccess.permission.base.model.PermissionModel;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper implements OnActivityPermissionCallback {
    private static final int OVERLAY_PERMISSION_REQ_CODE = 2;
    private static final int REQUEST_PERMISSIONS = 1;

    @NonNull private final OnPermissionCallback permissionCallback;
    @NonNull private final Activity context;
    private boolean forceAccepting;

    private PermissionHelper(@NonNull Activity context) {
        this.context = context;
        if (context instanceof OnPermissionCallback) {
            this.permissionCallback = (OnPermissionCallback) context;
        } else {
            throw new IllegalArgumentException("Activity must implement (OnPermissionCallback)");
        }
    }

    private PermissionHelper(@NonNull Activity context, @NonNull OnPermissionCallback permissionCallback) {
        this.context = context;
        this.permissionCallback = permissionCallback;
    }

    @NonNull public static PermissionHelper getInstance(@NonNull Activity context) {
        return new PermissionHelper(context);
    }

    @NonNull public static PermissionHelper getInstance(@NonNull Activity context, @NonNull OnPermissionCallback permissionCallback) {
        return new PermissionHelper(context, permissionCallback);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (verifyPermissions(grantResults)) {
                permissionCallback.onPermissionGranted(permissions);
            } else {
                String[] declinedPermissions = declinedPermissions(context, permissions);
                List<Boolean> deniedPermissionsLength = new ArrayList<>();//needed
                for (String permissionName : declinedPermissions) {
                    if (permissionName != null && !isExplanationNeeded(permissionName)) {
                        permissionCallback.onPermissionReallyDeclined(permissionName);
                        deniedPermissionsLength.add(false);
                    }
                }
                if (deniedPermissionsLength.size() == 0) {
                    if (forceAccepting) {
                        requestAfterExplanation(declinedPermissions);
                        return;
                    }
                    permissionCallback.onPermissionDeclined(declinedPermissions);
                }
            }
        }
    }

    /**
     * used only for {@link android.Manifest.permission#SYSTEM_ALERT_WINDOW}
     */
    @Override public void onActivityForResult(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
                if (isSystemAlertGranted()) {
                    permissionCallback.onPermissionGranted(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW});
                } else {
                    permissionCallback.onPermissionDeclined(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW});
                }
            }
        } else {
            permissionCallback.onPermissionPreGranted(Manifest.permission.SYSTEM_ALERT_WINDOW);
        }
    }

    /**
     * force the user to accept the permission. it won't work if the user ever thick-ed the "don't show again"
     */
    @NonNull public PermissionHelper setForceAccepting(boolean forceAccepting) {
        this.forceAccepting = forceAccepting;
        return this;
    }

    /**
     * @param permissionName
     *         (it can be one of these types (String), (String[])
     */
    @NonNull public PermissionHelper request(@NonNull Object permissionName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionName instanceof String) {
                handleSingle((String) permissionName);
            } else if (permissionName instanceof String[]) {
                handleMulti((String[]) permissionName);
            } else {
                throw new IllegalArgumentException("Permissions can only be one of these types (String) or (String[])" +
                        ". given type is " + permissionName.getClass().getSimpleName());
            }
        } else {
            permissionCallback.onNoPermissionNeeded();
        }
        return this;
    }

    /**
     * used only for {@link android.Manifest.permission#SYSTEM_ALERT_WINDOW}
     */
    public void requestSystemAlertPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (!isSystemAlertGranted()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
                    context.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                } else {
                    permissionCallback.onPermissionPreGranted(Manifest.permission.SYSTEM_ALERT_WINDOW);
                }
            } catch (Exception ignored) {}
        } else {
            permissionCallback.onPermissionPreGranted(Manifest.permission.SYSTEM_ALERT_WINDOW);
        }
    }

    /**
     * internal usage.
     */
    private void handleSingle(@NonNull String permissionName) {
        if (permissionExists(permissionName)) {// android M throws exception when requesting
            // run time permission that does not exists in AndroidManifest.
            if (!permissionName.equalsIgnoreCase(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                if (isPermissionDeclined(permissionName)) {
                    if (isExplanationNeeded(permissionName)) {
                        permissionCallback.onPermissionNeedExplanation(permissionName);
                    } else {
                        ActivityCompat.requestPermissions(context, new String[]{permissionName}, REQUEST_PERMISSIONS);
                    }
                } else {
                    permissionCallback.onPermissionPreGranted(permissionName);
                }
            } else {
                requestSystemAlertPermission();
            }
        } else {
            permissionCallback.onPermissionDeclined(new String[]{permissionName});
        }
    }

    /**
     * internal usage.
     */
    private void handleMulti(@NonNull String[] permissionNames) {
        List<String> permissions = declinedPermissionsAsList(context, permissionNames);
        if (permissions.isEmpty()) {
            permissionCallback.onPermissionGranted(permissionNames);
            return;
        }
        boolean hasAlertWindowPermission = permissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW);
        if (hasAlertWindowPermission) {
            int index = permissions.indexOf(Manifest.permission.SYSTEM_ALERT_WINDOW);
            permissions.remove(index);
        }
        ActivityCompat.requestPermissions(context, permissions.toArray(new String[permissions.size()]), REQUEST_PERMISSIONS);
    }

    /**
     * to be called when explanation is presented to the user
     */
    public void requestAfterExplanation(@NonNull String permissionName) {
        if (isPermissionDeclined(permissionName)) {
            ActivityCompat.requestPermissions(context, new String[]{permissionName}, REQUEST_PERMISSIONS);
        } else {
            permissionCallback.onPermissionPreGranted(permissionName);
        }
    }

    /**
     * to be called when explanation is presented to the user
     */
    public void requestAfterExplanation(@NonNull String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permissionName : permissions) {
            if (isPermissionDeclined(permissionName)) {
                permissionsToRequest.add(permissionName); // add permission to request
            } else {
                permissionCallback.onPermissionPreGranted(permissionName); // do not request, since it is already granted
            }
        }
        if (permissionsToRequest.isEmpty()) return;
        permissions = permissionsToRequest.toArray(new String[permissionsToRequest.size()]);
        ActivityCompat.requestPermissions(context, permissions, REQUEST_PERMISSIONS);
    }

    /**
     * return true if permission is declined, false otherwise.
     */
    public boolean isPermissionDeclined(@NonNull String permissionsName) {
        return ActivityCompat.checkSelfPermission(context, permissionsName) != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * return true if permission is granted, false otherwise.
     */
    public boolean isPermissionGranted(@NonNull String permissionsName) {
        return ActivityCompat.checkSelfPermission(context, permissionsName) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * @return true if explanation needed.
     */
    public boolean isExplanationNeeded(@NonNull String permissionName) {
        return ActivityCompat.shouldShowRequestPermissionRationale(context, permissionName);
    }

    /**
     * @return true if the permission is patently denied by the user and only can be granted via settings Screen
     * <p/>
     * consider using {@link PermissionHelper#openSettingsScreen(Context)} to open settings screen
     */
    public boolean isPermissionPermanentlyDenied(@NonNull String permission) {
        return isPermissionDeclined(permission) && !isExplanationNeeded(permission);
    }

    /**
     * internal usage.
     */
    private boolean verifyPermissions(@NonNull int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if permission exists in the manifest, false otherwise.
     */
    public boolean permissionExists(@NonNull String permissionName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (packageInfo.requestedPermissions != null) {
                for (String p : packageInfo.requestedPermissions) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return true if {@link Manifest.permission#SYSTEM_ALERT_WINDOW} is granted
     */
    public boolean isSystemAlertGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }

    /**
     * open android settings screen for the specific package name
     */
    public void openSettingsScreen() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + context.getPackageName());
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * be aware as it might return null (do check if the returned result is not null!)
     * <p/>
     * can be used outside of activity.
     */
    @Nullable public static String declinedPermission(@NonNull Context context, @NonNull String[] permissions) {
        for (String permission : permissions) {
            if (isPermissionDeclined(context, permission)) {
                return permission;
            }
        }
        return null;
    }

    /**
     * @return list of permissions that the user declined or not yet granted.
     */
    public static String[] declinedPermissions(@NonNull Context context, @NonNull String[] permissions) {
        List<String> permissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (isPermissionDeclined(context, permission) && permissionExists(context, permission)) {
                permissionsNeeded.add(permission);
            }
        }
        return permissionsNeeded.toArray(new String[permissionsNeeded.size()]);
    }

    public static List<String> declinedPermissionsAsList(@NonNull Context context, @NonNull String[] permissions) {
        List<String> permissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (isPermissionDeclined(context, permission) && permissionExists(context, permission)) {
                permissionsNeeded.add(permission);
            }
        }
        return permissionsNeeded;
    }

    /**
     * return true if permission is granted, false otherwise.
     * <p/>
     * can be used outside of activity.
     */
    public static boolean isPermissionGranted(@NonNull Context context, @NonNull String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * return true if permission is declined, false otherwise.
     * <p/>
     * can be used outside of activity.
     */
    public static boolean isPermissionDeclined(@NonNull Context context, @NonNull String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * @return true if explanation needed.
     */
    public static boolean isExplanationNeeded(@NonNull Activity context, @NonNull String permissionName) {
        return ActivityCompat.shouldShowRequestPermissionRationale(context, permissionName);
    }

    /**
     * @return true if the permission is patently denied by the user and only can be granted via settings Screen
     * <p/>
     * consider using {@link PermissionHelper#openSettingsScreen(Context)} to open settings screen
     */
    public static boolean isPermissionPermanentlyDenied(@NonNull Activity context, @NonNull String permission) {
        return isPermissionDeclined(context, permission) && !isExplanationNeeded(context, permission);
    }

    /**
     * open android settings screen for your app.
     */
    public static void openSettingsScreen(@NonNull Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + context.getPackageName());
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * @return true if permission exists in the manifest, false otherwise.
     */
    public static boolean permissionExists(@NonNull Context context, @NonNull String permissionName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (packageInfo.requestedPermissions != null) {
                for (String p : packageInfo.requestedPermissions) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return true if {@link android.Manifest.permission#SYSTEM_ALERT_WINDOW} is granted
     */
    public static boolean isSystemAlertGranted(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }

    public static void removeGrantedPermissions(@NonNull Context context, @NonNull List<PermissionModel> models) {
        List<PermissionModel> granted = new ArrayList<>();
        for (PermissionModel permissionModel : models) {
            if (permissionModel.getPermissionName().equalsIgnoreCase(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                if (isSystemAlertGranted(context)) {
                    granted.add(permissionModel);
                }
            } else if (isPermissionGranted(context, permissionModel.getPermissionName())) {
                granted.add(permissionModel);
            }
        }
        if (!granted.isEmpty()) {
            models.removeAll(granted);
        }
    }
}
