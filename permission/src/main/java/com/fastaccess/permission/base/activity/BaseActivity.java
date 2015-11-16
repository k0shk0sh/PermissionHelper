package com.fastaccess.permission.base.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.fastaccess.permission.R;
import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.adapter.PagerAdapter;
import com.fastaccess.permission.base.callback.BaseCallback;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.fastaccess.permission.base.model.PermissionModel;
import com.fastaccess.permission.base.widget.CirclePageIndicator;

import java.util.List;

/**
 * Created by Kosh on 16/11/15 9:19 PM. copyrights @ Innov8tif
 */
public abstract class BaseActivity extends AppCompatActivity implements OnPermissionCallback, BaseCallback {

    private PermissionHelper permissionHelper;
    private ViewPager pager;
    private CirclePageIndicator indicator;

    @NonNull
    protected abstract List<PermissionModel> permissions();

    @StyleRes
    protected abstract int theme();

    @Nullable
    protected abstract Class<? extends AppCompatActivity> mainActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (theme() != 0) setTheme(theme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        if (permissions().size() == 0) {
            throw new NullPointerException("permissions() is empty");
        }
        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (CirclePageIndicator) findViewById(R.id.pager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), permissions()));
        indicator.setViewPager(pager);
        permissionHelper = PermissionHelper.getInstance(this);
    }

    @Override
    public void onPermissionGranted(String[] permissionName) {

    }

    @Override
    public void onPermissionDeclined(String[] permissionName) {

    }

    @Override
    public void onPermissionPreGranted(String permissionsName) {

    }

    @Override
    public void onPermissionNeedExplanation(String permissionName) {

    }

    @Override
    public void onPermissionReallyDeclined(String permissionName) {

    }

    @Override
    public void onNoPermissionNeeded() {
        if (mainActivity() == null) {
            finish();
            return;
        }
    }

    @Override
    public void onStatusBarColorChange(@ColorInt int color) {
        if (color == 0) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float cl = 0.9f;
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] *= cl;
            int primaryDark = Color.HSVToColor(hsv);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(primaryDark);
        }
    }

    @Override
    public void onSkip(boolean isSkipped, String permissionName) {
        pager.setCurrentItem(pager.getCurrentItem() + 1);
    }

    @Override
    public void onPermissionRequest(@NonNull String permissionName) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
