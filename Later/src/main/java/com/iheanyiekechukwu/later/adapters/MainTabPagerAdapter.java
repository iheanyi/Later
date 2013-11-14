package com.iheanyiekechukwu.later.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iheanyiekechukwu.later.fragments.MainPendingFragment;
import com.iheanyiekechukwu.later.fragments.MainSentFragment;
import com.iheanyiekechukwu.later.fragments.MainTrashFragment;

/**
 * Created by iekechuk on 11/12/13.
 */
public class MainTabPagerAdapter extends FragmentPagerAdapter {

    private String[] TITLES = {"Trash", "Pending", "Sent"};

    public MainTabPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new MainTrashFragment();
            case 1:
                return new MainPendingFragment();
            case 2:
                return new MainSentFragment();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }


    @Override
    public int getCount() {
        return 3;
    }
}
