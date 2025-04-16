package com.example.buddyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateFriendActivity extends AppCompatActivity {

    private EditText nameEditText, genderEditText, dobEditText, phoneEditText, emailEditText;
    private DatabaseHelper databaseHelper;
    private int friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend); // Reuse the add friend layout

        databaseHelper = new DatabaseHelper(this);

        nameEditText = findViewById(R.id.nameEditText);
        genderEditText = findViewById(R.id.genderEditText);
        dobEditText = findViewById(R.id.dobEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        Button saveButton = findViewById(R.id.saveButton);

        friendId = getIntent().getIntExtra("FRIEND_ID", -1);
        loadFriendData(friendId);

        saveButton.setOnClickListener(v -> updateFriend());
    }

    private void loadFriendData(int id) {
        Friend friend = databaseHelper.getFriend(id);
        if (friend != null) {
            nameEditText.setText(friend.getName());
            genderEditText.setText(friend.getGender());
            dobEditText.setText(friend.getDob());
            phoneEditText.setText(friend.getPhone());
            emailEditText.setText(friend.getEmail());
        }
    }

    private void updateFriend() {
        String name = nameEditText.getText().toString();
        String gender = genderEditText.getText().toString();
        String dob = dobEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();

        Friend friend = new Friend(friendId, name, gender, dob, phone, email);
        int rowsAffected = databaseHelper.updateFriend(friend);
        if (rowsAffected > 0) {
            Toast.makeText(this, "Friend updated!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Set result as OK
        } else {
            Toast.makeText(this, "Error updating friend", Toast.LENGTH_SHORT).show();
        }
        finish(); // Finish the activity
    }
}

