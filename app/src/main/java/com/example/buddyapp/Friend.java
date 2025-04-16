package com.example.buddyapp;

public class Friend {
    private int id;         // Unique ID for the friend
    private String name;    // Name of the friend
    private String gender;  // Gender of the friend
    private String dob;     // Date of Birth
    private String phone;   // Phone number
    private String email;   // Email address

    // Constructor for adding a new friend
    public Friend(String name, String gender, String dob, String phone, String email) {
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
    }

    // Constructor for retrieving a friend from the database
    public Friend(int id, String name, String gender, String dob, String phone, String email) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
    }

    // Empty constructor
    public Friend() {
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Overriding the toString method for display purposes
    @Override
    public String toString() {
        return name + " - " + dob;
    }
}

