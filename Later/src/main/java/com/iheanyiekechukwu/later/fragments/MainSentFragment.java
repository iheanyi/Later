package com.iheanyiekechukwu.later.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import android.widget.TextView;

import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.activities.AlternateActivity;
import com.iheanyiekechukwu.later.adapters.MessageAdapter;
import com.iheanyiekechukwu.later.adapters.MessageSentAdapter;
import com.iheanyiekechukwu.later.helpers.HelperUtils;
import com.iheanyiekechukwu.later.models.MessageModel;

import java.util.ArrayList;
import java.util.Calendar;

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

    ArrayList<MessageModel> tmpMoveList;
    TextView statusTextView;


    boolean fromDialog = true;



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

        statusTextView = (TextView) rootView.findViewById(R.id.statusTextView);
        ArrayList<MessageModel> testModelList = new ArrayList<MessageModel>();

        moveList = new ArrayList<MessageModel>();
        mActivity = (AlternateActivity) getActivity();

        mSentAdapter = new MessageSentAdapter(getActivity().getBaseContext());



        tmpMoveList = new ArrayList<MessageModel>();
        sentListView.setAdapter(mSentAdapter);

        sentListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        sentListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                MessageModel tmpModel = mSentAdapter.getItem(position);
                //MessageAdapter test = getTargetFragment().getAdapter();

                if (checked == true) {
                    moveList.add(tmpModel);
                    tmpMoveList.add(tmpModel);

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
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_pending:
                        /*queueSelectedItems();
                        mode.finish();*/
                        return true;

                    case R.id.action_delete:

                        /*AlertDialog dialog = new AlertDialog.Builder(getActivity().getBaseContext()).setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Confirm Trash")
                                .setMessage("All selected messages will be trashed.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mActivity.getmTrashFragment().addNewItems(moveList);

                                        clearMoveList();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });*/

                        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity).setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Confirm Trash")
                                .setMessage("All selected messages will be trashed.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        trashSelectedItems();
                                        mode.finish();

                                        /*Log.e("TAG", "THIS IS A POSITIVE CLICK.");
                                        mActivity.getmTrashFragment().addNewItems(moveList);

                                        clearMoveList();*/
                                        Log.e("TAG", String.valueOf(moveList.size()));
                                    }

                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mode.finish();
                                        dialog.dismiss();
                                    }
                                });

                        dialog.show();


                        Log.e("TEST", String.valueOf(moveList.size()));
                        Log.e("TEST", String.valueOf(tmpMoveList.size()));
                       // dialog.show();



                        /*trashSelectedItems();
                        mode.finish();*/
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
            Log.e("TAG", "DELETING ITEM");
            mSentAdapter.remove(item);
        }

        mSentAdapter.sort(HelperUtils.MessageComparator);
        mSentAdapter.notifyDataSetChanged();
        moveList.clear();

        Log.e("TAG", "MOVE LIST SUCCESSSFULLY CLEARED.");

        checkLayout();
    }
    public void trashSelectedItems() {

       /*AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm Trash")
                .setMessage("All selected messages will be trashed.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.e("TAG", "THIS IS A POSITIVE CLICK.");
                        mActivity.getmTrashFragment().addNewItems(moveList);

                        clearMoveList();
                        Log.e("TAG", String.valueOf(moveList.size()));
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        Log.e("TEST", String.valueOf(moveList.size()));
        Log.e("TEST", String.valueOf(tmpMoveList.size()));
        dialog.show();*/


       // mActivity.getmTrashFragment().addNewItems(moveList);

        //clearMoveList();



        mActivity.getmTrashFragment().addNewItems(moveList);

        //undoMove(moveList);

        clearMoveList();
    }

    public void undoMove(ArrayList<MessageModel> moveList) {
        //UndoBar
    }

    public void sendItem(ArrayList<MessageModel> newData) {
        if(newData != null) {
            Calendar curr = Calendar.getInstance();
            for (MessageModel item: newData) {
                item.setmDate(curr);
                mSentAdapter.add(item);
            }
            //mSentAdapter.addAll(newData);
            mSentAdapter.sort(HelperUtils.MessageComparator);

            mSentAdapter.notifyDataSetChanged();
            checkLayout();
        }

    }

    public void checkLayout() {
        if (mSentAdapter.isEmpty()) {
            statusTextView.setVisibility(View.VISIBLE);
        } else {
            statusTextView.setVisibility(View.GONE);
        }
    }

    public void addNewItems(ArrayList<MessageModel> newData) {
        mSentAdapter.addAll(newData);
        mSentAdapter.sort(HelperUtils.MessageComparator);

        mSentAdapter.notifyDataSetChanged();

        if(newData.size() == 1) {
            Crouton.makeText(getActivity(), "Message successfully sent.", Style.CONFIRM).show();
        } else {
            Crouton.makeText(getActivity(), String.valueOf(newData.size()) + " messages successfully sent.", Style.CONFIRM).show();
        }

        checkLayout();
    }
}