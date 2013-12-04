package com.iheanyiekechukwu.later.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.activities.AlternateActivity;
import com.iheanyiekechukwu.later.adapters.MessageTrashAdapter;
import com.iheanyiekechukwu.later.helpers.HelperUtils;
import com.iheanyiekechukwu.later.models.MessageModel;

import java.util.ArrayList;
import java.util.Calendar;

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

    TextView statusTextView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_delete:
                mTrashAdapter.clear();
                mTrashAdapter.notifyDataSetChanged();

                Crouton.makeText(mActivity, "All messages cleared from trash.", Style.ALERT).show();
                return true;
            case R.id.action_compose:

                mActivity.startComposeActivity();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_trash, container, false);

        setHasOptionsMenu(true);
        mTrashAdapter = new MessageTrashAdapter(getActivity().getBaseContext());

        mTrashListView = (ListView) rootView.findViewById(R.id.messageListView);

        statusTextView = (TextView) rootView.findViewById(R.id.statusTextView);

        mTrashListView.setAdapter(mTrashAdapter);

        moveList = new ArrayList<MessageModel>();
        mActivity = (AlternateActivity) getActivity();


        mTrashListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


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
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_pending:

                        new AlertDialog.Builder(mActivity).setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Confirm Queue")
                                .setMessage("You're moving this message from trash to pending. " +
                                        "We'll add a year to give you time to edit and reschedule when to send it.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        queueSelectedItems();
                                        mode.finish();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mode.finish();
                                        dialog.dismiss();
                                    }
                                }).show();

                        return true;

                    case R.id.action_delete:
                        //trashSelectedItems();

                        new AlertDialog.Builder(mActivity).setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Confirm Queue")
                                .setMessage("\"You're moving this message from trash to pending. " +
                                        "We'll add a year to give you time to edit and reschedule when to send it")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mTrashAdapter.clear();
                                        checkLayout();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mode.finish();
                                        dialog.dismiss();
                                    }
                                }).show();
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

        mTrashAdapter.sort(HelperUtils.MessageComparator);
        mTrashAdapter.notifyDataSetChanged();
        moveList.clear();

        checkLayout();
    }

    public void addNewItems(ArrayList<MessageModel> newData) {
        Calendar current = Calendar.getInstance();

       /* for (MessageModel item: newData) {
            //item.setmDate(current);
            mTrashAdapter.add(item);
        }
*/
        mTrashAdapter.addAll(newData);
        mTrashAdapter.sort(HelperUtils.MessageComparator);

        mTrashAdapter.notifyDataSetChanged();

        if(newData.size() == 1) {
            Crouton.makeText(getActivity(), "Message moved to trash.", Style.ALERT).show();
        } else {
            Crouton.makeText(getActivity(), "Messages moved to trash.", Style.ALERT).show();
        }

        checkLayout();
    }

    public void checkLayout() {
        if (mTrashAdapter.isEmpty()) {
            statusTextView.setVisibility(View.VISIBLE);
        } else {
            statusTextView.setVisibility(View.GONE);
        }
    }


}