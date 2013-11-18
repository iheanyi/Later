package com.iheanyiekechukwu.later.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.adapters.MessageAdapter;
import com.iheanyiekechukwu.later.adapters.MessagePendingAdapter;

/**
 * Created by iekechuk on 11/12/13.
 */
public class MainPendingFragment extends Fragment {

    private ListView mPendingListView;
    private MessageAdapter mPendingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_pending, container, false);

        mPendingAdapter = new MessagePendingAdapter(getActivity().getBaseContext());

        mPendingListView = (ListView) rootView.findViewById(R.id.messageListView);
        return rootView;
    }
}