package com.example.sqlproject.entities;

public class Location {
    private final int id;
    private final String address;
    private final double latitude;
    private final double longitude;

    public Location(int id, String address, double latitude, double longitude) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getID() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }
}