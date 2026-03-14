package com.example.madmp2.db;

public class User {
    private int id;
    private String emailPhone;
    private String fullName;
    private String phone;
    private String address;
    private String bloodGroup;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmailPhone() { return emailPhone; }
    public void setEmailPhone(String emailPhone) { this.emailPhone = emailPhone; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
}