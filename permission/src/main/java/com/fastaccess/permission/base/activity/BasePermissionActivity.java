package com.fastaccess.permission.base.activity;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.fastaccess.permission.R;
import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.adapter.PagerAdapter;
import com.fastaccess.permission.base.callback.BaseCallback;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.fastaccess.permission.base.fragment.PermissionFragment;
import com.fastaccess.permission.base.model.PermissionModel;
import com.fastaccess.permission.base.utils.FontTypeHelper;
import com.fastaccess.permission.base.utils.ThemeUtil;
import com.fastaccess.permission.base.widget.CirclePageIndicator;

import java.util.List;


public abstract class BasePermissionActivity extends AppCompatActivity implements OnPermissionCallback, BaseCallback {

    private static final String PAGER_POSITION = "PAGER_POSITION";
    private static final String SYSTEM_OVERLAY_NUM_INSTANCE = "SYSTEM_OVERLAY_NUM_INSTANCE";
    protected PermissionHelper permissionHelper;
    protected ViewPager pager;
    protected CirclePageIndicator indicator; // take control to change the color and stuff.
    private int systemOverRequestNumber = 0;/* only show the explanation once otherwise infinite
                                                        LOOP if canSkip is false */

    @NonNull protected abstract List<PermissionModel> permissions();

    @StyleRes protected abstract int theme();

    /**
     * Intro has finished.
     */
    protected abstract void onIntroFinished();

    @Nullable protected abstract ViewPager.PageTransformer pagerTransformer();

    protected abstract boolean backPressIsEnabled();

    /**
     * used to notify you that the permission is permanently denied. so you can decide whats next!
     */
    protected abstract void permissionIsPermanentlyDenied(@NonNull String permissionName);

