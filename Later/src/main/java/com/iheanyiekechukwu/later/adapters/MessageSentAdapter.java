package com.iheanyiekechukwu.later.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.models.MessageModel;

/**
 * Created by iekechuk on 11/13/13.
 */
public class MessageSentAdapter extends MessageAdapter {
    public MessageSentAdapter(Context context) {
        super(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        // Get the data item for this position
        MessageModel message = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.list_item_message, null);
            //viewHolder.typeImage = (ImageView) view.findViewById(R.id.messageImageView);
            viewHolder.typeBar = (View) view.findViewById(R.id.messageImageView);
            viewHolder.dateString = (TextView) view.findViewById(R.id.timeTextView);
            viewHolder.messageText = (TextView) view.findViewById(R.id.messageTextView);
            viewHolder.recipientName = (TextView) view.findViewById(R.id.recipientTextView);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String timeString = message.getmTime();
        String messageString = message.getmMessage();
        String recipientString = message.getmRecipient();

        viewHolder.messageText.setText(messageString);
        viewHolder.recipientName.setText(recipientString);
        viewHolder.dateString.setText("Sent: " + timeString);

        viewHolder.dateString.setTextColor(getColorHelper(android.R.color.holo_green_light));


        switch(message.getMessageType()) {

            case FACEBOOK:
                //viewHolder.typeImage.setImageResource(R.drawable.drawable_facebook);
                viewHolder.typeBar.setBackgroundColor(getColorHelper(R.color.facebookBlue));
                viewHolder.recipientName.setText("Facebook");

                break;

            case MESSAGE:
                viewHolder.typeBar.setBackgroundColor(getColorHelper(R.color.messageGreen));
                viewHolder.recipientName.setVisibility(View.VISIBLE);

                break;
            case TWITTER:
                viewHolder.typeBar.setBackgroundColor(getColorHelper(R.color.twitterBlue));
                viewHolder.recipientName.setText("Twitter");
                break;
            default:
                break;
        }

        return view;
    }
}
