package com.fastaccess.permission.base.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fastaccess.permission.R;
import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.BaseCallback;
import com.fastaccess.permission.base.model.PermissionModel;

public class PermissionFragment extends Fragment implements View.OnClickListener {

    public final static String PERMISSION_INSTANCE = "PERMISSION_INSTNACE";
    private PermissionModel permissionModel;
    private BaseCallback callback;
    private View background_layout;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (permissionModel != null) {
            outState.putParcelable(PERMISSION_INSTANCE, permissionModel);
        }
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
        title = (TextView) view.findViewById(R.id.title);
        image = (ImageView) view.findViewById(R.id.image);
        message = (TextView) view.findViewById(R.id.message);
        previous = (ImageButton) view.findViewById(R.id.previous);
        request = (ImageButton) view.findViewById(R.id.request);
        next = (ImageButton) view.findViewById(R.id.next);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        request.setVisibility(Build.VERSION.SDK_INT < Build.VERSION_CODES.M ? View.GONE : View.VISIBLE);
        background_layout.setBackgroundColor(permissionModel.getLayoutColor());
        image.setImageResource(permissionModel.getImageResourceId());
        title.setText(permissionModel.getTitle());
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, permissionModel.getTextSize());
        title.setTextColor(permissionModel.getTextColor());
        message.setText(permissionModel.getMessage());
        message.setTextColor(permissionModel.getTextColor());
        message.setTextSize(TypedValue.COMPLEX_UNIT_PX, permissionModel.getTextSize());
        previous.setImageResource(permissionModel.getPreviousIcon() == 0 ? R.drawable.ic_arrow_left : permissionModel.getPreviousIcon());
        request.setImageResource(permissionModel.getRequestIcon() == 0 ? R.drawable.ic_arrow_done : permissionModel.getRequestIcon());
        next.setImageResource(permissionModel.getNextIcon() == 0 ? R.drawable.ic_arrow_right : permissionModel.getNextIcon());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.previous) {
            callback.onSkip(true, permissionModel.getPermissionName());
        } else if (v.getId() == R.id.next) {
            boolean isGranted = PermissionHelper.isPermissionGranted(getContext(), permissionModel.getPermissionName());
            if (!permissionModel.isCanSkip() && !isGranted) {
                new AlertDialog.Builder(getContext())
                        .setMessage(permissionModel.getExplanationMessage())
                        .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callback.onPermissionRequest(permissionModel.getPermissionName());
                            }
                        }).show();
            } else {
                callback.onNext(true, permissionModel.getPermissionName());
            }
        } else if (v.getId() == R.id.request) {
            callback.onPermissionRequest(permissionModel.getPermissionName());
            Log.e("Request", "RequestCalled {" + permissionModel.getPermissionName() + "}");
        }
    }
}
