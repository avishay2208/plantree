package com.example.sqlproject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sqlproject.R;
import com.example.sqlproject.entities.Users;
import com.google.android.material.navigation.NavigationView;

public class AdminControlsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Context context;
    Button btnManageTrees, btnManageUsers, btnManageLocations;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;

    public Toolbar toolbar;
    private boolean isILS = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_controls_activity);
        context = this;

        drawerLayout = findViewById(R.id.drawerLayoutAdminControls);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarAdminControls);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isILS = extras.getBoolean("isILS");
        }

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        initButtons();

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.tVUserName);
        TextView navEmail = headerView.findViewById(R.id.textView3);
        navUsername.setText(Users.loggedOnUser.getFullName());
        navEmail.setText(Users.loggedOnUser.getEmail());
    }

    void initButtons() {
        btnManageTrees = findViewById(R.id.btnManageTrees);
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageLocations = findViewById(R.id.btnManageLocations);
        btnManageTrees.setOnClickListener(this);
        btnManageUsers.setOnClickListener(this);
        btnManageLocations.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_home) {
            Intent intent = new Intent(AdminControlsActivity.this, HomeActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        if (menuItem.getItemId() == R.id.nav_new_tree_plant) {
            Intent intent = new Intent(AdminControlsActivity.this, TreesListActivity.class);
            startActivity(intent);
        }
        if (menuItem.getItemId() == R.id.nav_planted_trees) {
            if (Users.loggedOnUser.isAdmin()) {
                Intent intent = new Intent(AdminControlsActivity.this, PlantsHistoryAdminActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(AdminControlsActivity.this, PlantsHistoryUserActivity.class);
                startActivity(intent);
            }
        }
        if (menuItem.getItemId() == R.id.nav_account_center) {
            Intent intent = new Intent(AdminControlsActivity.this, InfoUpdateActivity.class);
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
    public void onClick(View view) {
        if (btnManageTrees.isPressed()) {
            Intent intent = new Intent(context, ManageTreesActivity.class);
            intent.putExtra("isILS", isILS);
            context.startActivity(intent);
        }
        if (btnManageUsers.isPressed()) {
            Intent intent = new Intent(this, ManageUsersActivity.class);
            startActivity(intent);
        }
        if (btnManageLocations.isPressed()) {
            Intent intent = new Intent(this, ManageLocationsActivity.class);
            startActivity(intent);
        }
    }
}
