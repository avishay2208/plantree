package com.example.sqlproject.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlproject.R;
import com.example.sqlproject.Utils;
import com.example.sqlproject.adapters.RecycleAdapterUsers;
import com.example.sqlproject.RestApi;
import com.example.sqlproject.entities.User;
import com.example.sqlproject.entities.Users;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.stream.Collectors;

public class ManageUsersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener {

    Context context;
    RecycleAdapterUsers.RecycleViewClickListener listener;
    public RecycleAdapterUsers userAdapter;

    public Toolbar toolbar;
    public NavigationView navigationView;
    public DrawerLayout drawerLayout;
    User chosenUser;
    List<User> filteredUsers;
    CheckBox cBisAdmin;
    TextView tvUserCounter;
    Button btnSaveChangesManageUsers, btnDeleteUserManageUsers;
    RecyclerView recycleView;
    SearchView searchView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_users_activity);

        context = this;

        Users.getUsers();
        filteredUsers = Users.getUsers();

        drawerLayout = findViewById(R.id.drawerLayoutAdminUsersManagement);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarAdminUsersManagement);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_new_tree_plant);

        initButtons();
        initTextViews();
        initRecycleView();

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.tVUserName);
        TextView navEmail = headerView.findViewById(R.id.textView3);
        navUsername.setText(Users.loggedOnUser.getFullName());
        navEmail.setText(Users.loggedOnUser.getEmail());

        searchView = findViewById(R.id.searchViewUsers);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return false;
            }
        });
    }

    private void filterUsers(String query) {
        if (query.isEmpty()) {
            filteredUsers = Users.getUsers();
        } else {
            filteredUsers = Users.getUsers()
                    .stream()
                    .filter(user -> user.getFullName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }
       userAdapter.updateUsers(filteredUsers);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_home){
            Intent intent = new Intent(ManageUsersActivity.this, HomeActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        if (menuItem.getItemId() == R.id.nav_new_tree_plant){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (menuItem.getItemId() == R.id.nav_planted_trees){
            if (Users.loggedOnUser.isAdmin()) {
                Intent intent = new Intent(ManageUsersActivity.this, PlantsHistoryAdminActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ManageUsersActivity.this, PlantsHistoryUserActivity.class);
                startActivity(intent);
            }
        }
        if (menuItem.getItemId() == R.id.nav_account_center) {
            Intent intent = new Intent(ManageUsersActivity.this, InfoUpdateActivity.class);
            startActivity(intent);
        }
        if (menuItem.getItemId() == R.id.nav_log_out){
            LogoutDialogBoxActivity logoutDialogBoxActivity = new LogoutDialogBoxActivity(this);
            logoutDialogBoxActivity.show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initButtons() {
        btnSaveChangesManageUsers = findViewById(R.id.btnSaveChangesManageUsers);
        btnDeleteUserManageUsers = findViewById(R.id.btnDeleteUserManageUsers);
        cBisAdmin = findViewById(R.id.cBisAdmin);
        btnSaveChangesManageUsers.setOnClickListener(this);
        btnDeleteUserManageUsers.setOnClickListener(this);
        cBisAdmin.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    void initTextViews() {
        tvUserCounter = findViewById(R.id.tvUserCounter);
        tvUserCounter.setText(Users.getUsers().size() + " users in total");
    }

    void initRecycleView() {
        recycleView = findViewById(R.id.rVusersList);
        recycleView.setVisibility(View.VISIBLE); // GONE

        itemClick();
        userAdapter = new RecycleAdapterUsers(filteredUsers, listener, this);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        recycleView.setLayoutManager(layoutManager2);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setAdapter(userAdapter);
    }

    private void itemClick() {
        listener = new RecycleAdapterUsers.RecycleViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                cBisAdmin.setChecked(false);
                chosenUser = filteredUsers.get(position);
                Users.setChosenUser(chosenUser);
                if (Users.chosenUser.isAdmin())
                    cBisAdmin.setChecked(true);
            }
        };
    }

    public void updateUser(User chosenUser) {
        @SuppressLint("DefaultLocale")
        String UpdateUser = String.format("UPDATE users SET isAdmin = %d WHERE id = %d ",
                chosenUser.isAdmin() ? 1 : 0, chosenUser.getID());
        RestApi.sqlCommand(UpdateUser);
    }

    @Override
    public void onClick(View view) {
        if (chosenUser != null) {
            if (btnSaveChangesManageUsers.isPressed()) {
                if (cBisAdmin.isChecked()) {
                    chosenUser.setIsAdmin(1);
                    updateUser(chosenUser);
                    Toast.makeText(context,chosenUser.getFirstName() + " is now admin", Toast.LENGTH_SHORT).show();
                    searchView.setQuery("", false);
                    cBisAdmin.setChecked(false);
                } else {
                    if (chosenUser.getID() == 1) {
                        Toast.makeText(context, "Can not remove admin role", Toast.LENGTH_SHORT).show();
                    }
                    else if (chosenUser.getID() == Users.loggedOnUser.getID()) {
                        chosenUser.setIsAdmin(0);
                        updateUser(chosenUser);
                        Users.userLogout(this);
                    } else {
                        chosenUser.setIsAdmin(0);
                        updateUser(chosenUser);
                        Toast.makeText(context,chosenUser.getFirstName() + " is no longer an admin", Toast.LENGTH_SHORT).show();
                        searchView.setQuery("", false);
                        cBisAdmin.setActivated(false);
                    }
                }
            }
            if (btnDeleteUserManageUsers.isPressed()) {
                if (chosenUser.getID() == 1)
                    Toast.makeText(this, "Cannot delete user", Toast.LENGTH_SHORT).show();
                else {
                    DeleteUserDialogBoxActivity deleteUserDialogBoxActivity = new DeleteUserDialogBoxActivity(this, chosenUser.getID());
                    deleteUserDialogBoxActivity.show();
                    searchView.setQuery("", false);
                    cBisAdmin.setActivated(false);
                    Utils.importUsers();
                }
            }
        }
        else
            Toast.makeText(this, "Please select a user", Toast.LENGTH_SHORT).show();
    }
}
