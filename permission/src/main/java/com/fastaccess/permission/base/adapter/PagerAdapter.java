package com.fastaccess.permission.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fastaccess.permission.base.fragment.PermissionFragment_;
import com.fastaccess.permission.base.model.PermissionModel;

import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private final List<PermissionModel> permissions;

    public PagerAdapter(FragmentManager fm, List<PermissionModel> permissions) {
        super(fm);
        this.permissions = permissions;
    }

    @Override
    public Fragment getItem(int position) {
        return PermissionFragment_.builder()
                .permissionModel(permissions.get(position))
                .shouldHidePreviousButton(position == 0)
                .build();
    }

    @Override
    public int getCount() {
        return permissions.size();
    }
}