package com.iheanyiekechukwu.later.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.models.MessageModel;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by iekechuk on 11/11/13.
 */
public class ComposeTwitterFragment extends Fragment implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    /* Layout variables */
    EditText messageText;
    TextView timeText;
    Button buttonDate;
    Button buttonTime;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    Calendar newCalendar;

    MessageModel newMessage;

    String mTimeString;


    private int mYear, mMonth, mDay, mMinute, mHour, mHourType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_compose_twitter, container, false);

        messageText = (EditText) rootView.findViewById(R.id.edit_message);
        timeText = (TextView) rootView.findViewById(R.id.text_time);
        buttonDate = (Button) rootView.findViewById(R.id.btn_date);

        newCalendar = Calendar.getInstance();

        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.DAY_OF_MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        mMinute = cal.get(Calendar.MINUTE);
        mHour = cal.get(Calendar.HOUR);
        mHourType = cal.get(Calendar.AM_PM);

        datePickerDialog = DatePickerDialog.newInstance(this, mYear, mMonth+1, mDay, false);

        timePickerDialog = TimePickerDialog.newInstance(this, mHour, mMinute, false);



        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.setYearRange(mYear, 2028);
                datePickerDialog.show(getFragmentManager(), "datepicker");

            }
        });

        return rootView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                // Do Fragment menu item stuff here
                if(messageText.getText().toString().equals("") || timeText.getText().toString().equals("")) {
                    Toast.makeText(this.getActivity().getBaseContext(), "Incomplete fields!", Toast.LENGTH_SHORT).show();
                } else {
                    String newString = buildTimeString(newCalendar);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("type", 2);
                    returnIntent.putExtra("message", messageText.getText().toString());
                    returnIntent.putExtra("dateString", newString);
                    returnIntent.putExtra("calendar", newCalendar);
                    //setResult(RESULT_OK, returnIntent);
                    //finish();
                }


                return true;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Toast.makeText(getActivity(), "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();

        mYear = year;
        mMonth = month;
        mDay = day;

        //datePickerDialog.dismiss();
        timePickerDialog.show(getFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

        /*if (hourOfDay > 12) {
            mHour = hourOfDay - 12;
            mHourType = 1;
        } else {
            mHour = hourOfDay;
            mHourType = 0;
        }*/

        mHour = hourOfDay;
        mMinute = minute;

        newCalendar.set(mYear, mDay, mMonth, mHour, mMinute);


        //Toast.makeText(getActivity(), "new time:" + hourOfDay+ "-" + minute + newCalendar.get(Calendar.AM_PM), Toast.LENGTH_LONG).show();

        //String timeString = buildTimeString(mMonth, mDay, mYear, mHour, mMinute, mHourType);

        String timeString = buildTimeString(newCalendar);
        timeText.setText(timeString);

        //DatePickerDialog.newInst
        //DatePickerDialog.newInsta
        datePickerDialog = DatePickerDialog.newInstance(this, mYear, mMonth, mDay, false);
        timePickerDialog = TimePickerDialog.newInstance(this, mHour, mMinute, false);



    }

    private String buildTimeString(Calendar c) {
        StringBuilder timeString = new StringBuilder();

        SimpleDateFormat df = new SimpleDateFormat("M/d/yyyy, h:mm a");

        String formattedDate = df.format(newCalendar.getTime());

        Toast.makeText(getActivity().getBaseContext(), formattedDate, Toast.LENGTH_SHORT).show();

        return formattedDate;

    }


}
