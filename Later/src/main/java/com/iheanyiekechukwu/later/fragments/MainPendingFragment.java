package com.iheanyiekechukwu.later.fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.undobar.UndoBarController;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;
import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.activities.AlternateActivity;
import com.iheanyiekechukwu.later.activities.ComposeActivity;
import com.iheanyiekechukwu.later.adapters.MessageAdapter;
import com.iheanyiekechukwu.later.adapters.MessagePendingAdapter;
import com.iheanyiekechukwu.later.helpers.Constants;
import com.iheanyiekechukwu.later.helpers.HelperUtils;
import com.iheanyiekechukwu.later.models.MessageModel;
import com.iheanyiekechukwu.later.services.SendService;

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
    public boolean showNotif = false;


    private SendService s;


    private static final int EDIT_REQUEST_CODE = 20;
    private static final int COMPOSE_REQUEST_CODE = 10;

    public TextView statusTextView;


    NotificationManager nm;
    private Intent intent;


    private int NOTIFICATION_ID = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = new Intent(getActivity(), SendService.class);
        nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

    }

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
                    mPendingAdapter.sort(HelperUtils.MessageComparator);
                    mPendingAdapter.notifyDataSetChanged();

                    Crouton.makeText(getActivity(), "Message successfully edited.", Style.INFO).show();

                }
            }
        }
        //Crouton.makeText(getActivity(), "Callback received?", Style.INFO).show();
    }

    /*private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            s = ((SendService.SendBinder) binder).getService();
            Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT)
                    .show();
        }

        public void onServiceDisconnected(ComponentName className) {
            s = null;
        }
    };*/

    @Override
    public void onPause() {
        super.onPause();
        showNotif = true;
        getActivity().startService(intent);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(SendService.REMOVE_LATEST));
       /* super.onPause();
        //getActivity().unbindService(mConnection);
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().stopService(intent);*/
    }


    /*@Override
    public void onStart() {
        super.onStart();
        showNotif = false;

    }*/

    @Override
    public void onStop() {
        super.onStop();
        //getActivity().unbindService(mConnection);
        //getActivity().unregisterReceiver(broadcastReceiver);
        //getActivity().stopService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().stopService(intent);

    }
    @Override
    public void onResume() {
        super.onResume();
        showNotif = false;
        getActivity().startService(intent);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(SendService.REMOVE_LATEST));
        /*getActivity().bindService(new Intent(getActivity(), SendService.class), mConnection,
                Context.BIND_AUTO_CREATE);*/
    }
    public void updateService() {

        if(!mPendingAdapter.isEmpty()) {
            MessageModel nextMessage = mPendingAdapter.getItem(0);
            Calendar cal = nextMessage.getmDate();

            Calendar curr = Calendar.getInstance();

           // Content ctx = getActivity().getBaseContext();
            Intent intent = new Intent(mActivity.getBaseContext(), SendService.class);
            PendingIntent pintent = PendingIntent.getService(mActivity.getBaseContext(), 0, intent, 0);


            AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            alarm.setRepeating(AlarmManager.RTC_WAKEUP, curr.getTimeInMillis(), 5*1000, pintent);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_pending, container, false);



        statusTextView = (TextView) rootView.findViewById(R.id.statusTextView);
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
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_delete:

                        new AlertDialog.Builder(mActivity).setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Confirm Trash")
                                .setMessage("All these messages will be moved to the trash!")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        trashSelectedItems();
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

                    case R.id.action_send:
                        new AlertDialog.Builder(mActivity).setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Confirm Sending")
                                .setMessage("These selected messages will be sent IMMEDIATELY.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sendSelectedItems();
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



                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                moveList.clear();

            }
        });


        //updateService();

        getActivity().startService(new Intent(getActivity().getBaseContext(), SendService.class));
        return rootView;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {
            // Check latest date.

            checkQueue(i);

            //updateUI(intent);
        }
    };


    private boolean checkQueue(Intent data) {

        Calendar checkTime = (Calendar) data.getSerializableExtra("date");

        ArrayList<MessageModel> tmpList = new ArrayList<MessageModel>();

        //int i = 0;

        if (mPendingAdapter.isEmpty() == false) {
            int counter = 0;

            for(int i = 0; i < mPendingAdapter.getCount(); ++i) {
                MessageModel tmpModel = mPendingAdapter.getItem(i);
                Calendar tmpCal = tmpModel.getmDate();


                if (tmpCal.compareTo(checkTime) <= 0) {
                    tmpList.add(tmpModel);
                    //mPendingAdapter.remove(tmpModel);
                    counter += 1;
                }
            }

            if (tmpList.size() > 0) {
                mActivity.getmSentFragment().sendItem(tmpList);


            }

            for (MessageModel tmp : tmpList) {
                mPendingAdapter.remove(tmp);
            }


            displayNotification(tmpList.size());


            mPendingAdapter.sort(HelperUtils.MessageComparator);
            mPendingAdapter.notifyDataSetChanged();


            checkLayout();
            return true;

        } else {
            return false;
        }

    }


    public void checkLayout() {
        if (mPendingAdapter.isEmpty()) {
            statusTextView.setVisibility(View.VISIBLE);
        } else {
            statusTextView.setVisibility(View.GONE);
        }
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

    void displayNotification(int count) {



        if (count > 0) {
            String message = "";
            if (count == 1) {
                message = "Later sent your queued message!";
            } else {
                message = "Later sent your queued messages!";
            }

            Intent newIntent = new Intent(getActivity().getApplicationContext(), AlternateActivity.class);
            newIntent.setAction(Intent.ACTION_MAIN);
            newIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            PendingIntent pIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, newIntent, 0);
            Notification n = new Notification.Builder(getActivity().getBaseContext())
                    .setContentTitle("Later")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.later_logo)
                    .setContentIntent(pIntent)
                    .setNumber(count)
                    .setAutoCancel(true).build();

            if(showNotif == true) {
                nm.notify(0, n);
            } else {
                if (count == 1) {
                    Crouton.makeText(getActivity(), "Queued message successfully sent.", Style.CONFIRM).show();
                } else {
                    Crouton.makeText(getActivity(), String.valueOf(count) + " queued messages successfully sent.", Style.CONFIRM).show();
                }
            }

            checkLayout();
        }

    }
    public void trashSelectedItems() {



        /*new AlertDialog.Builder(mActivity).setIcon(android.R.drawable.ic_dialog_alert)
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
        mPendingAdapter.sort(HelperUtils.MessageComparator);

        mPendingAdapter.notifyDataSetChanged();
        moveList.clear();

        checkLayout();
    }

    public DialogFragment diaFag = new DialogFragment() {

    };
    public void sendSelectedItems() {

        mActivity.getmSentFragment().addNewItems(moveList);

        clearMoveList();



        //mActivity.getmSentFragment().addNewItems(moveList);

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

//        clearMoveList();
    }

    public void addItem(MessageModel message) {
        mPendingAdapter.add(message);
        mPendingAdapter.sort(HelperUtils.MessageComparator);
        mPendingAdapter.notifyDataSetChanged();


        Crouton.makeText(getActivity(), "Message added to queue.", Style.INFO).show();
        checkLayout();
    }

    public void addNewItems(ArrayList<MessageModel> newItems) {

        Calendar current = Calendar.getInstance();
        for(MessageModel item: newItems) {
            if (item.getmDate().before(current)) {
                Calendar old = item.getmDate();
                int oldYear = old.get(Calendar.YEAR);
                old.set(Calendar.YEAR, oldYear+1);
                item.setmDate(old);
            }

            mPendingAdapter.add(item);
        }
        //mPendingAdapter.addAll(newItems);

        mPendingAdapter.sort(HelperUtils.MessageComparator);
        mPendingAdapter.notifyDataSetChanged();

        if(newItems.size() == 1) {
            Crouton.makeText(getActivity(), "Message moved to queue.", Style.INFO).show();
        } else {
            Crouton.makeText(getActivity(), "Messages moved to queue.", Style.INFO).show();
        }

        checkLayout();
    }


    @Override
    public void onUndo(Parcelable token) {

        Toast.makeText(mActivity.getBaseContext(), "Undo clicked", Toast.LENGTH_LONG).show();
        //mActivity.getmSentFragment().undoSelectedItems(lastMoveList);

        //lastMoveList.clear();

    }

}