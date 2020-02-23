package com.lrose07.concat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class    MessageAdapter extends ArrayAdapter<ConCatMessage> {
    public MessageAdapter(Context context, int resource, List<ConCatMessage> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message, parent, false);
        }

        TextView messageTextView = convertView.findViewById(R.id.messageTextView);

        ConCatMessage message = getItem(position);

        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message.getText());

        return convertView;
    }
}
