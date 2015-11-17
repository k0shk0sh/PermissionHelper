package com.fastaccess.permission.base.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.fastaccess.permission.R;
import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.adapter.PagerAdapter;
import com.fastaccess.permission.base.callback.BaseCallback;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.fastaccess.permission.base.fragment.PermissionFragment;
import com.fastaccess.permission.base.model.PermissionModel;
import com.fastaccess.permission.base.widget.CirclePageIndicator;

import java.util.List;


public abstract class BaseActivity extends AppCompatActivity implements OnPermissionCallback, BaseCallback {

    private PermissionHelper permissionHelper;
    private ViewPager pager;
    private CirclePageIndicator indicator;
    private final static String PAGER_POSITION = "PAGER_POSITION";

    @NonNull
    protected abstract List<PermissionModel> permissions();

    @StyleRes
    protected abstract int theme();

    @Nullable
    protected abstract Class<? extends AppCompatActivity> mainActivity();

    @Nullable
    protected abstract ViewPager.PageTransformer pagerTransformer();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (pager != null) {
            outState.putInt(PAGER_POSITION, pager.getCurrentItem());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (theme() != 0) setTheme(theme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        if (permissions().isEmpty()) {
            throw new NullPointerException("permissions() is empty");
        }
        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), permissions()));
        indicator.setViewPager(pager);
        pager.setOffscreenPageLimit(permissions().size());
        permissionHelper = PermissionHelper.getInstance(this);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                onStatusBarColorChange(permissions().get(position).getLayoutColor());
            }
        });
        onStatusBarColorChange(permissions().get(0).getLayoutColor());
        if (pagerTransformer() == null)
            pager.setPageTransformer(true, new ParallaxPageTransformer());
        else
            pager.setPageTransformer(true, pagerTransformer());

        if (savedInstanceState != null) {
            pager.setCurrentItem(savedInstanceState.getInt(PAGER_POSITION), true);
        }
    }

    @Override
    public void onPermissionGranted(String[] permissionName) {
        onNext(false, permissionName[0]);// we are certain that one permission is requested.
    }

    @Override
    public void onPermissionDeclined(String[] permissionName) {
        PermissionFragment fragment = getFragment(pager.getCurrentItem());
        if (fragment != null) {}
    }

    @Override
    public void onPermissionPreGranted(String permissionsName) {
        onNext(false, permissionsName);
    }

    @Override
    public void onPermissionNeedExplanation(String permissionName) {
        PermissionFragment fragment = getFragment(pager.getCurrentItem());
        if (fragment != null) {
            // blink the textView perhaps?
        }
    }

    @Override
    public void onPermissionReallyDeclined(String permissionName) {
        onNext(true, permissionName);
    }

    @Override
    public void onNoPermissionNeeded() {
        if ((pager.getAdapter().getCount() - 1) == pager.getCurrentItem()) {
            if (mainActivity() == null) {
                finish();
            } else {
                startActivity(new Intent(this, mainActivity()));
                finish();
            }
        } else {
            onNext(true, "null");
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
        pager.setCurrentItem(pager.getCurrentItem() - 1, true);
    }

    @Override
    public void onNext(boolean skipped, @NonNull String permissionName) {
        int currentPosition = pager.getCurrentItem();
        Log.e("EEE", pager.getAdapter().getCount() + "  " + (currentPosition));
        if ((pager.getAdapter().getCount() - 1) == (currentPosition)) {
            onNoPermissionNeeded();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
        }
    }

    @Override
    public void onPermissionRequest(@NonNull String permissionName) {
        permissionHelper.request(permissionName);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private PermissionFragment getFragment(int index) {
        return (PermissionFragment) pager.getAdapter().instantiateItem(pager, index);
    }


    public class ParallaxPageTransformer implements ViewPager.PageTransformer {

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 0) { // [-1,0]
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
            } else if (position <= 1) { // (0,1]
                view.setAlpha(1 - position);
                view.setTranslationX(pageWidth * -position);
                float scaleFactor = 0.75f + (1 - 0.75f) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setAlpha(0);
            }

        }
    }
}
