package com.fastaccess.permission.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fastaccess.permission.base.model.PermissionModel;

import java.util.List;

/**
 * Created by Kosh on 16/11/15 10:35 PM. copyrights @ Innov8tif
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private List<PermissionModel> permissions;

    public PagerAdapter(FragmentManager fm, List<PermissionModel> permissions) {
        super(fm);
        this.permissions = permissions;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
