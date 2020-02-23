package com.lrose07.concat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * @author Lauren Rose, Elizabeth Jolly
 * @version 23 Feb 2020
 * Adapter class to help display messages to the app
 */
public class MessageAdapter extends ArrayAdapter<ConCatMessage> {
    MessageAdapter(Context context, int resource, List<ConCatMessage> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message, parent, false);
        }

        TextView messageTextView = convertView.findViewById(R.id.messageTextView);
        ConCatMessage message = getItem(position);
        messageTextView.setVisibility(View.VISIBLE);
        if (message != null) messageTextView.setText(message.getText());

        return convertView;
    }
}
