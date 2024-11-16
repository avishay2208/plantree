package com.example.sqlproject.entities;

public class User {

    private int id;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String eMail;
    private final String password;
    private int plantCounter;
    private String joinDate;
    private int isAdmin;

    public User(int id, String firstName,String lastName, String phoneNumber, String eMail, String password, int plantCounter, String joinDate, int isAdmin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
        this.password = password;
        this.plantCounter = plantCounter;
        this.joinDate = joinDate;
        this.isAdmin = isAdmin;
    }

    public User(String firstName,String lastName, String phoneNumber, String eMail, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
        this.password = password;
    }

    public int getID() {
        return id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFullName() {
        return (this.firstName + " " + this.lastName);
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getEmail() {
        return this.eMail;
    }

    public String getPassword() {
        return this.password;
    }

    public int getPlantCounter() {
        return plantCounter;
    }

    public void setPlantCounter(int plantCounter) {
        this.plantCounter = plantCounter;
    }

    public String getJoinDate() {
        return this.joinDate;
    }

    public boolean isAdmin() {
        return this.isAdmin == 1;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}