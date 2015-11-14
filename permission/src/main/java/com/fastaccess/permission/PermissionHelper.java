package com.fastaccess.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper implements OnActivityPermissionCallback {

    private OnPermissionCallback permissionCallback;
    private Activity context;
    private final int REQUEST_PERMISSIONS = 1;
    private boolean forceAccepting;

    /**
     * @param context
     *         (Activity context -MUST BE ACTIVITY-)
     * @param permissionCallback
     *         (that returns the results to you)
     */
    public PermissionHelper(@NonNull Activity context, @NonNull OnPermissionCallback permissionCallback) {
        this.context = context;
        this.permissionCallback = permissionCallback;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (verifyPermissions(grantResults)) {
                permissionCallback.onPermissionGranted(permissions);
            } else {
                if (forceAccepting) {
                    requestAfterExplanation(declinedPermissions(context, permissions));
                }
                permissionCallback.onPermissionDeclined(permissions);
            }
        }
    }

    /**
     * internal usage.
     */
    private void handleSingle(String permissionName) {
        if (isPermissionDeclined(permissionName)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permissionName)) {
                permissionCallback.onPermissionNeedExplanation(permissionName);
            } else {
                ActivityCompat.requestPermissions(context, new String[]{permissionName}, REQUEST_PERMISSIONS);
            }
        } else {
            permissionCallback.onPermissionPreGranted(permissionName);
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
     * internal usage.
     */
    private void handleMulti(String[] permissionsName) {
        for (String permission : permissionsName) {
            if (permission != null) handleSingle(permission);
        }
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

}
