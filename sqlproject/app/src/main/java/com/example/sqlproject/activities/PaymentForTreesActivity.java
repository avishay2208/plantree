package com.example.sqlproject.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlproject.CurrencyConverter;
import com.example.sqlproject.R;
import com.example.sqlproject.RestApi;
import com.example.sqlproject.SaveUserToFile;
import com.example.sqlproject.SendSms;
import com.example.sqlproject.Utils;
import com.example.sqlproject.entities.Locations;
import com.example.sqlproject.entities.Plants;
import com.example.sqlproject.entities.Trees;
import com.example.sqlproject.entities.Users;

import java.io.IOException;
import java.text.DecimalFormat;

public class PaymentForTreesActivity extends AppCompatActivity implements View.OnClickListener {

    Activity activity;
    TextView tVcardHolderID, tVcardNumber, tVcardDate, tVcardCVV, tvTotalPrice;
    EditText etCardHolderID, etCardNumber, etCardDate, etCardCVV;
    DecimalFormat df = new DecimalFormat("#.00");
    Button btnPay;
    String currencySymbol;
    boolean isILS = true;
    double treePriceInILS;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_for_planting_trees_activity);

        activity = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isILS = extras.getBoolean("isILS");
        }
        currencySymbol = isILS ? "₪" : "$";


        initEditTexts();
        initTextViews();
        initButtons();

        if (isILS)
            tvTotalPrice.setText("Total Price: " + Trees.chosenTree.getPrice() + "₪");
        else
            tvTotalPrice.setText("Total Price: " + df.format(Trees.chosenTree.getPrice()) + "$");
    }

    void initEditTexts() {
        etCardHolderID = findViewById(R.id.eTcardID);
        etCardNumber = findViewById(R.id.eTcardNumber);
        etCardDate = findViewById(R.id.eTcardDate);
        etCardCVV = findViewById(R.id.eTcardCVV);
        tvTotalPrice = findViewById(R.id.tVtotal);
    }

    void initTextViews() {
        tVcardHolderID = findViewById(R.id.tVcardID);
        tVcardNumber = findViewById(R.id.tVcardNumber);
        tVcardDate = findViewById(R.id.tVcardDate);
        tVcardCVV = findViewById(R.id.tVcardCVV);
    }

    void initButtons() {
        btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        boolean valid = true;

        if (btnPay.isPressed()) {
            if (etCardHolderID.getText().toString().length() != 9) {
                Toast.makeText(this, "Incorrect ID", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            if (etCardNumber.getText().toString().length() != 16) {
                Toast.makeText(this, "Incorrect card number", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            if (etCardDate.getText().toString().length() != 5) {
                Toast.makeText(this, "Incorrect Date", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            if (etCardCVV.getText().toString().length() != 3) {
                Toast.makeText(this, "Incorrect CVV", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        if(valid) {
            int updatedUserPlantCounter = Users.loggedOnUser.getPlantCounter() + 1;
            @SuppressLint("DefaultLocale")
            String updateUserPlantsCounter = String.format("UPDATE users SET plantCounter = %d WHERE id = %d", updatedUserPlantCounter, Users.loggedOnUser.getID());
            RestApi.sqlCommand(updateUserPlantsCounter);

            Users.loggedOnUser.setPlantCounter(updatedUserPlantCounter);
            SaveUserToFile.saveString(this, "user", Users.loggedOnUser);

            int updatedTreeStock = Trees.chosenTree.getStock() - 1;
            @SuppressLint("DefaultLocale")
            String updatedTreeStocks = String.format("UPDATE trees SET stock = %d WHERE id = %d", updatedTreeStock, Trees.chosenTree.getID());
            RestApi.sqlCommand(updatedTreeStocks);

            if (!isILS) {
                try {
                    treePriceInILS = (Trees.chosenTree.getPrice() / CurrencyConverter.getUSD_Rate());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else
                treePriceInILS = Trees.chosenTree.getPrice();

            Plants plants = Plants.getPlants();
            String newPlantID = plants.isEmpty() ? "1" : String.valueOf(plants.get(plants.size() - 1).getPlantID() + 1);

            @SuppressLint("DefaultLocale")
            String insertPlant = String.format("INSERT INTO plants (plantID, userID, userName, treeID, treeName, plantAddress, plantDate, price) " +
                    "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%f')",
                    newPlantID,
                    Users.loggedOnUser.getID(),
                    Users.loggedOnUser.getFullName(),
                    Trees.chosenTree.getID(),
                    Trees.chosenTree.getType(),
                    Locations.chosenLocation.getAddress(),
                    Utils.getCurrentDate(),
                    treePriceInILS);

            RestApi.sqlCommand(insertPlant);

            PurchaseDialogBoxActivity purchaseDialogBoxActivity = new PurchaseDialogBoxActivity(this);
            purchaseDialogBoxActivity.show();

            if (!SendSms.hasSMSPermission(this))
                SendSms.askForPermission(this);
            SendSms.sendSMS("+972586602669", "Thank you for your purchase!\nYour " + Trees.chosenTree.getType()
                    + " will be planted within one business day at " + Locations.chosenLocation.getAddress()
                    + ".\nTotal price: " + df.format(Trees.chosenTree.getPrice()) + currencySymbol, this);
            Utils.importTrees();
            Utils.importUsers();
        }
    }
}