package com.example.sqlproject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.sqlproject.entities.Locations;
import com.example.sqlproject.entities.Plants;
import com.example.sqlproject.entities.Trees;
import com.example.sqlproject.entities.Users;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static void importUsers() {
        String jsonUser = RestApi.sqlCommand("select * from users");
        Users.setUsers(Serialization.convertStringJsonToObject(Users.class, jsonUser));
    }

    public static void importPlants() {
        String jsonPlant = RestApi.sqlCommand("select * from plants");
        Plants.setPlants(Serialization.convertStringJsonToObject(Plants.class, jsonPlant));
    }

    public static void importPlantsByUserID(int userID) {
        @SuppressLint("DefaultLocale")
        String jsonPlantByUserID = RestApi.sqlCommand(String.format("select * from plants where userID = '%s'", userID));
        Plants.setPlants(Serialization.convertStringJsonToObject(Plants.class, jsonPlantByUserID));
    }


    public static void importTrees() {
        String jsonTree = RestApi.sqlCommand("select * from trees");
        Trees.setTrees(Serialization.convertStringJsonToObject(Trees.class, jsonTree));
    }

    public static void importLocations() {
        String jsonLocation = RestApi.sqlCommand("select * from locations");
        //Log.e("my json", jsonLocation);
        Locations.setLocations(Serialization.convertStringJsonToObject(Locations.class, jsonLocation));
    }

    public static String getCurrentDate() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }
}
