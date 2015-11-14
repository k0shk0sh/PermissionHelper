package com.fastaccess.permission.sample;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fastaccess.permission.OnPermissionCallback;
import com.fastaccess.permission.PermissionHelper;

import java.util.Arrays;

public class SampleActivity extends AppCompatActivity implements OnPermissionCallback, View.OnClickListener {

    private final String TAG = SampleActivity.class.getName();
    private Button single;
    private Button multi;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        single = (Button) findViewById(R.id.single);
        multi = (Button) findViewById(R.id.multi);
        result = (TextView) findViewById(R.id.result);
        single.setOnClickListener(this);
        multi.setOnClickListener(this);
        permissionHelper = new PermissionHelper(this, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(String[] permissionName) {
        result.setText("Permission(s) " + Arrays.toString(permissionName) + " Granted");
        Log.i("onPermissionGranted", "Permission(s) " + Arrays.toString(permissionName) + " Granted");
    }

    @Override
    public void onPermissionDeclined(String[] permissionName) {
        result.setText("Permission(s) " + Arrays.toString(permissionName) + " Declined");
        Log.i("onPermissionDeclined", "Permission(s) " + Arrays.toString(permissionName) + " Declined");
    }

    @Override
    public void onPermissionPreGranted(String permissionsName) {
        result.setText("Permission( " + permissionsName + " ) preGranted");
        Log.i("onPermissionPreGranted", "Permission( " + permissionsName + " ) preGranted");
    }

    @Override
    public void onPermissionNeedExplanation(String permissionName) {
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

    @Override
    public void onNoPermissionNeeded() {
        result.setText("Permission(s) not needed");
        Log.i("onNoPermissionNeeded", "Permission(s) not needed");
    }

    @Override
    public void onClick(View v) {
        isSingle = v.getId() == R.id.single;
        permissionHelper
                .setForceAccepting(false)
                .request(isSingle ? SINGLE_PERMISSION : MULTI_PERMISSIONS);
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
