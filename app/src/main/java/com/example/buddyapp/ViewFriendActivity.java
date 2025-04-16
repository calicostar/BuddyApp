package com.example.buddyapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ViewFriendActivity extends AppCompatActivity {

    private static final int REQUEST_UPDATE = 1; // Request code for updating friend

    private TextView nameTextView, genderTextView, dobTextView, phoneTextView, emailTextView;
    private Button whatsappButton, updateButton;
    private DatabaseHelper databaseHelper;
    private int friendId;
    private Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);

        databaseHelper = new DatabaseHelper(this);

        // Initialize TextViews and Buttons
        nameTextView = findViewById(R.id.nameTextView);
        genderTextView = findViewById(R.id.genderTextView);
        dobTextView = findViewById(R.id.dobTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        emailTextView = findViewById(R.id.emailTextView);
        whatsappButton = findViewById(R.id.whatsappButton);
        updateButton = findViewById(R.id.updateButton);

        whatsappButton.setOnClickListener(v -> sendWhatsAppMessage());

        updateButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewFriendActivity.this, UpdateFriendActivity.class);
            intent.putExtra("FRIEND_ID", friendId);
            startActivityForResult(intent, REQUEST_UPDATE);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        });

        friendId = getIntent().getIntExtra("FRIEND_ID", -1);
        if (friendId != -1) {
            loadFriendData(friendId);
        } else {
            Toast.makeText(this, "Friend ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFriendData(int id) {
        friend = databaseHelper.getFriend(id);
        if (friend != null) {
            updateUI();
        } else {
            Toast.makeText(this, "Friend not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        nameTextView.setText("Name: " + friend.getName());
        genderTextView.setText("Gender: " + friend.getGender());
        dobTextView.setText("DOB: " + friend.getDob());
        phoneTextView.setText("Phone: " + friend.getPhone());
        emailTextView.setText("Email: " + friend.getEmail());
    }

    private void sendWhatsAppMessage() {
        if (friend.getPhone() != null && !friend.getPhone().isEmpty()) {
            String phoneNumber = friend.getPhone().replaceAll("[^0-9]", "");
            phoneNumber = phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
            String url = "https://api.whatsapp.com/send?phone=" + Uri.encode(phoneNumber) + "&text=" + Uri.encode("Happy Birthday!");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else {
            Toast.makeText(this, "Phone number not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPDATE && resultCode == RESULT_OK) {
            // Refresh data
            loadFriendData(friendId);
        }
    }
}