    /**
     * used to notify that the user ignored the permission
     * <p/>
     * note: if the {@link PermissionModel#isCanSkip()} return false, we could display the explanation immediately.
     */
    protected abstract void onUserDeclinePermission(@NonNull String permissionName);

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (pager != null) {
            outState.putInt(PAGER_POSITION, pager.getCurrentItem());
        }
        outState.putInt(SYSTEM_OVERLAY_NUM_INSTANCE, systemOverRequestNumber);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (theme() != 0) setTheme(theme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_permissionhelper_layout);
        if (permissions().isEmpty()) {
            onIntroFinished();
            return;
        }
        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), permissions()));
        indicator.setViewPager(pager);
        pager.setOffscreenPageLimit(permissions().size());
        permissionHelper = PermissionHelper.getInstance(this);
        int color = permissions().get(0).getLayoutColor();
        if (color == 0) {
            color = ThemeUtil.getPrimaryColor(this);
        }
        pager.setBackgroundColor(color);
        onStatusBarColorChange(color);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int color = getPermission(position).getLayoutColor();
                if (color == 0) {
                    color = ThemeUtil.getPrimaryColor(BasePermissionActivity.this);
                }
                animateColorChange(pager, color);
            }
        });
        pager.setPageTransformer(true, pagerTransformer() == null ? new IntroTransformer() : pagerTransformer());
        if (savedInstanceState != null) {
            pager.setCurrentItem(savedInstanceState.getInt(PAGER_POSITION), true);
            systemOverRequestNumber = savedInstanceState.getInt(SYSTEM_OVERLAY_NUM_INSTANCE);
        }

    }

    /**
     * Used to determine if the user accepted {@link android.Manifest.permission#SYSTEM_ALERT_WINDOW} or no.
     * <p/>
     * if you never passed the permission this method won't be called.
     */
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        permissionHelper.onActivityForResult(requestCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override public void onBackPressed() {
        if (backPressIsEnabled()) {
            super.onBackPressed();
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        FontTypeHelper.clear();
    }

    @Override public void onPermissionGranted(@NonNull String[] permissionName) {
        onNext(permissionName[0]);// we are certain that one permission is requested.
    }

    @Override public void onPermissionDeclined(@NonNull String[] permissionName) {
        PermissionModel model = getPermission(pager.getCurrentItem());
        if (model != null) {
            if (!model.isCanSkip()) {
                if (!model.getPermissionName().equalsIgnoreCase(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                    requestPermission(model);//ask again. you asked for it, and i'm just doing it.
                    return;
                } else {
                    if (systemOverRequestNumber == 0) {//because boolean is too mainstream. (jk).
                        requestPermission(model);
                        systemOverRequestNumber = 1;
                        return;
                    } else {
                        onUserDeclinePermission(model.getPermissionName());
                    }
                }
            } else {
                onUserDeclinePermission(permissionName[0]);
            }
        }
        onNext(permissionName[0]);
    }

    @Override public void onPermissionPreGranted(@NonNull String permissionsName) {
        onNext(permissionsName);
    }

    @Override public void onPermissionNeedExplanation(@NonNull String permissionName) {
        if (!permissionName.equalsIgnoreCase(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            PermissionModel model = getPermission(pager.getCurrentItem());
            if (model != null) {
                requestPermission(model);
            } else { // it will never occur. but in case it does, call it :).
                permissionHelper.requestAfterExplanation(permissionName);
            }
        } else {
            onPermissionReallyDeclined(permissionName); // sorry, i can't do that, its just bad.
        }
    }

    @Override public void onPermissionReallyDeclined(@NonNull String permissionName) {
        permissionIsPermanentlyDenied(permissionName);
        onNoPermissionNeeded();
    }

    @Override public void onNoPermissionNeeded() {
        if ((pager.getAdapter().getCount() - 1) == pager.getCurrentItem()) {
            onIntroFinished();
        } else {
            onNext("");// the irony. I can't pass null too :p.
        }
    }

    @Override public void onStatusBarColorChange(@ColorInt int color) {
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

    @Override public void onSkip(@NonNull String permissionName) {
        pager.setCurrentItem(pager.getCurrentItem() - 1, true);
    }

    @Override public void onNext(@NonNull String permissionName) {
        int currentPosition = pager.getCurrentItem();
        if ((pager.getAdapter().getCount() - 1) == (currentPosition)) {
            onNoPermissionNeeded();
        } else {
            currentPosition = pager.getCurrentItem() + 1;
            pager.setCurrentItem(currentPosition, true);
        }
    }

    @Override public void onPermissionRequest(@NonNull String permissionName, boolean canSkip) {
        if (permissionHelper.isExplanationNeeded(permissionName)) {
            onPermissionNeedExplanation(permissionName);
        } else {
            permissionHelper.request(permissionName);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * @return instance of {@link PermissionFragment}
     */
    @SuppressWarnings("unused") protected PermissionFragment getFragment(int index) {
        return (PermissionFragment) pager.getAdapter().instantiateItem(pager, index);
    }

    /**
     * return PermissionModel at specific index.
     * <p/>
     * if index > {@link #permissions().size()} null will be returned
     */
    protected PermissionModel getPermission(int index) {
        if (permissions().isEmpty()) return null;
        if ((index) <= permissions().size()) {// avoid accessing index does not exists.
            return permissions().get(index);
        }
        return null;
    }

    /**
     * internal usage to show dialog with explanation you provided and a button to ask the user to request the permission
     */
    protected void requestPermission(final PermissionModel model) {
        new AlertDialog.Builder(this)
                .setTitle(model.getTitle())
                .setMessage(model.getExplanationMessage())
                .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (model.getPermissionName().equalsIgnoreCase(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                            permissionHelper.requestSystemAlertPermission();
                        } else {
                            permissionHelper.requestAfterExplanation(model.getPermissionName());
                        }
                    }
                }).show();
    }

    private void animateColorChange(final View view, final int color) {
        ValueAnimator animator = new ValueAnimator();
        animator.setIntValues(((ColorDrawable) view.getBackground()).getColor(), color);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setDuration(600);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setBackgroundColor((Integer) animation.getAnimatedValue());
                onStatusBarColorChange((Integer) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    protected static class IntroTransformer implements ViewPager.PageTransformer {

        private void setAlpha(View view, float value) {
            view.animate().alpha(value);
        }

        private void setTranslationX(View view, float value) {
            view.animate().translationX(value);
        }

        @Override public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            View message = view.findViewById(R.id.message);
            View title = view.findViewById(R.id.title);
            if (position >= -1) {
                if (position <= 0) {
                    setTranslationX(view, -position);
                    setTranslationX(message, pageWidth * position);
                    setTranslationX(title, pageWidth * position);
                    setAlpha(message, 1 + position);
                    setAlpha(title, 1 + position);
                } else if (position <= 1) { // (0,1]
                    setTranslationX(view, position);
                    setTranslationX(message, pageWidth * position);
                    setTranslationX(title, pageWidth * position);
                    setAlpha(message, 1 - position);
                    setAlpha(title, 1 - position);
                }
            }
        }
    }

}

