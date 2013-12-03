package com.iheanyiekechukwu.later.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cocosw.undobar.UndoBarController;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;
import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.activities.AlternateActivity;
import com.iheanyiekechukwu.later.activities.ComposeActivity;
import com.iheanyiekechukwu.later.adapters.MessageAdapter;
import com.iheanyiekechukwu.later.adapters.MessagePendingAdapter;
import com.iheanyiekechukwu.later.helpers.Constants;
import com.iheanyiekechukwu.later.models.MessageModel;

import java.util.ArrayList;
import java.util.Calendar;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by iekechuk on 11/12/13.
 */
public class MainPendingFragment extends Fragment implements UndoBarController.UndoListener {

    private ListView mPendingListView;
    private MessageAdapter mPendingAdapter;

    private ArrayList<MessageModel> lastMoveList;
    //private ActionMode.Callback mActionModeCallback;
    private ActionMode mActionMode;

    int lastSelected = 0;

    private ArrayList<MessageModel> moveList;

    AlternateActivity mActivity;

    SwingRightInAnimationAdapter mAnimAdapter;


    private static final int EDIT_REQUEST_CODE = 20;
    private static final int COMPOSE_REQUEST_CODE = 10;




    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.pending_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_delete:


                    mode.finish();
                    return false;

                default:

