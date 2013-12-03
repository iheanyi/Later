package com.iheanyiekechukwu.later.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.ContextMenu;
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
import com.iheanyiekechukwu.later.adapters.MessageAdapter;
import com.iheanyiekechukwu.later.adapters.MessageSentAdapter;
import com.iheanyiekechukwu.later.models.MessageModel;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by iekechuk on 11/12/13.
 */
public class MainSentFragment extends Fragment {

    ListView sentListView;
    MessageSentAdapter mSentAdapter;

    ArrayList<MessageModel> moveList;
    AlternateActivity mActivity;




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

        moveList = new ArrayList<MessageModel>();
        mActivity = (AlternateActivity) getActivity();

        mSentAdapter = new MessageSentAdapter(getActivity().getBaseContext());


        sentListView.setAdapter(mSentAdapter);

        sentListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        sentListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                MessageModel tmpModel = mSentAdapter.getItem(position);
                //MessageAdapter test = getTargetFragment().getAdapter();

                if (checked == true) {
                    moveList.add(tmpModel);

                } else {
                    moveList.remove(tmpModel);
                }



                mode.setTitle(moveList.size() + " selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.sent_menu, menu);
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
                        trashSelectedItems();
                        mode.finish();
                        return true;

                    default:
                        return false;
                }
               // return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                moveList.clear();
            }
        });
        return rootView;
    }

    public void queueSelectedItems() {

        mActivity.getmPendingFragment().addNewItems(moveList);

        clearMoveList();
    }


    public MessageAdapter getAdapter() {
        return this.mSentAdapter;
    }
    public void clearMoveList() {
        for (MessageModel item : moveList) {
            mSentAdapter.remove(item);
        }

        mSentAdapter.notifyDataSetChanged();
        moveList.clear();
    }
    public void trashSelectedItems() {
        mActivity.getmTrashFragment().addNewItems(moveList);

        //undoMove(moveList);

        clearMoveList();
    }

    public void undoMove(ArrayList<MessageModel> moveList) {
        //UndoBar
    }

    public void undoSelectedItems(ArrayList<MessageModel> mList) {
        mActivity.getmPendingFragment().addNewItems(mList);

        for (MessageModel item : mList) {
            mSentAdapter.remove(item);
        }

        mSentAdapter.notifyDataSetChanged();
        //moveList.clear();


    }

    public void addNewItems(ArrayList<MessageModel> newData) {
        mSentAdapter.addAll(newData);
        mSentAdapter.notifyDataSetChanged();

        if(newData.size() == 1) {
            Crouton.makeText(getActivity(), "Message will be sent in 10 seconds.", Style.CONFIRM).show();
        } else {
            Crouton.makeText(getActivity(), "Messages will be sent in 10 seconds.", Style.CONFIRM).show();
        }

    }
}