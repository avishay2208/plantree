package com.example.sqlproject.activities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sqlproject.R;
import com.example.sqlproject.RestApi;
import com.example.sqlproject.Utils;
import com.example.sqlproject.entities.Locations;
import com.example.sqlproject.entities.Tree;
import com.example.sqlproject.entities.Trees;
import com.example.sqlproject.entities.Users;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class TreeDataPreviewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Context context;
    private Tree tree;
    private ImageView iV;
    private TextView tvStock;
    private EditText etType, etStock, etPrice, etUrl;
    private Button btnPlant, btnSeeMap, btnUpdateData, btnSaveData, btnCancelSave;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView autoCompleteTextView;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;

    public Toolbar toolbar;
    private boolean valid = true, isILS = true;
    int treeID;
    private double usd_Currency = 1, treePriceILS = 1;
    DecimalFormat df = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_data_preview_activity);
        context = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isILS = extras.getBoolean("isILS");
            usd_Currency = extras.getDouble("usd_Currency");
        }

        drawerLayout = findViewById(R.id.drawerLayoutTreeDataPreview);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarTreeDataPreview);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Trees.getTrees();
        initButtons();
        initEditTexts();
        initTextViews();
        initImageViews();
        initTextInput();
        findTree();

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.tVUserName);
        TextView navEmail = headerView.findViewById(R.id.textView3);
        navUsername.setText(Users.loggedOnUser.getFullName());
        navEmail.setText(Users.loggedOnUser.getEmail());

        int grayColor = ContextCompat.getColor(this, R.color.gray);

        if (Trees.chosenTree.getStock() == 0) {
            btnPlant.setClickable(false);
            btnPlant.setBackgroundColor(grayColor);
        }

        if (!Users.loggedOnUser.isAdmin()) {
            btnUpdateData.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    public void findTree() {
        Intent intent = getIntent();
        String treeType = intent.getStringExtra("type");
        tree = Trees.getTreeByType(treeType);
        treeID = tree.getID();
        Picasso.get().load(tree.getImageUrl()).into(iV);
        etType.setText(tree.getType());
        if (isILS)
            etPrice.setText((tree.getPrice() + "₪"));
        else {
            treePriceILS = tree.getPrice() / usd_Currency;
            etPrice.setText((df.format(tree.getPrice()) + "$"));
        }
        tvStock.setText(Trees.chosenTree.getStock() + " in stock");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_home){
            Intent intent = new Intent(TreeDataPreviewActivity.this, HomeActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        if (menuItem.getItemId() == R.id.nav_new_tree_plant) {
            Intent intent = new Intent(TreeDataPreviewActivity.this, TreesListActivity.class);
            startActivity(intent);
        }
        if (menuItem.getItemId() == R.id.nav_planted_trees) {
            if (Users.loggedOnUser.isAdmin()) {
                Intent intent = new Intent(TreeDataPreviewActivity.this, PlantsHistoryAdminActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(TreeDataPreviewActivity.this, PlantsHistoryUserActivity.class);
                startActivity(intent);
            }
        }
        if (menuItem.getItemId() == R.id.nav_account_center) {
            Intent intent = new Intent(TreeDataPreviewActivity.this, InfoUpdateActivity.class);
            startActivity(intent);
        }
        if (menuItem.getItemId() == R.id.nav_log_out){
            LogoutDialogBoxActivity logoutDialogBoxActivity = new LogoutDialogBoxActivity(this);
            logoutDialogBoxActivity.show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initImageViews() {
        iV = findViewById(R.id.iVTree);
    }

    private void initEditTexts() {
        etType = findViewById(R.id.etTypeOfTree);
        etPrice = findViewById(R.id.etPriceOfTree);
        etUrl = findViewById(R.id.etImageUrlOfTree);
        etStock = findViewById(R.id.etStockOfTree);

        etType.setInputType(InputType.TYPE_NULL);
        etPrice.setInputType(InputType.TYPE_NULL);
        etUrl.setVisibility(View.GONE);
        etStock.setVisibility(View.GONE);
    }

    private void initTextViews() {
        tvStock = findViewById(R.id.tvStock);
    }

    private void initTextInput() {
        textInputLayout = findViewById(R.id.inputLayout);
        autoCompleteTextView = findViewById(R.id.inputTV);

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

    private void initButtons() {
        btnPlant = findViewById(R.id.btnPlant);
        btnSeeMap = findViewById(R.id.btnSeeMap);
        btnUpdateData = findViewById(R.id.btnUpdateTreeData);
        btnSaveData = findViewById(R.id.btnSaveTreeData);
        btnCancelSave = findViewById(R.id.btnCancelUpdateTree);
        btnPlant.setOnClickListener(this);
        btnSeeMap.setOnClickListener(this);
        btnUpdateData.setOnClickListener(this);
        btnSaveData.setOnClickListener(this);
        btnCancelSave.setOnClickListener(this);

        btnSaveData.setVisibility(View.GONE);
        btnCancelSave.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        textInputLayout.setErrorEnabled(false);

        if (!autoCompleteTextView.getText().toString().isEmpty()) {
            if (btnSeeMap.isPressed()) {
                MapViewDialogBoxActivity mapViewDialogBoxActivity = new MapViewDialogBoxActivity(this);
                mapViewDialogBoxActivity.show();
            }

            if (btnPlant.isPressed()) {
                Intent intent = new Intent(context, PaymentForTreesActivity.class);
                intent.putExtra("isILS", isILS);
                context.startActivity(intent);
            }
        } else
            textInputLayout.setError("Please select an address");

        if (btnUpdateData.isPressed()) {
            textInputLayout.setErrorEnabled(false);
            etType.setInputType(InputType.TYPE_CLASS_TEXT);
            etPrice.setInputType(InputType.TYPE_CLASS_TEXT);
            btnUpdateData.setVisibility(View.GONE);
            btnPlant.setVisibility(View.GONE);
            btnSeeMap.setVisibility(View.GONE);
            textInputLayout.setVisibility(View.GONE);
            btnSaveData.setVisibility(View.VISIBLE);
            btnCancelSave.setVisibility(View.VISIBLE);
            etUrl.setVisibility(View.VISIBLE);
            etStock.setVisibility(View.VISIBLE);
            tvStock.setVisibility(View.GONE);

            etType.setText(tree.getType());
            if (isILS)
                etPrice.setText(String.valueOf(tree.getPrice()));
            else
                etPrice.setText(df.format(treePriceILS));

            etStock.setText(String.valueOf(Trees.chosenTree.getStock()));
            etUrl.setText(Trees.chosenTree.getImageUrl());
        }

        if (btnCancelSave.isPressed()) {
            textInputLayout.setErrorEnabled(false);
            etType.setInputType(InputType.TYPE_NULL);
            etPrice.setInputType(InputType.TYPE_NULL);
            etUrl.setVisibility(View.GONE);
            etStock.setVisibility(View.GONE);
            btnSaveData.setVisibility(View.GONE);
            btnCancelSave.setVisibility(View.GONE);
            btnSeeMap.setVisibility(View.VISIBLE);
            textInputLayout.setVisibility(View.VISIBLE);
            btnPlant.setVisibility(View.VISIBLE);
            btnUpdateData.setVisibility(View.VISIBLE);
            tvStock.setVisibility(View.VISIBLE);

            etType.setText(tree.getType());
            if (isILS)
                etPrice.setText((tree.getPrice() + "₪"));
            else
                etPrice.setText((df.format(treePriceILS * usd_Currency) + "$"));
            etUrl.setText(Trees.chosenTree.getImageUrl());
            etStock.setText(String.valueOf(Trees.chosenTree.getStock()));
        }

        if (btnSaveData.isPressed()) {
            textInputLayout.setErrorEnabled(false);
            if (etType.getText().toString().isEmpty()) {
                etType.setError("Invalid Type");
                valid = false;
            }
            if (etPrice.getText().toString().isEmpty() || etPrice.getText().toString().contains("₪") || etPrice.getText().toString().contains("$")) {
                etPrice.setError("Invalid Price");
                valid = false;
            }
            if (etStock.getText().toString().isEmpty()) {
                etPrice.setError("Invalid Stock");
                valid = false;
            }
            if (etUrl.getText().toString().isEmpty()) {
                etPrice.setError("Invalid URL");
                valid = false;
            }

            String type = etType.getText().toString();
            double price = Double.parseDouble(etPrice.getText().toString());
            int stock = Integer.parseInt(etStock.getText().toString());
            String imageUrl = etUrl.getText().toString();
            int chosenTreeID = Trees.chosenTree.getID();

            if (valid) {
                @SuppressLint("DefaultLocale")
                String updateTree = String.format("UPDATE trees SET " +
                                "type = '%s', " +
                                "stock = %d, " +
                                "price = %f, " +
                                "imageUrl = '%s' " +
                                "WHERE id = %d",
                        type, stock, price, imageUrl, chosenTreeID);

                String res = RestApi.sqlCommand(updateTree);
                if (!res.trim().isEmpty())
                    Toast.makeText(this, "Something went wrong, try again later.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Tree updated successfully!", Toast.LENGTH_SHORT).show();

                Utils.importTrees();

                tree = Trees.getTrees().get(treeID - 1);

                etType.setInputType(InputType.TYPE_NULL);
                etPrice.setInputType(InputType.TYPE_NULL);
                etUrl.setVisibility(View.GONE);
                etStock.setVisibility(View.GONE);
                btnSaveData.setVisibility(View.GONE);
                btnCancelSave.setVisibility(View.GONE);
                textInputLayout.setVisibility(View.VISIBLE);
                btnSeeMap.setVisibility(View.VISIBLE);
                btnPlant.setVisibility(View.VISIBLE);
                btnUpdateData.setVisibility(View.VISIBLE);
                tvStock.setVisibility(View.VISIBLE);

                etType.setText(tree.getType());

                if (isILS)
                    etPrice.setText((tree.getPrice() + "₪"));
                else
                    etPrice.setText((df.format(treePriceILS * usd_Currency) + "$"));

                etUrl.setText(Trees.chosenTree.getImageUrl());
                etStock.setText(String.valueOf(Trees.chosenTree.getStock()));
            }
        }
    }
}