package com.example.buddyapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements FriendAdapter.OnItemClickListener {

    private ListView friendsListView;
    private DatabaseHelper databaseHelper;
    private EditText searchEditText;
    private Button logoutButton, btnGoToAnalysis;
    private FriendAdapter adapter;
    private TextView greetingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseHelper = new DatabaseHelper(this);
        friendsListView = findViewById(R.id.friendsListView);
        searchEditText = findViewById(R.id.searchEditText);
        logoutButton = findViewById(R.id.logoutButton);
        btnGoToAnalysis = findViewById(R.id.btnGoToAnalysis);
        greetingTextView = findViewById(R.id.greetingTextView);

        adapter = new FriendAdapter(this, databaseHelper.getAllFriends(), this);
        friendsListView.setAdapter(adapter);



        Button addFriendButton = findViewById(R.id.addFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddFriendActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        Button btnGoToAnalysis = findViewById(R.id.btnGoToAnalysis);
        btnGoToAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MonthlyBirthdaysReportActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });


        // Implementing real-time search with TextWatcher
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Trigger search operation when text changes
                performSearch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this example
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform logout actions
                logout();
            }
        });

        // Set greeting message with the username
        setGreetingMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load all friends initially
        loadFriends();
    }

    public void loadFriends() {
        List<Friend> friends = databaseHelper.getAllFriends();
        adapter.clear();
        adapter.addAll(friends);
        adapter.notifyDataSetChanged();
    }

    private void performSearch() {
        String searchTerm = searchEditText.getText().toString().trim();

        if (!searchTerm.isEmpty()) {
            List<Friend> searchResults = databaseHelper.searchFriends(searchTerm);
            adapter.clear();
            adapter.addAll(searchResults);
            adapter.notifyDataSetChanged();
        } else {
            // Empty search term, load all friends
            loadFriends();
        }
    }

    private void logout() {
        // Clear any session data or preferences related to the current user
        // For example, you can use SharedPreferences to store the current user information.
        // Clearing SharedPreferences in this case:
        getSharedPreferences("user_prefs", MODE_PRIVATE).edit().clear().apply();

        // Navigate back to the login screen
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        // Add flags to clear the back stack and prevent going back to HomeActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
    }

    private void setGreetingMessage() {
        // Retrieve the current user information from SharedPreferences
        String currentUser = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("current_user", null);

        if (currentUser != null) {
            // Display a greeting message with the username
            greetingTextView.setText("Hi, " + currentUser + "!");
        } else {
            // Redirect to login screen if there is no current user information
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            // Add flags to clear the back stack and prevent going back to HomeActivity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        }
    }

    @Override
    public void onItemClick(int friendId) {
        Intent intent = new Intent(this, ViewFriendActivity.class);
        intent.putExtra("FRIEND_ID", friendId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}