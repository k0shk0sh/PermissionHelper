package com.fastaccess.permission.sample;

import android.Manifest;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.fastaccess.permission.base.activity.BaseActivity;
import com.fastaccess.permission.base.model.PermissionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 16/11/15 11:18 PM. copyrights @ Innov8tif
 */
public class SamplePagerActivity extends BaseActivity {

    @NonNull
    @Override
    protected List<PermissionModel> permissions() {
        List<PermissionModel> permissions = new ArrayList<>();
        PermissionModel model = getDefaultInstance();
        model.setCanSkip(true);
        model.setPermissionName(Manifest.permission.GET_ACCOUNTS);
        model.setExplanationText("We need this permission to customize your experience by auto completing your email address");
        model.setMessage("Cur cedrium resistere?A falsis, armarium fidelis epos.");
        model.setLayoutColor(getResources().getColor(R.color.colorPrimaryDark));
        model.setImageResourceId(R.drawable.permission_one);
        permissions.add(model);
        model = getDefaultInstance();
        model.setCanSkip(false);
        model.setPermissionName(Manifest.permission.ACCESS_FINE_LOCATION);
        model.setMessage("Cur cedrium resistere?A falsis, armarium fidelis epos.");
        model.setExplanationText("We need this permission to access to your location to find nearby restaurants and places you like!");
        model.setLayoutColor(getResources().getColor(R.color.colorAccent));
        model.setImageResourceId(R.drawable.permission_two);
        permissions.add(model);
        model = getDefaultInstance();
        model.setCanSkip(true);
        model.setPermissionName(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        model.setMessage("Cur cedrium resistere?A falsis, armarium fidelis epos.");
        model.setExplanationText("We need this permission to save your captured images and videos to your SD-Card");
        model.setLayoutColor(getResources().getColor(R.color.colorPrimary));
        model.setImageResourceId(R.drawable.permission_three);
        permissions.add(model);
        return permissions;
    }

    @Override
    protected int theme() {
        return R.style.noActionBar;
    }

    @Nullable
    @Override
    protected Class<? extends AppCompatActivity> mainActivity() {
        return SampleActivity.class;
    }

    @Nullable
    @Override
    protected ViewPager.PageTransformer pagerTransformer() {
        return new ParallaxPageTransformer();
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
