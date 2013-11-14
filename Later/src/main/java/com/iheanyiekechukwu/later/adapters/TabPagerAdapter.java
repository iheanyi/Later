package com.iheanyiekechukwu.later.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iheanyiekechukwu.later.fragments.ComposeFacebookFragment;
import com.iheanyiekechukwu.later.fragments.ComposeMessageFragment;
import com.iheanyiekechukwu.later.fragments.ComposeTwitterFragment;

/**
 * Created by iekechuk on 11/11/13.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {


    public TabPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }


    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new ComposeMessageFragment();
            case 1:
                return new ComposeFacebookFragment();
            case 2:
                return new ComposeTwitterFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
