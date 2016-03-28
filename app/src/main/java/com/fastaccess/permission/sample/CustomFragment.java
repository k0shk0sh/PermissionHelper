package com.fastaccess.permission.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fastaccess.permission.base.fragment.PermissionFragment;
import com.fastaccess.permission.base.model.PermissionModel;

public class CustomFragment extends PermissionFragment {
    private PermissionModel permission;


    public static CustomFragment instantiate(PermissionModel permission) {
        CustomFragment customFragment = new CustomFragment();
        customFragment.permission = permission;
        return customFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView_ == null) {
            contentView_ = inflater.inflate(R.layout.custom_fragment_layout, container, false);
        }
        return contentView_;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.title)).setText(permission.getMessage());
    }
}