                    return false;
            }
            //return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.pending_menu, menu);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == getActivity().RESULT_OK) {
            if(requestCode == COMPOSE_REQUEST_CODE) {


                //Toast.makeText(this, "WE MADE IT.", Toast.LENGTH_SHORT).show();
                //mViewPager.setCurrentItem(1);
                int type;
                type = data.getIntExtra("id", -1);

                Constants.MESSAGE_TYPE tType = (Constants.MESSAGE_TYPE) data.getSerializableExtra("type");
                //String recipient = "";

                String recipient = data.getStringExtra("recipient");
                Constants.MESSAGE_TYPE tmpType = Constants.MESSAGE_TYPE.MESSAGE;

                switch (type) {
                    case R.id.radio_facebook:
                        tmpType = Constants.MESSAGE_TYPE.FACEBOOK;
                        //recipient = "Facebook";
                        break;
                    case R.id.radio_sms:
                        tmpType = Constants.MESSAGE_TYPE.MESSAGE;
                        break;

                    case R.id.radio_twitter:
                        tmpType = Constants.MESSAGE_TYPE.TWITTER;
                        //recipient = "Twitter";
                        break;

                    default:
                        break;
                }

                String message = "";
                String dateString = "";
                message = data.getStringExtra("message");
                dateString = data.getStringExtra("dateString");
                Calendar tmpCal = (Calendar) data.getSerializableExtra("calendar");


           /* if (tmpCal != null) {
                Toast.makeText(getBaseContext(), Integer.toString(tmpCal.get(Calendar.HOUR)), Toast.LENGTH_SHORT).show();
            }*/


                MessageModel messageModel = new MessageModel(tType, recipient, message, dateString,tmpCal);

                addItem(messageModel);
            } else if (requestCode == EDIT_REQUEST_CODE) {
                int position = data.getIntExtra("position", -1);
                MessageModel editedMessage = (MessageModel) data.getExtras().get("edited_message");

                if(position >= 0) {
                    MessageModel removeModel = mPendingAdapter.getItem(position);
                    mPendingAdapter.remove(removeModel);

                    mPendingAdapter.add(editedMessage);
                    mPendingAdapter.notifyDataSetChanged();

                    Crouton.makeText(getActivity(), "Message edited.", Style.INFO).show();

                }
            }
        }
        //Crouton.makeText(getActivity(), "Callback received?", Style.INFO).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_pending, container, false);



        mActivity = (AlternateActivity) getActivity();

        mPendingAdapter = new MessagePendingAdapter(getActivity().getBaseContext());


        mPendingListView = (ListView) rootView.findViewById(R.id.messageListView);

        mPendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageModel selectedMessage = mPendingAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), ComposeActivity.class);
                intent.putExtra("isNew", false);
                intent.putExtra("message", selectedMessage);
                intent.putExtra("position", position);

                startActivityForResult(intent, EDIT_REQUEST_CODE);

            }
        });


        moveList = new ArrayList<MessageModel>();
        lastMoveList = new ArrayList<MessageModel>();



        ArrayList<MessageModel> testModelList = new ArrayList<MessageModel>();


        Calendar tmpCal = Calendar.getInstance();
        mPendingAdapter.add(new MessageModel(Constants.MESSAGE_TYPE.MESSAGE, "Taylor Seale", "Test message.", "Tomorrow whenever.", tmpCal));
        mPendingAdapter.add(new MessageModel(Constants.MESSAGE_TYPE.MESSAGE, "Taylor Seale", "Test message.", "Tomorrow whenever.", tmpCal));

        mPendingListView.setAdapter(mPendingAdapter);

        mPendingListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mPendingListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            ArrayList<MessageModel> tmpMessageModel = new ArrayList<MessageModel>();
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                MessageModel tmpModel = mPendingAdapter.getItem(position);

                tmpModel = mPendingAdapter.getItem(position);

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
                inflater.inflate(R.menu.pending_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_delete:
                        trashSelectedItems();
                        mode.finish();
                        return true;

                    case R.id.action_send:
                        sendSelectedItems();
                        mode.finish();
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                moveList.clear();

            }
        });
        return rootView;
    }


    private void editMessage() {
        /**
         * Method for creating a new ComposeActivity.
         *
         */

        //Intent intent = new Intent(, ComposeActivity.class);
        Intent intent = new Intent(getActivity(), ComposeActivity.class);
        //Intent intent = new Intent(this, AlternateActivity.class);
        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }

    public void trashSelectedItems() {
        mActivity.getmTrashFragment().addNewItems(moveList);

   /*     for(MessageModel item : moveList) {
            mPendingAdapter.remove(item);
        }

        mPendingAdapter.notifyDataSetChanged();
        moveList.clear();*/

        //Crouton.makeText(getActivity(), "Moved messages to trash!", Style.ALERT).show();

       //UndoBarController.show(mActivity, "Undo", this);
       clearMoveList();


    }


    public void clearMoveList() {
        for(MessageModel item : moveList) {
            mPendingAdapter.remove(item);
        }

        mPendingAdapter.notifyDataSetChanged();
        moveList.clear();
    }

    public void sendSelectedItems() {
        mActivity.getmSentFragment().addNewItems(moveList);
        /*
        for(MessageModel item : moveList) {
            mPendingAdapter.remove(item);
        }

        mPendingAdapter.notifyDataSetChanged();
        moveList.clear();*/

       // lastMoveList.addAll(moveList);

        /*if(mActivity != null && sentUndoListener != null) {
            //UndoBarController.show
//            UndoBarController.show(getActivity(), "Undo", sentUndoListener);
        }*/

        clearMoveList();
    }

    public void addItem(MessageModel message) {
        mPendingAdapter.add(message);
        mPendingAdapter.notifyDataSetChanged();

        Crouton.makeText(getActivity(), "Message added to queue.", Style.INFO).show();
    }

    public void addNewItems(ArrayList<MessageModel> newItems) {
        mPendingAdapter.addAll(newItems);
        mPendingAdapter.notifyDataSetChanged();

        if(newItems.size() == 1) {
            Crouton.makeText(getActivity(), "Message moved to queue.", Style.INFO).show();
        } else {
            Crouton.makeText(getActivity(), "Messages moved to queue.", Style.INFO).show();
        }
    }


    @Override
    public void onUndo(Parcelable token) {

        Toast.makeText(mActivity.getBaseContext(), "Undo clicked", Toast.LENGTH_LONG).show();
        //mActivity.getmSentFragment().undoSelectedItems(lastMoveList);

        //lastMoveList.clear();

    }
}