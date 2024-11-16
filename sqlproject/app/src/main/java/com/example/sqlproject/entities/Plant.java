package com.example.sqlproject.entities;

public class Plant {

    private final int plantID;
    private final int userID;
    private final String userName;
    private final int treeID;
    private final String treeName;
    private final String plantAddress;
    private final String plantDate;
    private final double price;

    public Plant(int plantID, int userID, String userName, int treeID, String treeName,String plantAddress,String plantDate, double price) {
        this.plantID = plantID;
        this.userID = userID;
        this.userName = userName;
        this.treeID = treeID;
        this.treeName = treeName;
        this.plantAddress = plantAddress;
        this.plantDate = plantDate;
        this.price = price;
    }


    public int getPlantID() {
        return plantID;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public int getTreeID() {
        return treeID;
    }

    public String getTreeName() {
        return treeName;
    }

    public String getPlantAddress() {
        return plantAddress;
    }

    public String getPlantDate() {
        return plantDate;
    }

    public double getPrice() {
        return price;
    }
}