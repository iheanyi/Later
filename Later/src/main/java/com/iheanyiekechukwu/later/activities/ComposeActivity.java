package com.iheanyiekechukwu.later.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.adapters.TabPagerAdapter;
import com.iheanyiekechukwu.later.helpers.HelperUtils;
import com.iheanyiekechukwu.later.models.MessageModel;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import static com.iheanyiekechukwu.later.helpers.Constants.MESSAGE_TYPE;
import static com.iheanyiekechukwu.later.helpers.Constants.MESSAGE_TYPE.MESSAGE;

public class ComposeActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    //SectionsPagerAdapter mSectionsPagerAdapter;

    //MainPagerAdapter

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ActionBar actionBar;
    private TabPagerAdapter mAdapter;
    private String[] tabs = {"Message", "Facebook", "Twitter"};

    private String[] options = {"Bob", "Sidney", "Taylor", "Jake", "Sally", "Sammy", "Samanatha", "Katie 1",
                              "Katie 2", "Matt", "Mike", "Bill", "Romeo", "George", "Mom", "Dad"};

    /* Layout variables */
    EditText messageText;
    //EditText recipientText;
    AutoCompleteTextView recipientText;
    TextView sizeText;
    Button buttonDate;
    Button buttonTime;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    Calendar newCalendar;

    MessageModel newMessage;

    String mTimeString;

    private RadioGroup radioMessageGroup;
    private RadioButton radioTypeButton;

    boolean isNew  = true;


    private int mYear, mMonth, mDay, mMinute, mHour, mHourType;

    MESSAGE_TYPE mType;

    private boolean boolSMS;
    private boolean boolTwitter;

    TextWatcher messageWatcher;

    String mRecipient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_alternate);



        // Set up the action bar.
        actionBar = getActionBar();


        mRecipient ="";



        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, options);

        recipientText = (AutoCompleteTextView) findViewById(R.id.textRecipient);
        recipientText.setThreshold(1);
        recipientText.setAdapter(dropdownAdapter);


        // Find Elements in layout
        //recipientText = (EditText) findViewById(R.id.edit_recipient);
        messageText = (EditText) findViewById(R.id.edit_message);
        sizeText = (TextView) findViewById(R.id.text_size);
        buttonDate = (Button) findViewById(R.id.btn_time);
        radioMessageGroup = (RadioGroup) findViewById(R.id.radio_type);



        radioMessageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()  {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                updateLayout(checkedId);


            }
        });


        /*ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, options);

        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        textView.setThreshold(1);
        textView.setAdapter(dropdownAdapter);*/


        radioMessageGroup.check(R.id.radio_sms);

        messageWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int textLimit = 140-(start+count);

                sizeText.setText(Integer.toString(textLimit));

                if(textLimit < 0) {
                    sizeText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                } else {
                    sizeText.setTextColor(Color.BLACK);
                }
                /*Toast.makeText(getBaseContext(), "Start: " + Integer.toString(start) + " Before: " +
                        Integer.toString(before) + " Count: " + Integer.toString(count),
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        Calendar cal = Calendar.getInstance();
        newCalendar = Calendar.getInstance();

        isNew = true;

        if (getIntent() != null) {
            isNew = getIntent().getBooleanExtra("isNew", true);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                //isNew = extras.getBoolean("isNew", true);

                //final Calendar cal;
                if (isNew) {
                    radioMessageGroup.check(R.id.radio_sms);
                    mType = MESSAGE;
                    actionBar.setTitle("Add New Message");

                } else {
                    //Crouton.makeText(ComposeActivity.this, "Editing.", Style.ALERT).show();
                    MessageModel message = (MessageModel) extras.get("message");

                    cal = message.getmDate();
                    newCalendar = message.getmDate();
                    newCalendar = Calendar.getInstance();
                    actionBar.setTitle("Edit Message");
                    if (message.getMessageType() == MESSAGE_TYPE.FACEBOOK) {
                        radioMessageGroup.check(R.id.radio_facebook);
                        updateLayout(R.id.radio_facebook);
                    } else if(message.getMessageType() == MESSAGE_TYPE.MESSAGE) {
                        radioMessageGroup.check(R.id.radio_sms);
                        updateLayout(R.id.radio_sms);
                    } else {
                        radioMessageGroup.check(R.id.radio_twitter);
                        updateLayout(R.id.radio_twitter);
                    }

                    buildEditLayout(message);
                }
            }
        }

        //sizeText.setVisibility(View.GONE);


        boolSMS = true;


/*
        newCalendar = Calendar.getInstance();

        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        mMinute = cal.get(Calendar.MINUTE);
        mHour = cal.get(Calendar.HOUR);
        mHourType = cal.get(Calendar.AM_PM);*/

        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        mMinute = cal.get(Calendar.MINUTE);
        mHour = cal.get(Calendar.HOUR);
        mHourType = cal.get(Calendar.AM_PM);

        datePickerDialog = DatePickerDialog.newInstance(this, mYear, mMonth, mDay, false);

        timePickerDialog = TimePickerDialog.newInstance(this, mHour, mMinute, false);

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.setYearRange(2013, 2028);
                datePickerDialog.show(getSupportFragmentManager(), "datepicker");

            }
        });







        //messageText.addTextChangedListener(messageWatcher);






        ArrayList<String> contactCollection = new ArrayList<String>();
        ArrayList<String> numberCollection = new ArrayList<String>();

        /*
        ContentResolver cr = getContentResolver();

        Cursor contactCur = cr.query(ContactsContract.Contacts.CONTENT_URI
                , null, null, null, null);

        int i = 0;
        while (contactCur.moveToNext()) {
            String name = contactCur.getString(contactCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String sel = contactCur.getString(contactCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            Toast.makeText(getBaseContext(), name, Toast.LENGTH_SHORT).show();
            //String num = contactCur.getString(contactCur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            contactCollection.add(sel);
            Log.e("TAG", name);
            //numberCollection.add(num);
            i++;
        }

        contactCur.close();*/

        String[] names = contactCollection.toArray(new String[contactCollection.size()]);
        contactCollection.toArray(names);

        /*ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, names);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        textView.setAdapter(dropdownAdapter);*/

    }

    private void updateLayout(int checkedId) {
        switch(checkedId) {

            case R.id.radio_sms:
                boolSMS = true;
                boolTwitter = false;

                messageText.removeTextChangedListener(messageWatcher);
                sizeText.setVisibility(View.GONE);


                recipientText.setVisibility(View.VISIBLE);

                mRecipient = recipientText.getText().toString().trim();


                mType = MESSAGE;
                //recipientText.
                break;

            case R.id.radio_facebook:
                recipientText.setVisibility(View.GONE);
                sizeText.setVisibility(View.GONE);
                messageText.removeTextChangedListener(messageWatcher);

                mType = MESSAGE_TYPE.FACEBOOK;

                mRecipient = "Facebook";
                break;

            case R.id.radio_twitter:

                boolTwitter = true;
                boolSMS = false;

                mType = MESSAGE_TYPE.TWITTER;
                sizeText.setText(Integer.toString(140-sizeText.getText().length()));
                sizeText.setVisibility(View.VISIBLE);
                sizeText.setText(Integer.toString(140-sizeText.getText().length()));
                messageText.addTextChangedListener(messageWatcher);
                recipientText.setVisibility(View.GONE);

                mRecipient = "Twitter";
                break;

            default:
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }


    public void buildEditLayout(MessageModel message) {

        if (message.getmType() == MESSAGE) {
            recipientText.setText(message.getmRecipient());
        }

        messageText.setText(message.getmMessage());
        buttonDate.setText(message.getmTime());

    }

    private boolean checkParams() {

        String message = messageText.getText().toString().trim();
        String msgDate = buttonDate.getText().toString().trim();
        String recipient = recipientText.getText().toString().trim();

        Calendar currCal = Calendar.getInstance();

        if (radioMessageGroup.getCheckedRadioButtonId() == -1) {
            Crouton.makeText(ComposeActivity.this, "Unchecked radio.", Style.INFO).show();
            return false;
        }

        if (mType == null) {
            Crouton.makeText(ComposeActivity.this, "Null type.", Style.INFO).show();
            return false;
        }

        switch (mType) {
            case MESSAGE:
                if(recipient.equals("")) {
                    Crouton.makeText(ComposeActivity.this, "Please enter a recipient.", Style.ALERT).show();

                    return false;
                }

                break;

            case TWITTER:
                if (message.length() > 140) {
                    Crouton.makeText(ComposeActivity.this, "Please shorten your message.", Style.ALERT).show();

                    return false;
                }

                break;

            case FACEBOOK:

                break;

            default:
                break;
        }

        if(message.equals("")) {
            Crouton.makeText(ComposeActivity.this, "Please enter a message.", Style.ALERT).show();

            return false;
        }

        if (msgDate.equals("")) {
            Crouton.makeText(ComposeActivity.this, "Please select a date.", Style.ALERT).show();
        }

        if (newCalendar.before(currCal)) {
            Crouton.makeText(ComposeActivity.this, "Please select a valid date.", Style.ALERT).show();
            return false;
        }

        return true;

        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case android.R.id.home:

                this.finish();
                return true;
            case R.id.action_send:



                boolean switchIntent = checkParams();


                if(switchIntent == false) {
                    //Toast.makeText(getBaseContext(), "Incorrect fields!", Toast.LENGTH_SHORT).show();
                    //Crouton.makeText(ComposeActivity.this, "Incorrect fields specified", Style.ALERT).show();
                } else {


                    if (isNew) {
                        updateLayout(radioMessageGroup.getCheckedRadioButtonId());

                        String newString = HelperUtils.buildTimeString(newCalendar);
                        Intent returnIntent = new Intent();

                        returnIntent.putExtra("recipient", mRecipient);
                        returnIntent.putExtra("message", messageText.getText().toString());
                        returnIntent.putExtra("dateString", newString);
                        returnIntent.putExtra("calendar", newCalendar);
                        returnIntent.putExtra("type", mType);
                        returnIntent.putExtra("id", radioMessageGroup.getCheckedRadioButtonId());

                        setResult(RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Crouton.makeText(ComposeActivity.this, "Called an edit function.", Style.INFO).show();

                        updateLayout(radioMessageGroup.getCheckedRadioButtonId());
                        String newString = HelperUtils.buildTimeString(newCalendar);

                        int pos = getIntent().getIntExtra("position", -1);
                        Intent returnIntent = new Intent();

                        MessageModel messageModel = new MessageModel(mType, mRecipient, messageText.getText().toString(), newString, newCalendar);
                        returnIntent.putExtra("edited_message", messageModel);
                        returnIntent.putExtra("position", pos);

                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }

                }


                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        //Toast.makeText(this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();

        mYear = year;
        mMonth = month;
        mDay = day;


        newCalendar.set(Calendar.YEAR, year);
        newCalendar.set(Calendar.MONTH, month);
        newCalendar.set(Calendar.DAY_OF_MONTH, day);
        //datePickerDialog.dismiss();
        timePickerDialog.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

        mHour = hourOfDay;

        //Toast.makeText(getBaseContext(), Integer.toString(mHour), Toast.LENGTH_SHORT).show();
        mMinute = minute;

        //newCalendar.set(mYear-1, mDay, mMonth, mHour, mMinute);
        newCalendar.set(Calendar.YEAR, mYear);
        newCalendar.set(Calendar.MONTH, mMonth);
        newCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        newCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        newCalendar.set(Calendar.MINUTE, mMinute);

//        Toast.makeText(getBaseContext(), mHour, Toast.LENGTH_SHORT).show();


        String timeString = HelperUtils.buildTimeString(newCalendar);
        buttonDate.setText(timeString);

        /*datePickerDialog = DatePickerDialog.newInstance(this, mYear-1, mMonth, mDay, false);
        timePickerDialog = TimePickerDialog.newInstance(this, mHour, mMinute, false);*/

    }

}
