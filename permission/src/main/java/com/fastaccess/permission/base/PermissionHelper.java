package com.fastaccess.permission.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.fastaccess.permission.base.callback.OnActivityPermissionCallback;
import com.fastaccess.permission.base.callback.OnPermissionCallback;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper implements OnActivityPermissionCallback {

    private OnPermissionCallback permissionCallback;
    private Activity context;
    private final int REQUEST_PERMISSIONS = 1;
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

    public static PermissionHelper getInstance(@NonNull Activity context) {
        return new PermissionHelper(context);
    }

    public static PermissionHelper getInstance(@NonNull Activity context, @NonNull OnPermissionCallback permissionCallback) {
        return new PermissionHelper(context, permissionCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (verifyPermissions(grantResults)) {
                permissionCallback.onPermissionGranted(permissions);
            } else {
                String[] declinedPermissions = declinedPermissions(context, permissions);
                List<Boolean> deniedPermissionsLength = new ArrayList<Boolean>();//needed
                for (String permissionName : declinedPermissions) {
                    if (permissionName != null) {
                        if (!isExplanationNeeded(permissionName)) {
                            permissionCallback.onPermissionReallyDeclined(permissionName);
                            deniedPermissionsLength.add(false);
                        }
                    }
                }
                if (deniedPermissionsLength.size() == 0) {
                    if (forceAccepting) {
                        requestAfterExplanation(declinedPermissions);
                    }
                    permissionCallback.onPermissionDeclined(declinedPermissions);
                }
            }
        }
    }

    /**
     * force the user to accept the permission. it won't work if the user ever thick-ed the "don't show again"
     */
    public PermissionHelper setForceAccepting(boolean forceAccepting) {
        this.forceAccepting = forceAccepting;
        return this;
    }

    /**
     * @param permissionName
     *         (it can be one of these types (String), (String[])
     */
    public PermissionHelper request(@NonNull Object permissionName) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
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
     * internal usage.
     */
    private void handleSingle(String permissionName) {
        if (isPermissionDeclined(permissionName)) {
            if (isExplanationNeeded(permissionName)) {
                permissionCallback.onPermissionNeedExplanation(permissionName);
            } else {
                ActivityCompat.requestPermissions(context, new String[]{permissionName}, REQUEST_PERMISSIONS);
            }
        } else {
            permissionCallback.onPermissionPreGranted(permissionName);
        }
    }

    /**
     * internal usage.
     */
    private void handleMulti(String[] permissionsName) {
        String[] permissions = declinedPermissions(context, permissionsName);
        if (permissions.length == 0) {
            permissionCallback.onPermissionGranted(permissionsName);
            return;
        }
        for (String permission : permissions) {
            if (permission != null) {
                handleSingle(permission);
            }
        }
    }

    /**
     * to be called when explanation is presented to the user
     */
    public void requestAfterExplanation(String permissionName) {
        if (isPermissionDeclined(permissionName)) {
            ActivityCompat.requestPermissions(context, new String[]{permissionName}, REQUEST_PERMISSIONS);
        } else {
            permissionCallback.onPermissionPreGranted(permissionName);
        }
    }

    /**
     * to be called when explanation is presented to the user
     */
    public void requestAfterExplanation(String[] permissions) {
        for (String permissionName : permissions) {
            if (isPermissionDeclined(permissionName)) {
                ActivityCompat.requestPermissions(context, new String[]{permissionName}, REQUEST_PERMISSIONS);
            } else {
                permissionCallback.onPermissionPreGranted(permissionName);
            }
        }
    }

    /**
     * return true if permission is declined, false otherwise.
     */
    public boolean isPermissionDeclined(String permissionsName) {
        return ActivityCompat.checkSelfPermission(context, permissionsName) != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * @return true if explanation needed.
     */
    public boolean isExplanationNeeded(@NonNull String permissionName) {
        return ActivityCompat.shouldShowRequestPermissionRationale(context, permissionName);
    }

    /**
     * internal usage.
     */
    private boolean verifyPermissions(int[] grantResults) {
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
     * be aware as it might return null (do check if the returned result is not null!)
     * <p/>
     * can be used outside of activity.
     */
    public static String declinedPermission(@NonNull Context context, @NonNull String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return permission;
            }
        }
        return null;
    }

    /**
     * @return list of permissions that the user declined or not yet granted.
     */
    public static String[] declinedPermissions(@NonNull Context context, @NonNull String[] permissions) {
        List<String> permissionsNeeded = new ArrayList<String>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
            }
        }
        return permissionsNeeded.toArray(new String[permissionsNeeded.size()]);
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
     * open android settings screen for your app.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void openSettingsScreen(@NonNull Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + context.getPackageName());
        intent.setData(uri);
        context.startActivity(intent);
    }

}
