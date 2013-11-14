package com.iheanyiekechukwu.later.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.models.MessageModel;

/**
 * Created by iekechuk on 11/10/13.
 */
public class MessageAdapter extends ArrayAdapter<MessageModel> {

    // View Lookup Cache

    private static class ViewHolder {
        //ImageView typeImage;

        View typeBar;
        TextView recipientName;
        TextView dateString;
        TextView messageText;
    }

    public MessageAdapter(Context context) {
        super(context, R.layout.list_item_message);
    }

    private int getColorHelper(int colorID) {
        return getContext().getResources().getColor(colorID);
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

        switch(message.getMessageType()) {

            case FACEBOOK:
                //viewHolder.typeImage.setImageResource(R.drawable.drawable_facebook);
                viewHolder.typeBar.setBackgroundColor(getColorHelper(R.color.facebookBlue));
                viewHolder.recipientName.setText("Facebook");
                viewHolder.messageText.setText("Let me post something about my feelings that nobody cares about.");
                viewHolder.dateString.setText("Today 5:00PM");
                break;

            case MESSAGE:
                viewHolder.typeBar.setBackgroundColor(getColorHelper(R.color.messageGreen));
                viewHolder.recipientName.setVisibility(View.VISIBLE);
                viewHolder.recipientName.setText("Taylor Seale");
                viewHolder.dateString.setText("Tomorrow 12:00PM");
                //viewHolder.typeImage.setImageResource(R.drawable.drawable_message);
                viewHolder.messageText.setText("I hope we do well on this evaluation!.");
                break;
            case TWITTER:
                viewHolder.typeBar.setBackgroundColor(getColorHelper(R.color.twitterBlue));
                //viewHolder.recipientName.setVisibility(View.INVISIBLE);
                viewHolder.recipientName.setText("Twitter");
                viewHolder.dateString.setText("Today 2:00PM");
                //viewHolder.typeImage.setImageResource(R.drawable.drawable_message);
                viewHolder.messageText.setText("Let's talk about how bad Notre Dame football is.");
                break;
            default:
                break;
        }

        return view;
    }

}
