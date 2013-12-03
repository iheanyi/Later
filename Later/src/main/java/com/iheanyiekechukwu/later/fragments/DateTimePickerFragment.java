package com.iheanyiekechukwu.later.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;import com.iheanyiekechukwu.later.R;

/**
 * Created by iekechuk on 11/18/13.
 */
public class DateTimePickerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_datetime, container, false);
    }
}