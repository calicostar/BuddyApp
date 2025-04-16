package com.example.buddyapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class FriendAdapter extends ArrayAdapter<Friend> {
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int friendId);
    }

    public FriendAdapter(Context context, List<Friend> friends, OnItemClickListener listener) {
        super(context, 0, friends);
        this.mContext = context;
        this.databaseHelper = new DatabaseHelper(context);
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_friend, parent, false);
        }

        TextView friendInfoTextView = convertView.findViewById(R.id.friendInfoTextView);
        Button viewButton = convertView.findViewById(R.id.viewButton);
        // Update button reference removed
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        Friend friend = getItem(position);
        friendInfoTextView.setText(friend.toString());

        viewButton.setOnClickListener(v -> {
            if (listener != null) {
                int friendId = getItem(position).getId();
                listener.onItemClick(friendId);
            }
        });

        // Update button onClickListener removed

        deleteButton.setOnClickListener(v -> {
            databaseHelper.deleteFriend(friend.getId());
            Toast.makeText(mContext, "Friend deleted", Toast.LENGTH_SHORT).show();
            ((HomeActivity) mContext).loadFriends(); // Assuming you have this method in HomeActivity to refresh the list
        });

        return convertView;
    }
}


