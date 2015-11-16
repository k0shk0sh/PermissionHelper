package com.fastaccess.permission.base.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fastaccess.permission.R;
import com.fastaccess.permission.base.callback.BaseCallback;
import com.fastaccess.permission.base.model.PermissionModel;

/**
 * Created by Kosh on 16/11/15 10:36 PM. copyrights @ Innov8tif
 */
public class PermissionFragment extends Fragment implements View.OnClickListener {

    private final static String PERMISSION_INSTANCE = "PERMISSION_INSTNACE";
    private PermissionModel permissionModel;
    private BaseCallback callback;
    private View background_layout;
    private ImageView image;
    private TextView text;
    private ImageButton skip;
    private ImageButton request;

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

    public static PermissionFragment newInstance(PermissionModel permissionModel) {
        PermissionFragment fragment = new PermissionFragment();
        Bundle localBundle = new Bundle();
        localBundle.putParcelable(PERMISSION_INSTANCE, permissionModel);
        fragment.setArguments(localBundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            permissionModel = savedInstanceState.getParcelable(PERMISSION_INSTANCE);
        } else {
            permissionModel = getArguments().getParcelable(PERMISSION_INSTANCE);
        }
        if (permissionModel == null) {
            throw new NullPointerException("Permission Model some how went nuts and become null or was it?.");
        }
        background_layout = view.findViewById(R.id.background_layout);
        image = (ImageView) view.findViewById(R.id.image);
        text = (TextView) view.findViewById(R.id.text);
        skip = (ImageButton) view.findViewById(R.id.skip);
        request = (ImageButton) view.findViewById(R.id.request);
        request.setVisibility(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? View.GONE : View.VISIBLE);
        background_layout.setBackgroundColor(permissionModel.getLayoutColor());
        image.setImageResource(permissionModel.getImageResourceId());
        text.setText(permissionModel.getExplanationText());
        text.setTextColor(permissionModel.getTextColor());
        text.setTextSize(permissionModel.getTextSize());
        skip.setImageResource(permissionModel.getSkipIcon());
        request.setImageResource(permissionModel.getRequestIcon());
        if (callback != null) callback.onStatusBarColorChange(permissionModel.getLayoutColor());
    }

    @Override
    public void onClick(View v) {
        boolean isSkip = v.getId() == R.id.skip;
        if (isSkip) {
            callback.onSkip(true, permissionModel.getPermissionName());
        } else {
            callback.onPermissionRequest(permissionModel.getPermissionName());
        }

    }

    public PermissionModel getPermissionModel() {
        return permissionModel;
    }
}
