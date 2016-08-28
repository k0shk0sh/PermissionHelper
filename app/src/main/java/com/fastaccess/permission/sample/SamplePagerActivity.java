package com.fastaccess.permission.sample;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.fastaccess.permission.base.activity.BasePermissionActivity;
import com.fastaccess.permission.base.model.PermissionModel;
import com.fastaccess.permission.base.model.PermissionModelBuilder;

import java.util.ArrayList;
import java.util.List;

public class SamplePagerActivity extends BasePermissionActivity {

    @NonNull @Override protected List<PermissionModel> permissions() {
        List<PermissionModel> permissions = new ArrayList<>();
        permissions.add(PermissionModelBuilder.withContext(this)
                .withCanSkip(true)
                .withPermissionName(Manifest.permission.GET_ACCOUNTS)
                .withTitle(R.string.title_get_accounts)
                .withMessage(R.string.message_get_accounts)
                .withExplanationMessage(R.string.explanation_message_get_accounts)
                .withFontType("my_font.ttf")
                .withLayoutColorRes(R.color.colorPrimary)
                .withImageResourceId(R.drawable.permission_three)
                .build());

        permissions.add(PermissionModelBuilder.withContext(this)
                .withTitle("ACCESS_FINE_LOCATION")
                .withCanSkip(false)
                .withPermissionName(Manifest.permission.ACCESS_FINE_LOCATION)
                .withMessage("PermissionHelper also prevents your app getting crashed if the " +
                        "requested permission never exists in your AndroidManifest" +
                        ". Android DOES!")
                .withExplanationMessage("We need this permission to access to your location to" +
                        " find nearby restaurants and places you like!")
                .withFontType("my_font.ttf")
                .withLayoutColorRes(R.color.colorAccent)
                .withImageResourceId(R.drawable.permission_two)
                .build());

        permissions.add(PermissionModelBuilder.withContext(this)
                .withCanSkip(true)
                .withTitle("WRITE_EXTERNAL_STORAGE")
                .withPermissionName(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withMessage("PermissionHelper lets you customize all these stuff you are seeing!." +
                        " if you ever thought of anything that improves the library please" +
                        " suggest by filling up an issue in github https://github.com/k0shk0sh/PermissionHelper")
                .withExplanationMessage("We need this permission to save your captured images and videos to your SD-Card")
                .withFontType("my_font.ttf")
                .withLayoutColorRes(R.color.blue)
                .withImageResourceId(R.drawable.permission_one)
                .build());

        permissions.add(PermissionModelBuilder.withContext(this)
                .withCanSkip(false) /*explanation only once will be called otherwise we will
                            run into infinite request if the user never grant the permission.*/
                .withTitle("SYSTEM_ALERT_WINDOW")
                .withPermissionName(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .withMessage("PermissionHelper handles requesting SYSTEM_ALERT_WINDOW permission")
                .withExplanationMessage("We need this permission to make our videoPlayer overlay on your screen.")
                .withFontType("my_font.ttf")
                .withLayoutColorRes(R.color.colorPrimaryDark)
                .withImageResourceId(R.drawable.permission_two).build());
        return permissions;
    }

    @Override protected int theme() {
        return R.style.noActionBar;
    }

    @Override protected void onIntroFinished() {
        Toast.makeText(this, "Intro Finished", Toast.LENGTH_SHORT).show();
        Log.i("onIntroFinished", "Intro has finished");
        // do whatever you like!
        finish();
    }

    @Nullable @Override protected ViewPager.PageTransformer pagerTransformer() {
        return null;//use default
    }

    @Override protected boolean backPressIsEnabled() {
        return false;
    }

    @Override protected void permissionIsPermanentlyDenied(@NonNull String permissionName) {
        Log.e("DANGER", "Permission ( " + permissionName + " ) is permanentlyDenied and can only be granted via settings screen");
        /** {@link com.fastaccess.permission.base.PermissionHelper#openSettingsScreen(Context)} can help you to open it if you like */
    }

    @Override protected void onUserDeclinePermission(@NonNull String permissionName) {
        Log.w("Warning", "Permission ( " + permissionName + " ) is skipped you can request it again by calling doing such\n " +
                "if (permissionHelper.isExplanationNeeded(permissionName)) {\n" +
                "        permissionHelper.requestAfterExplanation(permissionName);\n" +
                "    }\n" +
                "    if (permissionHelper.isPermissionPermanentlyDenied(permissionName)) {\n" +
                "        /** read {@link #permissionIsPermanentlyDenied(String)} **/\n" +
                "    }");

    }
}