package com.iheanyiekechukwu.later.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.iheanyiekechukwu.later.helpers.Constants.MESSAGE_TYPE;
import com.iheanyiekechukwu.later.helpers.HelperUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by iekechuk on 11/10/13.
 * <p/>
 * <p/>
 * Model being used for an SMS Message and later for our adapter.
 */



public class MessageModel implements Parcelable {

    private MESSAGE_TYPE mType;
    private String mRecipient;
    private String mMessage;
    //private Date mTime;
    private String mTime;
    private int mYear;
    private int mDay;
    private int mMonth;
    private int mHour;
    private int mMinute;

    private Calendar mDate;

    public MessageModel(MESSAGE_TYPE mType, String mRecipient, String mMessage, String mTime, Calendar mDate) {
        this.mType = mType;
        this.mRecipient = mRecipient;
        this.mMessage = mMessage;
        this.mTime = mTime;
        this.mDate = mDate;

        this.mTime = HelperUtils.buildTimeString(mDate);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(mType);
        dest.writeString(mRecipient);
        dest.writeString(mMessage);
        dest.writeString(mTime);
        dest.writeSerializable(mDate);
    }

    public MessageModel(Parcel in) {
       // String[] data = new S
        this.mType = (MESSAGE_TYPE) in.readSerializable();
        this.mRecipient = in.readString();
        this.mMessage = in.readString();
        this.mTime = in.readString();
        this.mDate = (Calendar) in.readSerializable();

    }
    public MessageModel(MessageModel item) {
        this.mType = item.getmType();
        this.mRecipient = item.getmRecipient();
        this.mMessage = item.getmMessage();
        this.mDate = item.getmDate();
        this.mTime = item.getmTime();
    }

    public MESSAGE_TYPE getMessageType() {

        return this.mType;
    }

    public MESSAGE_TYPE getmType() {
        return mType;
    }

    public void setmType(MESSAGE_TYPE mType) {
        this.mType = mType;
    }

    public String getmRecipient() {
        return mRecipient;
    }

    public void setmRecipient(String mRecipient) {
        this.mRecipient = mRecipient;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public Calendar getmDate() {
        return mDate;
    }

    public void setmDate(Calendar mDate) {
        this.mDate = mDate;
    }

    private String buildTimeString(Calendar c) {
        StringBuilder timeString = new StringBuilder();

        SimpleDateFormat df = new SimpleDateFormat("MM/d/yyyy at h:mm a");

        String formattedDate = df.format(mDate.getTime());

        //Toast.makeText(getActivity().getBaseContext(), formattedDate, Toast.LENGTH_SHORT).show();

        return formattedDate;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public MessageModel createFromParcel(Parcel source) {
            return new MessageModel(source);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

}
