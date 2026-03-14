package com.example.madmp2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BloodLink.db";
    private static final int DATABASE_VERSION = 2; // Bumped to force upgrade

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_EMAIL_PHONE = "email_phone";
    private static final String COL_PASSWORD = "password";
    private static final String COL_FULL_NAME = "full_name";
    private static final String COL_PHONE = "phone";
    private static final String COL_ADDRESS = "address";
    private static final String COL_BLOOD_GROUP = "blood_group";

    // Donations table
    private static final String TABLE_DONATIONS = "donations";
    private static final String COL_DONATION_ID = "id";
    private static final String COL_DONATION_USER_ID = "user_id";
    private static final String COL_DONATION_DATE = "donation_date";

    // Blood Requests table
    private static final String TABLE_REQUESTS = "blood_requests";
    private static final String COL_REQUEST_ID = "id";
    private static final String COL_REQUEST_USER_ID = "user_id";
    private static final String COL_PATIENT_NAME = "patient_name";
    private static final String COL_REQUEST_BLOOD_GROUP = "blood_group";
    private static final String COL_UNITS_NEEDED = "units_needed";
    private static final String COL_HOSPITAL_NAME = "hospital_name";
    private static final String COL_IS_URGENT = "is_urgent";
    private static final String COL_REQUEST_DATE = "request_date";
    private static final String COL_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL_PHONE + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_FULL_NAME + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_ADDRESS + " TEXT, " +
                COL_BLOOD_GROUP + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_DONATIONS + " (" +
                COL_DONATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DONATION_USER_ID + " INTEGER, " +
                COL_DONATION_DATE + " INTEGER, " +
                "FOREIGN KEY (" + COL_DONATION_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_REQUESTS + " (" +
                COL_REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_REQUEST_USER_ID + " INTEGER, " +
                COL_PATIENT_NAME + " TEXT, " +
                COL_REQUEST_BLOOD_GROUP + " TEXT, " +
                COL_UNITS_NEEDED + " INTEGER, " +
                COL_HOSPITAL_NAME + " TEXT, " +
                COL_IS_URGENT + " INTEGER, " +
                COL_REQUEST_DATE + " INTEGER, " +
                COL_STATUS + " TEXT DEFAULT 'Pending', " +
                "FOREIGN KEY (" + COL_REQUEST_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))");

        // Insert sample user
        ContentValues userValues = new ContentValues();
        userValues.put(COL_EMAIL_PHONE, "vedant@gmail.com");
        userValues.put(COL_PASSWORD, "vedant123");
        userValues.put(COL_FULL_NAME, "Vedant Garud");
        userValues.put(COL_PHONE, "+1234567890");
        userValues.put(COL_ADDRESS, "Thane West");
        userValues.put(COL_BLOOD_GROUP, "B+");
        db.insert(TABLE_USERS, null, userValues);

        // Insert sample donation
        ContentValues donationValues = new ContentValues();
        donationValues.put(COL_DONATION_USER_ID, 1);
        donationValues.put(COL_DONATION_DATE, System.currentTimeMillis() / 1000 - (30 * 24 * 60 * 60));
        db.insert(TABLE_DONATIONS, null, donationValues);

        // Insert sample blood request
        ContentValues requestValues = new ContentValues();
        requestValues.put(COL_REQUEST_USER_ID, 1);
        requestValues.put(COL_PATIENT_NAME, "Jane Smith");
        requestValues.put(COL_REQUEST_BLOOD_GROUP, "O+");
        requestValues.put(COL_UNITS_NEEDED, 2);
        requestValues.put(COL_HOSPITAL_NAME, "City Hospital");
        requestValues.put(COL_IS_URGENT, 1);
        requestValues.put(COL_REQUEST_DATE, System.currentTimeMillis() / 1000);
        requestValues.put(COL_STATUS, "Pending");
        db.insert(TABLE_REQUESTS, null, requestValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DONATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
        onCreate(db);
    }

    public User validateLogin(String emailPhone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COL_EMAIL_PHONE + "=? AND " + COL_PASSWORD + "=?",
                new String[]{emailPhone, password}, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
            user.setEmailPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL_PHONE)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(COL_FULL_NAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)));
            user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)));
            user.setBloodGroup(cursor.getString(cursor.getColumnIndexOrThrow(COL_BLOOD_GROUP)));
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COL_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)));
            user.setEmailPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL_PHONE)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(COL_FULL_NAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)));
            user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)));
            user.setBloodGroup(cursor.getString(cursor.getColumnIndexOrThrow(COL_BLOOD_GROUP)));
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public long registerDonor(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL_PHONE, user.getEmailPhone());
        values.put(COL_PASSWORD, "default123");
        values.put(COL_FULL_NAME, user.getFullName());
        values.put(COL_PHONE, user.getPhone());
        values.put(COL_ADDRESS, user.getAddress());
        values.put(COL_BLOOD_GROUP, user.getBloodGroup());
        return db.insert(TABLE_USERS, null, values);
    }

    public long submitBloodRequest(BloodRequest request) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_REQUEST_USER_ID, request.getUserId());
        values.put(COL_PATIENT_NAME, request.getPatientName());
        values.put(COL_REQUEST_BLOOD_GROUP, request.getBloodGroup());
        values.put(COL_UNITS_NEEDED, request.getUnitsNeeded());
        values.put(COL_HOSPITAL_NAME, request.getHospitalName());
        values.put(COL_IS_URGENT, request.isUrgent() ? 1 : 0);
        values.put(COL_REQUEST_DATE, System.currentTimeMillis() / 1000);
        values.put(COL_STATUS, "Pending");
        return db.insert(TABLE_REQUESTS, null, values);
    }

    public long addDonation(Donation donation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DONATION_USER_ID, donation.getUserId());
        values.put(COL_DONATION_DATE, donation.getDonationDate());
        return db.insert(TABLE_DONATIONS, null, values);
    }

    public int getTotalDonations(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DONATIONS + " WHERE " + COL_DONATION_USER_ID + "=?", new String[]{String.valueOf(userId)});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public long getDaysSinceLastDonation(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + COL_DONATION_DATE + ") FROM " + TABLE_DONATIONS + " WHERE " + COL_DONATION_USER_ID + "=?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            long lastDonation = cursor.getLong(0);
            long currentTime = System.currentTimeMillis() / 1000;
            cursor.close();
            return (currentTime - lastDonation) / (24 * 60 * 60);
        }
        cursor.close();
        return -1;
    }

    public List<BloodRequest> getBloodRequestsByUserId(int userId) {
        List<BloodRequest> requests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REQUESTS, null,
                COL_REQUEST_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, COL_REQUEST_DATE + " DESC");

        while (cursor.moveToNext()) {
            BloodRequest request = new BloodRequest();
            request.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_REQUEST_USER_ID)));
            request.setPatientName(cursor.getString(cursor.getColumnIndexOrThrow(COL_PATIENT_NAME)));
            request.setBloodGroup(cursor.getString(cursor.getColumnIndexOrThrow(COL_REQUEST_BLOOD_GROUP)));
            request.setUnitsNeeded(cursor.getInt(cursor.getColumnIndexOrThrow(COL_UNITS_NEEDED)));
            request.setHospitalName(cursor.getString(cursor.getColumnIndexOrThrow(COL_HOSPITAL_NAME)));
            request.setUrgent(cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_URGENT)) == 1);
            request.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)));
            request.setRequestId("BL-" + cursor.getInt(cursor.getColumnIndexOrThrow(COL_REQUEST_ID)));
            request.setRequestDate(cursor.getLong(cursor.getColumnIndexOrThrow(COL_REQUEST_DATE)));
            requests.add(request);
        }
        cursor.close();
        return requests;
    }

    public BloodRequest getBloodRequestByRequestId(String requestId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String id = requestId.replace("BL-", "");
        Cursor cursor = db.query(TABLE_REQUESTS, null,
                COL_REQUEST_ID + "=?",
                new String[]{id}, null, null, null);

        if (cursor.moveToFirst()) {
            BloodRequest request = new BloodRequest();
            request.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_REQUEST_USER_ID)));
            request.setPatientName(cursor.getString(cursor.getColumnIndexOrThrow(COL_PATIENT_NAME)));
            request.setBloodGroup(cursor.getString(cursor.getColumnIndexOrThrow(COL_REQUEST_BLOOD_GROUP)));
            request.setUnitsNeeded(cursor.getInt(cursor.getColumnIndexOrThrow(COL_UNITS_NEEDED)));
            request.setHospitalName(cursor.getString(cursor.getColumnIndexOrThrow(COL_HOSPITAL_NAME)));
            request.setUrgent(cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_URGENT)) == 1);
            request.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)));
            request.setRequestId("BL-" + cursor.getInt(cursor.getColumnIndexOrThrow(COL_REQUEST_ID)));
            request.setRequestDate(cursor.getLong(cursor.getColumnIndexOrThrow(COL_REQUEST_DATE)));
            cursor.close();
            return request;
        }
        cursor.close();
        return null;
    }
}