package com.fastaccess.permission.base.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fastaccess.permission.R;
import com.fastaccess.permission.base.callback.BaseCallback;
import com.fastaccess.permission.base.model.PermissionModel;
import com.fastaccess.permission.base.utils.FontTypeHelper;
import com.fastaccess.permission.base.utils.ThemeUtil;

public class PermissionFragment extends Fragment implements View.OnClickListener {

    private final static String PERMISSION_INSTANCE = "PERMISSION_INSTANCE";
    private PermissionModel permissionModel;
    private BaseCallback callback;
    private ImageView image;
    private TextView message;
    private ImageButton previous;
    private ImageButton request;
    private ImageButton next;
    private TextView title;

    public static PermissionFragment newInstance(PermissionModel permissionModel) {
        PermissionFragment fragment = new PermissionFragment();
        Bundle localBundle = new Bundle();
        localBundle.putParcelable(PERMISSION_INSTANCE, permissionModel);
        fragment.setArguments(localBundle);
        return fragment;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseCallback) {
            callback = (BaseCallback) context;
        } else {
            throw new IllegalArgumentException("Activity must Implement BaseCallback.");
        }
    }

    @Override public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (permissionModel != null) {
            outState.putParcelable(PERMISSION_INSTANCE, permissionModel);
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.permissionhelper_fragment_layout, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            permissionModel = savedInstanceState.getParcelable(PERMISSION_INSTANCE);
        } else {
            permissionModel = getArguments().getParcelable(PERMISSION_INSTANCE);
        }
        if (permissionModel == null) {
            throw new NullPointerException("Permission Model some how went nuts and become null or was it?.");
        }
        title = (TextView) view.findViewById(R.id.title);
        image = (ImageView) view.findViewById(R.id.image);
        message = (TextView) view.findViewById(R.id.message);
        previous = (ImageButton) view.findViewById(R.id.previous);
        next = (ImageButton) view.findViewById(R.id.next);
        request = (ImageButton) view.findViewById(R.id.request);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        request.setOnClickListener(this);
        initViews();
    }

    @Override public void onClick(View v) {
        if (v.getId() == R.id.previous) {
            callback.onSkip(permissionModel.getPermissionName());
        } else if (v.getId() == R.id.next) {
            if (!permissionModel.isCanSkip()) {
                callback.onPermissionRequest(permissionModel.getPermissionName(), false);
            } else {
                callback.onNext(permissionModel.getPermissionName());
            }
        } else if (v.getId() == R.id.request) {
            callback.onPermissionRequest(permissionModel.getPermissionName(), true);
        }
    }

    private void initViews() {
        request.setVisibility(Build.VERSION.SDK_INT < Build.VERSION_CODES.M ? View.GONE : View.VISIBLE);
        image.setImageResource(permissionModel.getImageResourceId());
        title.setText(permissionModel.getTitle());
        title.setTextColor(permissionModel.getTextColor() == 0 ? Color.WHITE : permissionModel.getTextColor());
        message.setText(permissionModel.getMessage());
        setTextSizes();
        message.setTextColor(permissionModel.getTextColor() == 0 ? Color.WHITE : permissionModel.getTextColor());
        previous.setImageResource(permissionModel.getPreviousIcon() == 0 ? R.drawable.ic_arrow_left : permissionModel.getPreviousIcon());
        request.setImageResource(permissionModel.getRequestIcon() == 0 ? R.drawable.ic_arrow_done : permissionModel.getRequestIcon());
        next.setImageResource(permissionModel.getNextIcon() == 0 ? R.drawable.ic_arrow_right : permissionModel.getNextIcon());
        FontTypeHelper.setTextTypeFace(title, permissionModel.getFontType());
        FontTypeHelper.setTextTypeFace(message, permissionModel.getFontType());
    }

    private void setTextSizes() {
        boolean isFromResources = ThemeUtil.isTextSizeFromResources(getContext(), permissionModel.getTextSize());
        if (isFromResources) {
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(permissionModel.getTextSize()));
            message.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(permissionModel.getTextSize()));
        } else {
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, permissionModel.getTextSize());
            message.setTextSize(TypedValue.COMPLEX_UNIT_PX, permissionModel.getTextSize());

        }
    }
}
