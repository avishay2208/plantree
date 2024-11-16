package com.example.sqlproject.entities;

import com.example.sqlproject.Utils;

import java.util.ArrayList;

public class Plants extends ArrayList<Plant> {

    private static Plants plants = new Plants();

    public static Plants getPlants() {
        if (plants.isEmpty())
            Utils.importPlants();

        return plants;
    }

    public static Plants getPlantsByUserID(int id) {
        Utils.importPlantsByUserID(id);
        return plants;
    }

    public static void setPlants(Plants plants) {
        Plants.plants = plants;
    }
}
