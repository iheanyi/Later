package com.iheanyiekechukwu.later.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iheanyiekechukwu.later.R;

/**
 * Created by iekechuk on 11/11/13.
 */
public class ComposeMessageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_compose, container, false);

        return rootView;
    }
}
