package com.example.buddyapp;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MonthlyBirthdaysReportActivity extends AppCompatActivity {

    private void addSeparatorRowToTable(TableLayout tableLayout) {
        TableRow separatorRow = new TableRow(this);
        separatorRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        separatorRow.setMinimumHeight(50); // Set the height of the separator row

        tableLayout.addView(separatorRow);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_birthdays_report);

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        DatabaseHelper db = new DatabaseHelper(this);

        int maleCount = db.countFriendsByGender("Male");
        int femaleCount = db.countFriendsByGender("Female");
        int[] monthCounts = db.countBirthdaysByMonth();

        // Add gender count rows
        addRowToTable(tableLayout, "Male Friends", String.valueOf(maleCount));
        addRowToTable(tableLayout, "Female Friends", String.valueOf(femaleCount));

        // Add an empty row as a separator
        addSeparatorRowToTable(tableLayout);

        // Add monthly birthday count rows
        for (int i = 0; i < monthCounts.length; i++) {
            addRowToTable(tableLayout, getMonthName(i), String.valueOf(monthCounts[i]));
        }
    }

    private void addRowToTable(TableLayout tableLayout, String label, String value) {
        TableRow row = new TableRow(this);
        row.setBackground(ContextCompat.getDrawable(this, R.drawable.border)); // Set border

        TextView labelView = createTextView(label);
        TextView valueView = createTextView(value);

        row.addView(labelView);
        row.addView(valueView);
        tableLayout.addView(row);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8); // Add padding
        textView.setBackground(ContextCompat.getDrawable(this, R.drawable.border)); // Set border
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        return textView;
    }



    private String getMonthName(int monthIndex) {
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        return months[monthIndex];
    }
}