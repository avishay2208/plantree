package com.example.sqlproject.entities;

import com.example.sqlproject.Utils;

import java.util.ArrayList;
import java.util.List;

public class Locations extends ArrayList<Location> {

    private static Locations locations = new Locations();
    public static Location chosenLocation;

    public static Location getLocationByAddress(String address) {
        return locations.stream().filter(item -> item.getAddress().equals(address)).findAny().get();
    }

    public static Locations getLocations() {
        if (locations.isEmpty())
            Utils.importLocations();

        return locations;
    }

    public static void setLocations(Locations locations) {
        Locations.locations = locations;
    }

    public static void setChosenLocation(Location location) {
        chosenLocation = location;
    }

    public static List<String> getAddressesOnly() {
        List<String> addresses = new ArrayList<>();
        for (Location location : Locations.getLocations()) {
            addresses.add(location.getAddress());
        }
        return addresses;
    }
}