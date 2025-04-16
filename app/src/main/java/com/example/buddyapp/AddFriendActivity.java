package com.example.buddyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddFriendActivity extends AppCompatActivity {

    private EditText nameEditText, genderEditText, dobEditText, phoneEditText, emailEditText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        databaseHelper = new DatabaseHelper(this);

        nameEditText = findViewById(R.id.nameEditText);
        genderEditText = findViewById(R.id.genderEditText);
        dobEditText = findViewById(R.id.dobEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFriend();
            }
        });
    }

    private void saveFriend() {
        String name = nameEditText.getText().toString();
        String gender = genderEditText.getText().toString();
        String dob = dobEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();

        Friend friend = new Friend(name, gender, dob, phone, email);
        databaseHelper.addFriend(friend);

        Toast.makeText(this, "Friend added!", Toast.LENGTH_SHORT).show();
        finish();
    }
}

