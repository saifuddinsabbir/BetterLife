package com.example.dashbosrd;

import com.google.firebase.database.ServerValue;

public class Appointment {
    private String appointmentKey;
    private String speciality;
    private String doctor;
    private String schedule;
    private String serialNo;
    private String userName;
    private String age;

    public Appointment() {
    }

    public Appointment(String speciality, String doctor, String schedule, String serialNo, String userName, String age) {
        this.speciality = speciality;
        this.doctor = doctor;
        this.schedule = schedule;
        this.serialNo = serialNo;
        this.userName = userName;
        this.age = age;
    }

    public String getAppointmentKey() {
        return appointmentKey;
    }

    public void setAppointmentKey(String appointmentKey) {
        this.appointmentKey = appointmentKey;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
