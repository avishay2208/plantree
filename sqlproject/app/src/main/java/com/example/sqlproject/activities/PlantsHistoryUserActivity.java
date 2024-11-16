package com.example.sqlproject.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlproject.R;
import com.example.sqlproject.Utils;
import com.example.sqlproject.adapters.RecycleAdapterPlantsUser;
import com.example.sqlproject.entities.Plants;
import com.example.sqlproject.entities.Trees;
import com.example.sqlproject.entities.Users;
import com.google.android.material.navigation.NavigationView;

public class PlantsHistoryUserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Context context;
    RecycleAdapterPlantsUser.RecycleViewClickListener listener;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public Toolbar toolbar;
    TextView tvEmptyPlantsUser;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plants_history_user_activity);

        context = this;

        Utils.importPlantsByUserID(Users.loggedOnUser.getID());
        drawerLayout = findViewById(R.id.drawerLayoutUserPlant);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarUserPlant);
        tvEmptyPlantsUser = findViewById(R.id.tvEmptyPlantsUser);
        tvEmptyPlantsUser.setVisibility(View.GONE);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_planted_trees);

        initRecycleView();

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.tVUserName);
        TextView navEmail = headerView.findViewById(R.id.textView3);
        navUsername.setText(Users.loggedOnUser.getFullName());
        navEmail.setText(Users.loggedOnUser.getEmail());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_home){
            Intent intent = new Intent(PlantsHistoryUserActivity.this, HomeActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        if (menuItem.getItemId() == R.id.nav_new_tree_plant){
            Intent intent = new Intent(PlantsHistoryUserActivity.this, TreesListActivity.class);
            startActivity(intent);
        }
        if (menuItem.getItemId() == R.id.nav_planted_trees){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (menuItem.getItemId() == R.id.nav_account_center) {
            Intent intent = new Intent(PlantsHistoryUserActivity.this, InfoUpdateActivity.class);
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

    }

    void initRecycleView() {
        RecyclerView recycleView;

        recycleView = findViewById(R.id.rVuserPlant);
        if (Plants.getPlantsByUserID(Users.loggedOnUser.getID()).isEmpty()) {
            recycleView.setVisibility(View.GONE);
            tvEmptyPlantsUser.setVisibility(View.VISIBLE);
        } else {
            recycleView.setVisibility(View.VISIBLE);
            tvEmptyPlantsUser.setVisibility(View.GONE);
        }

        itemClick();
        RecycleAdapterPlantsUser adapter = new RecycleAdapterPlantsUser(Plants.getPlantsByUserID(Users.loggedOnUser.getID()), Trees.getTrees(), listener,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(layoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setAdapter(adapter);
    }

    void itemClick()
    {
        listener = new RecycleAdapterPlantsUser.RecycleViewClickListener()
        {
            @Override
            public void onClick(View v, int position) {
            }
        };
    }
}
