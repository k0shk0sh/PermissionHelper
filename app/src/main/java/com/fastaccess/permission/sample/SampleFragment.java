package com.fastaccess.permission.sample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fastaccess.permission.base.PermissionFragmentHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;

import java.util.Arrays;

/**
 * Created by Kosh on 17 Oct 2016, 9:20 PM
 */

public class SampleFragment extends Fragment implements View.OnClickListener, OnPermissionCallback {

    private final static String SINGLE_PERMISSION = Manifest.permission.GET_ACCOUNTS;


    private final static String[] MULTI_PERMISSIONS = new String[]{
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private TextView result;
    private PermissionFragmentHelper permissionFragmentHelper;
    private boolean isSingle;
    private AlertDialog builder;
    private String[] neededPermission;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionFragmentHelper = PermissionFragmentHelper.getInstance(this);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sample, container, false);
        view.findViewById(R.id.useFragment).setVisibility(View.GONE);
        result = (TextView) view.findViewById(R.id.result);
        view.findViewById(R.id.single).setOnClickListener(this);
        view.findViewById(R.id.multi).setOnClickListener(this);
        view.findViewById(R.id.overlay).setOnClickListener(this);
        return view;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        permissionFragmentHelper.onActivityForResult(requestCode);
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionFragmentHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            neededPermission = PermissionFragmentHelper.declinedPermissions(this, MULTI_PERMISSIONS);
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
        /** you can call  {@link PermissionFragmentHelper#openSettingsScreen(Fragment)} to open the settings screen */
    }

    @Override public void onNoPermissionNeeded() {
        result.setText("Permission(s) not needed");
        Log.i("onNoPermissionNeeded", "Permission(s) not needed");
    }

    @Override public void onClick(View v) {
        if (v.getId() == R.id.single || v.getId() == R.id.multi) {
            isSingle = v.getId() == R.id.single;
            permissionFragmentHelper
                    .setForceAccepting(false) // default is false. its here so you know that it exists.
                    .request(isSingle ? SINGLE_PERMISSION : MULTI_PERMISSIONS);
        } else {
            permissionFragmentHelper
                    .request(Manifest.permission.SYSTEM_ALERT_WINDOW);/*you can pass it along other permissions,
                     just make sure you override OnActivityResult so you can get a callback.
                     ignoring that will result to not be notified if the user enable/disable the permission*/
        }
    }

    public AlertDialog getAlertDialog(final String[] permissions, final String permissionName) {
        if (builder == null) {
            builder = new AlertDialog.Builder(this.getContext())
                    .setTitle("Permission Needs Explanation")
                    .create();
        }
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionFragmentHelper.requestAfterExplanation(permissions);
            }
        });
        builder.setMessage("Permissions need explanation (" + permissionName + ")");
        return builder;
    }

    public AlertDialog getAlertDialog(final String permission) {
        if (builder == null) {
            builder = new AlertDialog.Builder(this.getContext())
                    .setTitle("Permission Needs Explanation")
                    .create();
        }
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionFragmentHelper.requestAfterExplanation(permission);
            }
        });
        builder.setMessage("Permission need explanation (" + permission + ")");
        return builder;
    }
}
