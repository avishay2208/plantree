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
import com.example.sqlproject.entities.Tree;
import com.example.sqlproject.entities.Trees;
import com.example.sqlproject.entities.Users;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class ManageTreesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener {

    Context context;
    Button btnAddTree, btnEditTree, btnDeleteTree, btnSaveTree, btnCancelSave;
    EditText etAddTreeType, etAddTreeStock, etAddTreePrice, etAddTreeImageUrl;
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;
    String treeType, treeImageUrl;
    int treeStock;
    double treePrice;
    private boolean valid = true, isILS = true;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;

    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_trees_activity);

        context = this;

        drawerLayout = findViewById(R.id.drawerLayoutAdminTreesManagement);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarAdminTreesManagement);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isILS = extras.getBoolean("isILS");
        }

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
        btnAddTree = findViewById(R.id.btnAddTree);
        btnEditTree = findViewById(R.id.btnEditTree);
        btnDeleteTree = findViewById(R.id.btnDeleteTree);
        btnSaveTree = findViewById(R.id.btnSaveNewTree);
        btnCancelSave = findViewById(R.id.btnCancelSaveNewTree);
        btnAddTree.setOnClickListener(this);
        btnEditTree.setOnClickListener(this);
        btnDeleteTree.setOnClickListener(this);
        btnSaveTree.setOnClickListener(this);
        btnCancelSave.setOnClickListener(this);

        btnSaveTree.setVisibility(View.GONE);
        btnCancelSave.setVisibility(View.GONE);
    }

    void initEditTexts() {
        etAddTreeType = findViewById(R.id.etAddTreeType);
        etAddTreePrice = findViewById(R.id.etAddTreePrice);
        etAddTreeImageUrl = findViewById(R.id.etAddTreeImageUrl);
        etAddTreeStock = findViewById(R.id.etAddTreeStock);

        etAddTreeType.setVisibility(View.GONE);
        etAddTreeStock.setVisibility(View.GONE);
        etAddTreePrice.setVisibility(View.GONE);
        etAddTreeImageUrl.setVisibility(View.GONE);
        etAddTreeStock.setVisibility(View.GONE);
    }

    void initTextInput() {
        textInputLayout = findViewById(R.id.inputLayoutManageTrees);
        autoCompleteTextView = findViewById(R.id.inputTVmanageTrees);

        Utils.importTrees();

        List<String> treesList = Trees.getTreeTypesOnly();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_drop_down, treesList);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trees.setChosenTree(Trees.getTreeByType(treesList.get(position)));
                textInputLayout.setErrorEnabled(false);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_home){
            Intent intent = new Intent(ManageTreesActivity.this, HomeActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        if (menuItem.getItemId() == R.id.nav_new_tree_plant) {
            Intent intent = new Intent(ManageTreesActivity.this, TreesListActivity.class);
            startActivity(intent);
        }
        if (menuItem.getItemId() == R.id.nav_planted_trees) {
            if (Users.loggedOnUser.isAdmin()) {
                Intent intent = new Intent(ManageTreesActivity.this, PlantsHistoryAdminActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ManageTreesActivity.this, PlantsHistoryUserActivity.class);
                startActivity(intent);
            }
        }
        if (menuItem.getItemId() == R.id.nav_account_center) {
            Intent intent = new Intent(ManageTreesActivity.this, InfoUpdateActivity.class);
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
        if(!autoCompleteTextView.getText().toString().isEmpty()) {
            if (btnEditTree.isPressed()) {
                Intent intent = new Intent(context, TreeDataPreviewActivity.class);
                intent.putExtra("type", Trees.chosenTree.getType());
                intent.putExtra("isILS", isILS);
                context.startActivity(intent);
            }
            if (btnDeleteTree.isPressed()) {
                DeleteTreeDialogBoxActivity deleteTreeDialogBoxActivity = new DeleteTreeDialogBoxActivity(context, Trees.chosenTree.getID());
                deleteTreeDialogBoxActivity.show();
            }
        } else
            textInputLayout.setError("Please select a tree");

        if (btnAddTree.isPressed()) {
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setVisibility(View.GONE);
            autoCompleteTextView.setVisibility(View.GONE);
            btnEditTree.setVisibility(View.GONE);
            btnDeleteTree.setVisibility(View.GONE);
            btnAddTree.setVisibility(View.GONE);

            etAddTreeType.setVisibility(View.VISIBLE);
            etAddTreeStock.setVisibility(View.VISIBLE);
            etAddTreePrice.setVisibility(View.VISIBLE);
            etAddTreeImageUrl.setVisibility(View.VISIBLE);
            btnSaveTree.setVisibility(View.VISIBLE);
            btnCancelSave.setVisibility(View.VISIBLE);
        }

        if (btnSaveTree.isPressed()) {
            textInputLayout.setErrorEnabled(false);
            if (etAddTreeType.getText().toString().isEmpty()) {
                etAddTreeType.setError("Invalid Type");
                valid = false;
            }
            if (etAddTreeStock.getText().toString().isEmpty()) {
                etAddTreeStock.setError("Invalid Stock");
                valid = false;
            }
            if (etAddTreePrice.getText().toString().isEmpty() || etAddTreePrice.getText().toString().contains("₪")) {
                etAddTreePrice.setError("Invalid Price");
                valid = false;
            }
            if (etAddTreeImageUrl.getText().toString().isEmpty()) {
                etAddTreeImageUrl.setError("Invalid URL");
                valid = false;
            }

            if (valid) {
                Trees trees = Trees.getTrees();
                String newTreeId = String.valueOf(trees.get(trees.size() - 1).getID() + 1);
                treeType = etAddTreeType.getText().toString();
                treeStock = Integer.parseInt(etAddTreeStock.getText().toString());
                treePrice = Double.parseDouble(etAddTreePrice.getText().toString());
                treeImageUrl = etAddTreeImageUrl.getText().toString();
                Tree newTree = new Tree(Integer.parseInt(newTreeId), treeType, treeStock,treePrice, treeImageUrl);

                @SuppressLint("DefaultLocale")
                String insertTree = String.format("INSERT INTO trees (id, type, stock, price, imageUrl) " +
                                "VALUES ('%s', '%s', %d, %f, '%s')  ",
                        newTree.getID(),
                        newTree.getType(),
                        newTree.getStock(),
                        newTree.getPrice(),
                        newTree.getImageUrl());

                String res = RestApi.sqlCommand(insertTree);
                if (!res.trim().isEmpty())
                    Toast.makeText(this, "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Tree added successfully!", Toast.LENGTH_SHORT).show();

                initTextInput();

                textInputLayout.setErrorEnabled(false);
                etAddTreeType.setVisibility(View.GONE);
                etAddTreeStock.setVisibility(View.GONE);
                etAddTreePrice.setVisibility(View.GONE);
                etAddTreeImageUrl.setVisibility(View.GONE);
                btnSaveTree.setVisibility(View.GONE);
                btnCancelSave.setVisibility(View.GONE);

                btnAddTree.setVisibility(View.VISIBLE);
                textInputLayout.setVisibility(View.VISIBLE);
                autoCompleteTextView.setVisibility(View.VISIBLE);
                btnEditTree.setVisibility(View.VISIBLE);
                btnDeleteTree.setVisibility(View.VISIBLE);
            }
        }

        if (btnCancelSave.isPressed()) {
            textInputLayout.setErrorEnabled(false);
            etAddTreeType.setVisibility(View.GONE);
            etAddTreeStock.setVisibility(View.GONE);
            etAddTreePrice.setVisibility(View.GONE);
            etAddTreeImageUrl.setVisibility(View.GONE);
            btnSaveTree.setVisibility(View.GONE);
            btnCancelSave.setVisibility(View.GONE);

            btnAddTree.setVisibility(View.VISIBLE);
            textInputLayout.setVisibility(View.VISIBLE);
            autoCompleteTextView.setVisibility(View.VISIBLE);
            btnEditTree.setVisibility(View.VISIBLE);
            btnDeleteTree.setVisibility(View.VISIBLE);
        }
    }
}
