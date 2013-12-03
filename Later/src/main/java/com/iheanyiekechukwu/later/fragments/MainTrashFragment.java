package com.iheanyiekechukwu.later.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.activities.AlternateActivity;
import com.iheanyiekechukwu.later.adapters.MessageTrashAdapter;
import com.iheanyiekechukwu.later.models.MessageModel;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by iekechuk on 11/12/13.
 */
public class MainTrashFragment extends Fragment {

    private ListView mTrashListView;
    private MessageTrashAdapter mTrashAdapter;

    ArrayList<MessageModel> moveList;
    AlternateActivity mActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_trash, container, false);

        mTrashAdapter = new MessageTrashAdapter(getActivity().getBaseContext());

        mTrashListView = (ListView) rootView.findViewById(R.id.messageListView);

        mTrashListView.setAdapter(mTrashAdapter);

        moveList = new ArrayList<MessageModel>();
        mActivity = (AlternateActivity) getActivity();

        mTrashListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        mTrashListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                MessageModel tmpModel = mTrashAdapter.getItem(position);

                if (checked == true) {
                    moveList.add(tmpModel);

                } else {
                    moveList.remove(tmpModel);
                }

                mode.setTitle(moveList.size() + " selected");
                //mode.set
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.trash_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_pending:
                        queueSelectedItems();
                        mode.finish();
                        return true;

                    case R.id.action_delete:
                        //trashSelectedItems();
                        mode.finish();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        return rootView;
    }

    public void queueSelectedItems() {

        mActivity.getmPendingFragment().addNewItems(moveList);

        clearMoveList();
    }

    public void clearMoveList() {
        for (MessageModel item : moveList) {
            mTrashAdapter.remove(item);
        }

        mTrashAdapter.notifyDataSetChanged();
        moveList.clear();
    }

    public void addNewItems(ArrayList<MessageModel> newData) {


        mTrashAdapter.addAll(newData);
        mTrashAdapter.notifyDataSetChanged();

        if(newData.size() == 1) {
            Crouton.makeText(getActivity(), "Message moved to trash.", Style.ALERT).show();
        } else {
            Crouton.makeText(getActivity(), "Messages moved to trash.", Style.ALERT).show();
        }


    }


}