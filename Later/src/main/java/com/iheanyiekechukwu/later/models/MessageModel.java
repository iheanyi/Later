package com.iheanyiekechukwu.later.models;


import com.iheanyiekechukwu.later.helpers.Constants.MESSAGE_TYPE;

/**
 * Created by iekechuk on 11/10/13.
 * <p/>
 * <p/>
 * Model being used for an SMS Message and later for our adapter.
 */



public class MessageModel {

    private MESSAGE_TYPE mType;
    private String mRecipient;
    private String mMessage;
    //private Date mTime;
    private String mTime;

    public MessageModel(MESSAGE_TYPE mType, String mRecipient, String mMessage, String mTime) {
        this.mType = mType;
        this.mRecipient = mRecipient;
        this.mMessage = mMessage;
        this.mTime = mTime;
    }

    public MESSAGE_TYPE getMessageType() {
        return this.mType;
    }

}
