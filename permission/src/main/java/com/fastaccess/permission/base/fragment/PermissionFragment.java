package com.fastaccess.permission.base.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fastaccess.permission.base.callback.BaseCallback;
import com.fastaccess.permission.base.model.PermissionModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

@EFragment(resName = "fragment_layout")
public class PermissionFragment extends Fragment {
    @FragmentArg
    @InstanceState
    PermissionModel permissionModel;
    @FragmentArg
    boolean shouldHidePreviousButton;
    @ViewById
    ImageView previous, request, next, image;
    @ViewById
    TextView title, message;
    private BaseCallback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseCallback) {
            callback = (BaseCallback) context;
        } else {
            throw new IllegalArgumentException("Activity must Implement BaseCallback.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @AfterViews
    void initViews() {
        if (permissionModel == null) {
            throw new NullPointerException("Permission Model some how went nuts and become null or was it?.");
        }
        request.setVisibility((Build.VERSION.SDK_INT < Build.VERSION_CODES.M || permissionModel.getPermissionName() == null) ? View.INVISIBLE : View.VISIBLE);
        image.setImageResource(permissionModel.getImageResourceId());
        title.setText(permissionModel.getTitle());
        title.setTextSize(permissionModel.getTextSize());
        title.setTextColor(permissionModel.getTextColor());
        message.setText(permissionModel.getMessage());
        message.setTextColor(permissionModel.getTextColor());
        if (permissionModel.getTextSize() != 0) {
            message.setTextSize(TypedValue.COMPLEX_UNIT_PX, permissionModel.getTextSize());
        }
        if (shouldHidePreviousButton) {
            previous.setVisibility(View.INVISIBLE);
        } else {
            previous.setImageResource(permissionModel.getPreviousIcon());
        }
        request.setImageResource(permissionModel.getRequestIcon());
        next.setImageResource(permissionModel.getNextIcon());
        if (!TextUtils.isEmpty(permissionModel.getFontType())) {
            Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), permissionModel.getFontType());
            if (typeface != null) {
                title.setTypeface(typeface);
                message.setTypeface(typeface);
            }
        }
    }

    @Click
    void previous() {
        callback.onSkip(permissionModel.getPermissionName());
    }

    @Click
    void next() {
        if (!permissionModel.isCanSkip()) {
            callback.onPermissionRequest(permissionModel.getPermissionName(), false);
        } else {
            callback.onNext(permissionModel.getPermissionName());
        }
    }

    @Click
    void request() {
        callback.onPermissionRequest(permissionModel.getPermissionName(), true);
    }
}