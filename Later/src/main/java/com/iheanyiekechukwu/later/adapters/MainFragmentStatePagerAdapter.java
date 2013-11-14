package com.iheanyiekechukwu.later.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.iheanyiekechukwu.later.fragments.MainPendingFragment;
import com.iheanyiekechukwu.later.fragments.MainSentFragment;
import com.iheanyiekechukwu.later.fragments.MainTrashFragment;

/**
 * Created by iekechuk on 11/13/13.
 */
public class  MainFragmentStatePagerAdapter extends FragmentStatePagerAdapter{
    private String[] TITLES = {"Trash", "Pending", "Sent"};
    private MainTrashFragment mTrashFragment;
    private MainPendingFragment mPendingFragment;
    private MainSentFragment mSentFragment;

    public MainFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        this.mTrashFragment = new MainTrashFragment();
        this.mPendingFragment = new MainPendingFragment();
        this.mSentFragment = new MainSentFragment();
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return this.mTrashFragment;
                //return new MainTrashFragment();
            case 1:
                return this.mPendingFragment;
                //return new MainPendingFragment();
            case 2:
                return this.mSentFragment;
                //return new MainSentFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
