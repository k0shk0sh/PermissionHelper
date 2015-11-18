package com.fastaccess.permission.sample;

import android.Manifest;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.fastaccess.permission.base.activity.BasePermissionActivity;
import com.fastaccess.permission.base.model.PermissionModel;

import java.util.ArrayList;
import java.util.List;

public class SamplePagerActivity extends BasePermissionActivity {

    @NonNull
    @Override
    protected List<PermissionModel> permissions() {
        List<PermissionModel> permissions = new ArrayList<>();
        PermissionModel model = getDefaultInstance();
        model.setCanSkip(true);
        model.setPermissionName(Manifest.permission.GET_ACCOUNTS);
        model.setTitle("GET_ACCOUNTS");
        model.setMessage("PermissionHelper supports all screens, try rotating your phone/tablet");
        model.setExplanationMessage("We need this permission to customize your experience by " +
                "auto completing your email address");
        model.setFontType("my_font.ttf");
        model.setLayoutColor(getResources().getColor(R.color.colorPrimary));
        model.setImageResourceId(R.drawable.permission_three);
        permissions.add(model);
        model = getDefaultInstance();
        model.setTitle("ACCESS_FINE_LOCATION");
        model.setCanSkip(false);
        model.setPermissionName(Manifest.permission.ACCESS_FINE_LOCATION);
        model.setMessage("PermissionHelper also prevents your app getting crashed if the " +
                "requested permission never exists in your AndroidManifest" +
                ". Android DOES!");
        model.setExplanationMessage("We need this permission to access to your location to" +
                " find nearby restaurants and places you like!");
        model.setFontType("my_font.ttf");
        model.setLayoutColor(getResources().getColor(R.color.colorAccent));
        model.setImageResourceId(R.drawable.permission_two);
        permissions.add(model);
        model = getDefaultInstance();
        model.setCanSkip(true);
        model.setTitle("WRITE_EXTERNAL_STORAGE");
        model.setPermissionName(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        model.setMessage("PermissionHelper lets you customize all these stuff you are seeing!." +
                " if you ever thought of anything that improves the library please" +
                " suggest by filling up an issue in github https://github.com/k0shk0sh/PermissionHelper");
        model.setExplanationMessage("We need this permission to save your captured images and videos to your SD-Card");
        model.setFontType("my_font.ttf");
        model.setLayoutColor(getResources().getColor(R.color.blue));
        model.setImageResourceId(R.drawable.permission_one);
        permissions.add(model);
        return permissions;
    }

    @Override
    protected int theme() {
        return R.style.noActionBar;
    }

    @Override
    protected void onIntroFinished() {
        Log.i("onIntroFinished", "Intro has finished");
        // do whatever you like!
        finish();
    }

    @Nullable
    @Override
    protected ViewPager.PageTransformer pagerTransformer() {
        return null;//use default
    }

    @NonNull
    @Override
    protected Boolean backPressIsEnabled() {
        return false;
    }

    @Override
    protected void permissionIsPermanentlyDenied(String permissionName) {
        Log.e("DANGER", "Permission ( " + permissionName + " ) is permanentlyDenied and can only be granted via settings screen");
        /** {@link com.fastaccess.permission.base.PermissionHelper#openSettingsScreen(Context)} can help you to open it if you like */
    }

    @Override
    protected void onUserDeclinePermission(String permissionName) {
        Log.w("Warning", "Permission ( " + permissionName + " ) is skipped you can request it again by calling doing such\n " +
                "if (permissionHelper.isExplanationNeeded(permissionName)) {\n" +
                "        permissionHelper.requestAfterExplanation(permissionName);\n" +
                "    }\n" +
                "    if (permissionHelper.isPermissionPermanentlyDenied(permissionName)) {\n" +
                "        /** read {@link #permissionIsPermanentlyDenied(String)} **/\n" +
                "    }");

    }

    private PermissionModel getDefaultInstance() {
        Resources res = getResources();
        PermissionModel model = new PermissionModel();
        model.setTextColor(Color.WHITE);
        model.setTextSize(res.getDimensionPixelSize(R.dimen.text_size));
        model.setRequestIcon(R.drawable.ic_arrow_done);
        model.setPreviousIcon(R.drawable.ic_arrow_left);
        model.setNextIcon(R.drawable.ic_arrow_right);
        return model;
    }
}
