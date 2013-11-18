package com.iheanyiekechukwu.later.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.adapters.MessageSentAdapter;
import com.iheanyiekechukwu.later.helpers.Constants;
import com.iheanyiekechukwu.later.models.MessageModel;

import java.util.ArrayList;

/**
 * Created by iekechuk on 11/12/13.
 */
public class MainSentFragment extends Fragment {

    ListView sentListView;
    MessageSentAdapter mSentAdapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_sent, container, false);

        this.sentListView = (ListView) rootView.findViewById(R.id.messageListView);

        ArrayList<MessageModel> testModelList = new ArrayList<MessageModel>();

        MessageModel test1 = new MessageModel(Constants.MESSAGE_TYPE.MESSAGE, "Testing Recipient", "Hey, this is my test message." +
                " Pretty cool, huh?", "Tomorrow at 8:00PM");

        MessageModel test2 = new MessageModel(Constants.MESSAGE_TYPE.MESSAGE, "Testing Recipient", "Hey, this is my test message." +
                " Pretty cool, huh?", "Tomorrow at 8:00PM");

        MessageModel test3 = new MessageModel(Constants.MESSAGE_TYPE.FACEBOOK, "Testing Recipient", "Hey, this is my test message." +
                " Pretty cool, huh?", "Tomorrow at 8:00PM");

        MessageModel test4 = new MessageModel(Constants.MESSAGE_TYPE.TWITTER, "Testing Recipient", "Just testing of a Twitter post.", "Tomorrow at 8:00 PM");

        testModelList.add(test1);
        testModelList.add(test4);
        testModelList.add(test3);

        MessageModel[] testArray = {test1, test4, test3};

        mSentAdapter = new MessageSentAdapter(getActivity().getBaseContext());

        mSentAdapter.addAll(testModelList);



        sentListView.setAdapter(mSentAdapter);

        return rootView;
    }
}