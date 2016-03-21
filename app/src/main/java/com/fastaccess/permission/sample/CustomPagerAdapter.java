package com.fastaccess.permission.sample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fastaccess.permission.base.model.PermissionModel;

import java.util.List;

public class CustomPagerAdapter extends FragmentStatePagerAdapter {

    private final List<PermissionModel> permissions;

    public CustomPagerAdapter(FragmentManager fm, List<PermissionModel> permissions) {
        super(fm);
        this.permissions = permissions;
    }

    @Override
    public Fragment getItem(int position) {
        return CustomFragment.instantiate(permissions.get(position));
    }

    @Override
    public int getCount() {
        return permissions.size();
    }
}