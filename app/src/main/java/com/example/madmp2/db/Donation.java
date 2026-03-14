package com.example.madmp2.db;

public class Donation {
    private int id;
    private int userId;
    private long donationDate; // Stored as Unix timestamp (seconds)

    // Constructors
    public Donation() {}

    public Donation(int userId, long donationDate) {
        this.userId = userId;
        this.donationDate = donationDate;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public long getDonationDate() { return donationDate; }
    public void setDonationDate(long donationDate) { this.donationDate = donationDate; }
}