package com.example.madmp2.db;

public class BloodRequest {
    private int userId;
    private String patientName;
    private String bloodGroup;
    private int unitsNeeded;
    private String hospitalName;
    private boolean isUrgent;
    private String status;
    private String requestId; // e.g., "BL-1234"
    private long requestDate; // Unix timestamp in seconds

    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public int getUnitsNeeded() { return unitsNeeded; }
    public void setUnitsNeeded(int unitsNeeded) { this.unitsNeeded = unitsNeeded; }
    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }
    public boolean isUrgent() { return isUrgent; }
    public void setUrgent(boolean urgent) { isUrgent = urgent; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public long getRequestDate() { return requestDate; }
    public void setRequestDate(long requestDate) { this.requestDate = requestDate; }
}