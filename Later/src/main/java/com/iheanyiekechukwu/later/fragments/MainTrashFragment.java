package com.iheanyiekechukwu.later.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.adapters.MessageTrashAdapter;

/**
 * Created by iekechuk on 11/12/13.
 */
public class MainTrashFragment extends Fragment {

    private ListView mTrashListView;
    private MessageTrashAdapter mTrashAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_trash, container, false);

        mTrashAdapter = new MessageTrashAdapter(getActivity().getBaseContext());

        mTrashListView = (ListView) rootView.findViewById(R.id.messageListView);
        return rootView;
    }
}