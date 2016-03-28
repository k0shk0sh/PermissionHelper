package com.fastaccess.permission.sample;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;

import java.util.Arrays;

public class SampleActivity extends AppCompatActivity implements OnPermissionCallback, View.OnClickListener {

    private final String TAG = SampleActivity.class.getName();

    private TextView result;
    private PermissionHelper permissionHelper;
    private boolean isSingle;
    private AlertDialog builder;
    private String[] neededPermission;

    private final String SINGLE_PERMISSION = Manifest.permission.GET_ACCOUNTS;


    private final String[] MULTI_PERMISSIONS = new String[]{
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        result = (TextView) findViewById(R.id.result);
        findViewById(R.id.single).setOnClickListener(this);
        findViewById(R.id.multi).setOnClickListener(this);
        findViewById(R.id.overlay).setOnClickListener(this);
        permissionHelper = PermissionHelper.getInstance(this);
    }

    /**
     * Used to determine if the user accepted {@link android.Manifest.permission#SYSTEM_ALERT_WINDOW} or no.
     * <p/>
     * if you never passed the permission this method won't be called.
     */
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        permissionHelper.onActivityForResult(requestCode);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override public void onPermissionGranted(@NonNull String[] permissionName) {
        result.setText("Permission(s) " + Arrays.toString(permissionName) + " Granted");
        Log.i("onPermissionGranted", "Permission(s) " + Arrays.toString(permissionName) + " Granted");
    }

    @Override public void onPermissionDeclined(@NonNull String[] permissionName) {
        result.setText("Permission(s) " + Arrays.toString(permissionName) + " Declined");
        Log.i("onPermissionDeclined", "Permission(s) " + Arrays.toString(permissionName) + " Declined");
    }

    @Override public void onPermissionPreGranted(@NonNull String permissionsName) {
        result.setText("Permission( " + permissionsName + " ) preGranted");
        Log.i("onPermissionPreGranted", "Permission( " + permissionsName + " ) preGranted");
    }

    @Override public void onPermissionNeedExplanation(@NonNull String permissionName) {
        Log.i("NeedExplanation", "Permission( " + permissionName + " ) needs Explanation");
        if (!isSingle) {
            neededPermission = PermissionHelper.declinedPermissions(this, MULTI_PERMISSIONS);
            StringBuilder builder = new StringBuilder(neededPermission.length);
            if (neededPermission.length > 0) {
                for (String permission : neededPermission) {
                    builder.append(permission).append("\n");
                }
            }
            result.setText("Permission( " + builder.toString() + " ) needs Explanation");
            AlertDialog alert = getAlertDialog(neededPermission, builder.toString());
            if (!alert.isShowing()) {
                alert.show();
            }
        } else {
            result.setText("Permission( " + permissionName + " ) needs Explanation");
            getAlertDialog(permissionName).show();
        }
    }

    @Override public void onPermissionReallyDeclined(@NonNull String permissionName) {
        result.setText("Permission " + permissionName + " can only be granted from SettingsScreen");
        Log.i("ReallyDeclined", "Permission " + permissionName + " can only be granted from settingsScreen");
        /** you can call  {@link PermissionHelper#openSettingsScreen(Context)} to open the settings screen */
    }

    @Override public void onNoPermissionNeeded() {
        result.setText("Permission(s) not needed");
        Log.i("onNoPermissionNeeded", "Permission(s) not needed");
    }

    @Override public void onClick(View v) {
        if (v.getId() == R.id.single || v.getId() == R.id.multi) {
            isSingle = v.getId() == R.id.single;
            permissionHelper
                    .setForceAccepting(false) // default is false. its here so you know that it exists.
                    .request(isSingle ? SINGLE_PERMISSION : MULTI_PERMISSIONS);
        } else {
            permissionHelper
                    .request(Manifest.permission.SYSTEM_ALERT_WINDOW);/*you can pass it along other permissions,
                     just make sure you override OnActivityResult so you can get a callback.
                     ignoring that will result to not be notified if the user enable/disable the permission*/
        }
    }

    public AlertDialog getAlertDialog(final String[] permissions, final String permissionName) {
        if (builder == null) {
            builder = new AlertDialog.Builder(this)
                    .setTitle("Permission Needs Explanation").create();
        }
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionHelper.requestAfterExplanation(permissions);
            }
        });
        builder.setMessage("Permissions need explanation (" + permissionName + ")");
        return builder;
    }

    public AlertDialog getAlertDialog(final String permission) {
        if (builder == null) {
            builder = new AlertDialog.Builder(this)
                    .setTitle("Permission Needs Explanation").create();
        }
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionHelper.requestAfterExplanation(permission);
            }
        });
        builder.setMessage("Permission need explanation (" + permission + ")");
        return builder;
    }
}
