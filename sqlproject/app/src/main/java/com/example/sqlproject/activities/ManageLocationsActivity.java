package com.example.sqlproject.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sqlproject.R;
import com.example.sqlproject.RestApi;
import com.example.sqlproject.Utils;
import com.example.sqlproject.entities.Location;
import com.example.sqlproject.entities.Locations;
import com.example.sqlproject.entities.Users;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class ManageLocationsActivity  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener {

    Context context;
    Button btnAddLocation, btnEditLocation, btnDeleteLocation, btnSaveLocation, btnCancelSave, btnSaveNewLocation;
    EditText etLocationAddress, etLocationLatitude, etLocationLongitude;
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;
    String locationAddress;
    double locationLatitude, locationLongitude;
    boolean validNew = true, validUpdate = true;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;

    public Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_locations_activity);

        context = this;

        drawerLayout = findViewById(R.id.drawerLayoutAdminLocationsManagement);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarAdminLocationsManagement);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        initButtons();
        initEditTexts();
        initTextInput();

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.tVUserName);
        TextView navEmail = headerView.findViewById(R.id.textView3);
        navUsername.setText(Users.loggedOnUser.getFullName());
        navEmail.setText(Users.loggedOnUser.getEmail());
    }

    void initButtons() {
        btnAddLocation = findViewById(R.id.btnAddLocation);
        btnEditLocation = findViewById(R.id.btnEditManageLocations);
        btnDeleteLocation = findViewById(R.id.btnDeleteManageLocations);
        btnSaveLocation = findViewById(R.id.btnSaveChangesManageLocations);
        btnSaveNewLocation = findViewById(R.id.btnSaveNewLocationManageLocations);
        btnCancelSave = findViewById(R.id.btnCancelChangesManageLocations);
        btnAddLocation.setOnClickListener(this);
        btnEditLocation.setOnClickListener(this);
        btnDeleteLocation.setOnClickListener(this);
        btnSaveLocation.setOnClickListener(this);
        btnSaveNewLocation.setOnClickListener(this);
        btnCancelSave.setOnClickListener(this);

        btnSaveLocation.setVisibility(View.GONE);
        btnSaveNewLocation.setVisibility(View.GONE);
        btnCancelSave.setVisibility(View.GONE);
    }

    void initEditTexts() {
        etLocationAddress = findViewById(R.id.etChangeAddress);
        etLocationLatitude = findViewById(R.id.etChangeLatitudeLocations);
        etLocationLongitude = findViewById(R.id.etChangeLongitudeLocations);

        etLocationAddress.setVisibility(View.GONE);
        etLocationLatitude.setVisibility(View.GONE);
        etLocationLongitude.setVisibility(View.GONE);
    }

    void initTextInput() {
        textInputLayout = findViewById(R.id.inputLayoutManageLocations);
        autoCompleteTextView = findViewById(R.id.inputTVManageLocations);
        Utils.importLocations();
        List<String> addresses = Locations.getAddressesOnly();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_drop_down, addresses);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Locations.setChosenLocation(Locations.getLocationByAddress(addresses.get(position)));
                textInputLayout.setErrorEnabled(false);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_home){
            Intent intent = new Intent(ManageLocationsActivity.this, HomeActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        if (menuItem.getItemId() == R.id.nav_new_tree_plant) {
            Intent intent = new Intent(ManageLocationsActivity.this, TreesListActivity.class);
            startActivity(intent);
        }
        if (menuItem.getItemId() == R.id.nav_planted_trees) {
            if (Users.loggedOnUser.isAdmin()) {
                Intent intent = new Intent(ManageLocationsActivity.this, PlantsHistoryAdminActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ManageLocationsActivity.this, PlantsHistoryUserActivity.class);
                startActivity(intent);
            }
        }
        if (menuItem.getItemId() == R.id.nav_account_center) {
            Intent intent = new Intent(ManageLocationsActivity.this, InfoUpdateActivity.class);
            startActivity(intent);
        }
        if (menuItem.getItemId() == R.id.nav_log_out){
            LogoutDialogBoxActivity logoutDialogBoxActivity = new LogoutDialogBoxActivity(this);
            logoutDialogBoxActivity.show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        textInputLayout.setErrorEnabled(false);

        if (!autoCompleteTextView.getText().toString().isEmpty()) {
            if (btnEditLocation.isPressed()) {
                textInputLayout.setVisibility(View.GONE);
                autoCompleteTextView.setVisibility(View.GONE);
                btnEditLocation.setVisibility(View.GONE);
                btnDeleteLocation.setVisibility(View.GONE);
                btnAddLocation.setVisibility(View.GONE);
                btnCancelSave.setVisibility(View.VISIBLE);
                btnSaveLocation.setVisibility(View.VISIBLE);
                etLocationAddress.setVisibility(View.VISIBLE);
                etLocationLatitude.setVisibility(View.VISIBLE);
                etLocationLongitude.setVisibility(View.VISIBLE);

                etLocationAddress.setText(Locations.chosenLocation.getAddress());
                etLocationLatitude.setText(String.valueOf(Locations.chosenLocation.getLatitude()));
                etLocationLongitude.setText(String.valueOf(Locations.chosenLocation.getLongitude()));
            }

            if (btnDeleteLocation.isPressed()) {
                DeleteLocationDialogBoxActivity deleteLocationDialogBoxActivity = new DeleteLocationDialogBoxActivity(this, Locations.chosenLocation.getID());
                deleteLocationDialogBoxActivity.show();
                initTextInput();
            }
        } else
            textInputLayout.setError("Please select an address");

        if (btnAddLocation.isPressed()) {
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setVisibility(View.GONE);
            autoCompleteTextView.setVisibility(View.GONE);
            btnEditLocation.setVisibility(View.GONE);
            btnDeleteLocation.setVisibility(View.GONE);
            btnAddLocation.setVisibility(View.GONE);
            etLocationAddress.setVisibility(View.VISIBLE);
            etLocationLatitude.setVisibility(View.VISIBLE);
            etLocationLongitude.setVisibility(View.VISIBLE);
            btnCancelSave.setVisibility(View.VISIBLE);
            btnSaveNewLocation.setVisibility(View.VISIBLE);

            etLocationAddress.setText("");
            etLocationLatitude.setText("");
            etLocationLongitude.setText("");
        }

        if (btnSaveNewLocation.isPressed()) {
            textInputLayout.setErrorEnabled(false);
            if (etLocationAddress.getText().toString().isEmpty()) {
                etLocationAddress.setError("Invalid Type");
                validNew = false;
            }
            if (etLocationLatitude.getText().toString().isEmpty()) {
                etLocationLatitude.setError("Invalid latitude");
                validNew = false;
            }
            if (etLocationLongitude.getText().toString().isEmpty()) {
                etLocationLongitude.setError("Invalid longitude");
                validNew = false;
            }

            if (validNew) {
                Locations locations = Locations.getLocations();
                String newLocationId = String.valueOf(locations.get(locations.size() - 1).getID() + 1);
                locationAddress = etLocationAddress.getText().toString();
                locationLatitude = Double.parseDouble(etLocationLatitude.getText().toString());
                locationLongitude = Double.parseDouble(etLocationLongitude.getText().toString());
                Location newLocation = new Location(Integer.parseInt(newLocationId), locationAddress,locationLatitude ,locationLongitude);

                @SuppressLint("DefaultLocale")
                String insertLocation = String.format("INSERT INTO locations (id, address, latitude, longitude) " +
                                "VALUES (%d, '%s', %f, %f)  ",
                        newLocation.getID(),
                        newLocation.getAddress(),
                        newLocation.getLatitude(),
                        newLocation.getLongitude());

                String resNew = RestApi.sqlCommand(insertLocation);
                if (!resNew.trim().isEmpty())
                    Toast.makeText(this, "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(context, "Location added successfully!", Toast.LENGTH_SHORT).show();
                }

                initTextInput();

                etLocationAddress.setVisibility(View.GONE);
                etLocationLatitude.setVisibility(View.GONE);
                etLocationLongitude.setVisibility(View.GONE);
                btnSaveLocation.setVisibility(View.GONE);
                btnSaveNewLocation.setVisibility(View.GONE);
                btnCancelSave.setVisibility(View.GONE);

                btnAddLocation.setVisibility(View.VISIBLE);
                textInputLayout.setVisibility(View.VISIBLE);
                autoCompleteTextView.setVisibility(View.VISIBLE);
                btnEditLocation.setVisibility(View.VISIBLE);
                btnDeleteLocation.setVisibility(View.VISIBLE);
            }
        }

        if (btnSaveLocation.isPressed()) {
            textInputLayout.setErrorEnabled(false);
            if (etLocationAddress.getText().toString().isEmpty()) {
                etLocationAddress.setError("Invalid Type");
                validUpdate = false;
            }
            if (etLocationLatitude.getText().toString().isEmpty()) {
                etLocationLatitude.setError("Invalid latitude");
                validUpdate = false;
            }
            if (etLocationLongitude.getText().toString().isEmpty()) {
                etLocationLongitude.setError("Invalid longitude");
                validUpdate = false;
            }
            if (validUpdate) {
                locationAddress = etLocationAddress.getText().toString();
                locationLatitude = Double.parseDouble(etLocationLatitude.getText().toString());
                locationLongitude = Double.parseDouble(etLocationLongitude.getText().toString());
                @SuppressLint("DefaultLocale")
                String updateLocation = String.format("UPDATE locations SET address = '%s', latitude = %f, longitude = %f WHERE id = %d", locationAddress, locationLatitude, locationLongitude, Locations.chosenLocation.getID());
                String resUpdate = RestApi.sqlCommand(updateLocation);
                if (!resUpdate.trim().isEmpty())
                    Toast.makeText(this, "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(context, "Location updated successfully", Toast.LENGTH_SHORT).show();
                }

                initTextInput();

                etLocationAddress.setVisibility(View.GONE);
                etLocationLatitude.setVisibility(View.GONE);
                etLocationLongitude.setVisibility(View.GONE);
                btnSaveLocation.setVisibility(View.GONE);
                btnSaveNewLocation.setVisibility(View.GONE);
                btnCancelSave.setVisibility(View.GONE);

                btnAddLocation.setVisibility(View.VISIBLE);
                textInputLayout.setVisibility(View.VISIBLE);
                autoCompleteTextView.setVisibility(View.VISIBLE);
                btnEditLocation.setVisibility(View.VISIBLE);
                btnDeleteLocation.setVisibility(View.VISIBLE);
            }
        }

        if (btnCancelSave.isPressed()) {
            textInputLayout.setErrorEnabled(false);
            etLocationAddress.setVisibility(View.GONE);
            etLocationLatitude.setVisibility(View.GONE);
            etLocationLongitude.setVisibility(View.GONE);
            btnSaveLocation.setVisibility(View.GONE);
            btnSaveNewLocation.setVisibility(View.GONE);
            btnCancelSave.setVisibility(View.GONE);

            btnAddLocation.setVisibility(View.VISIBLE);
            textInputLayout.setVisibility(View.VISIBLE);
            autoCompleteTextView.setVisibility(View.VISIBLE);
            btnEditLocation.setVisibility(View.VISIBLE);
            btnDeleteLocation.setVisibility(View.VISIBLE);
        }
    }
}
