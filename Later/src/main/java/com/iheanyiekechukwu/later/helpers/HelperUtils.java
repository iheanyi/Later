package com.iheanyiekechukwu.later.helpers;

import com.iheanyiekechukwu.later.models.MessageModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by iekechuk on 11/28/13.
 */
public class HelperUtils {

    public static Comparator<MessageModel> MessageComparator = new Comparator<MessageModel>() {
        @Override
        public int compare(MessageModel lhs, MessageModel rhs) {

            return lhs.compareTo(rhs);
        }
    };


    public static String buildTimeString(Calendar c) {
        StringBuilder timeString = new StringBuilder();

        SimpleDateFormat df = new SimpleDateFormat("M/d/yyyy, h:mm a");

        String formattedDate = df.format(c.getTime());

        //Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();

        return formattedDate;

    }
}
