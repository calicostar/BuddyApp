package com.example.buddyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "buddyDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_FRIENDS = "friends";

    // Friends Table Columns
    private static final String KEY_FRIEND_ID = "id";
    private static final String KEY_FRIEND_NAME = "name";
    private static final String KEY_FRIEND_GENDER = "gender";
    private static final String KEY_FRIEND_DOB = "dob";
    private static final String KEY_FRIEND_PHONE = "phone";
    private static final String KEY_FRIEND_EMAIL = "email";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FRIENDS_TABLE = "CREATE TABLE " + TABLE_FRIENDS +
                "(" +
                KEY_FRIEND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_FRIEND_NAME + " TEXT," +
                KEY_FRIEND_GENDER + " TEXT," +
                KEY_FRIEND_DOB + " TEXT," +
                KEY_FRIEND_PHONE + " TEXT," +
                KEY_FRIEND_EMAIL + " TEXT" +
                ")";
        db.execSQL(CREATE_FRIENDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        onCreate(db);
    }

    // Adding new friend
    public void addFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FRIEND_NAME, friend.getName());
        values.put(KEY_FRIEND_GENDER, friend.getGender());
        values.put(KEY_FRIEND_DOB, friend.getDob());
        values.put(KEY_FRIEND_PHONE, friend.getPhone());
        values.put(KEY_FRIEND_EMAIL, friend.getEmail());

        db.insert(TABLE_FRIENDS, null, values);
        db.close();
    }

    // Getting single friend
    public Friend getFriend(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FRIENDS, new String[]{KEY_FRIEND_ID,
                        KEY_FRIEND_NAME, KEY_FRIEND_GENDER, KEY_FRIEND_DOB,
                        KEY_FRIEND_PHONE, KEY_FRIEND_EMAIL}, KEY_FRIEND_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Friend friend = new Friend(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5));
        cursor.close();
        return friend;
    }

    // Code for updating, deleting, and listing all friends goes here...

    // Search for a friend by name
    public List<Friend> searchFriends(String name) {
        List<Friend> friendList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FRIENDS, new String[]{KEY_FRIEND_ID,
                        KEY_FRIEND_NAME, KEY_FRIEND_GENDER, KEY_FRIEND_DOB,
                        KEY_FRIEND_PHONE, KEY_FRIEND_EMAIL}, KEY_FRIEND_NAME + " LIKE ?",
                new String[]{"%" + name + "%"}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Friend friend = new Friend();
                friend.setId(Integer.parseInt(cursor.getString(0)));
                friend.setName(cursor.getString(1));
                friend.setGender(cursor.getString(2));
                friend.setDob(cursor.getString(3));
                friend.setPhone(cursor.getString(4));
                friend.setEmail(cursor.getString(5));
                friendList.add(friend);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return friendList;
    }

    public List<Friend> getAllFriends() {
        List<Friend> friendList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FRIENDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Friend friend = new Friend();
                friend.setId(cursor.getInt(cursor.getColumnIndex(KEY_FRIEND_ID)));
                friend.setName(cursor.getString(cursor.getColumnIndex(KEY_FRIEND_NAME)));
                friend.setGender(cursor.getString(cursor.getColumnIndex(KEY_FRIEND_GENDER)));
                friend.setDob(cursor.getString(cursor.getColumnIndex(KEY_FRIEND_DOB)));
                friend.setPhone(cursor.getString(cursor.getColumnIndex(KEY_FRIEND_PHONE)));
                friend.setEmail(cursor.getString(cursor.getColumnIndex(KEY_FRIEND_EMAIL)));

                // Adding friend to list
                friendList.add(friend);
            } while (cursor.moveToNext());
        }

        // Close the cursor to avoid memory leaks
        cursor.close();

        // Return the list of friends
        return friendList;
    }

    // Method to update a friend
    public int updateFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FRIEND_NAME, friend.getName());
        values.put(KEY_FRIEND_GENDER, friend.getGender());
        values.put(KEY_FRIEND_DOB, friend.getDob());
        values.put(KEY_FRIEND_PHONE, friend.getPhone());
        values.put(KEY_FRIEND_EMAIL, friend.getEmail());

        // updating row
        return db.update(TABLE_FRIENDS, values, KEY_FRIEND_ID + " = ?",
                new String[] { String.valueOf(friend.getId()) });
    }

    // Method to delete a friend
    public void deleteFriend(int friendId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIENDS, KEY_FRIEND_ID + " = ?",
                new String[] { String.valueOf(friendId) });
        db.close();
    }

    public int countFriendsByGender(String gender) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_FRIENDS +
                " WHERE " + KEY_FRIEND_GENDER + " COLLATE NOCASE = ?";

        Cursor cursor = db.rawQuery(query, new String[] { gender });

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    public int[] countBirthdaysByMonth() {
        int[] monthCounts = new int[12];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_FRIEND_DOB + " FROM " + TABLE_FRIENDS, null);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        while (cursor.moveToNext()) {
            try {
                String dobString = cursor.getString(0);
                Date dob = dateFormat.parse(dobString);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dob);
                int month = calendar.get(Calendar.MONTH); // January is 0
                monthCounts[month]++;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return monthCounts;
    }

    public List<Friend> getTodaysBirthdays() {
        List<Friend> todaysBirthdays = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        String today = dateFormat.format(new Date()); // Today's date in dd/MM format

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FRIENDS + " WHERE " + KEY_FRIEND_DOB + " LIKE '" + today + "%'", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(KEY_FRIEND_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_FRIEND_NAME));
                String gender = cursor.getString(cursor.getColumnIndex(KEY_FRIEND_GENDER));
                String dob = cursor.getString(cursor.getColumnIndex(KEY_FRIEND_DOB));
                String phone = cursor.getString(cursor.getColumnIndex(KEY_FRIEND_PHONE));
                String email = cursor.getString(cursor.getColumnIndex(KEY_FRIEND_EMAIL));

                // Assuming you have a constructor like this in your Friend class
                Friend friend = new Friend(id, name, gender, dob, phone, email);
                todaysBirthdays.add(friend);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return todaysBirthdays;
    }
}
