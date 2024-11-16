package com.example.sqlproject.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlproject.R;
import com.example.sqlproject.RestApi;
import com.example.sqlproject.SaveUserToFile;
import com.example.sqlproject.Utils;
import com.example.sqlproject.entities.User;
import com.example.sqlproject.entities.Users;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etPasswordReg, etConfirmPasswordReg, etMailReg, etPhoneNumber, etFirstNameReg, etLastNameReg;
    Button btnRegReg;
    String firstName = "", lastName = "", phoneNumber = "", eMail = "", password = "", plantCount = "";
    int isAdmin = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        Users.getUsers();

        initButtons();
        initEditTexts();
    }

    void initButtons() {
        btnRegReg = findViewById(R.id.btnRegReg);
        btnRegReg.setOnClickListener(this);
    }

    void initEditTexts() {
        etPasswordReg = findViewById(R.id.eTpasswordReg);
        etConfirmPasswordReg = findViewById(R.id.eTconfirmPasswordReg);
        etMailReg = findViewById(R.id.eTeMailReg);
        etPhoneNumber = findViewById(R.id.eTphoneNumberReg);
        etFirstNameReg = findViewById(R.id.eTfirstNameReg);
        etLastNameReg = findViewById(R.id.eTlastNameReg);
    }

    @Override
    public void onClick(View view) {

        boolean valid = true;

        if (btnRegReg.isPressed()) {
            if (etFirstNameReg.getText().toString().length() < 2) {
                Toast.makeText(this, "First name is too short", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            if(etLastNameReg.getText().toString().length() < 2) {
                Toast.makeText(this, "Last name is too short", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            if (!etMailReg.getText().toString().contains("@") || !etMailReg.getText().toString().contains(".com")) {
                Toast.makeText(this, "Invalid eMail address", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            if (etPhoneNumber.getText().toString().length() != 10) {
                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            if (etPasswordReg.getText().toString().length() < 3) {
                Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            if (!etPasswordReg.getText().toString().equals(etConfirmPasswordReg.getText().toString())) {
                Toast.makeText(this, "The passwords don't match", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        if (valid) {
            firstName = etFirstNameReg.getText().toString();
            lastName = etLastNameReg.getText().toString();
            phoneNumber = etPhoneNumber.getText().toString();
            eMail = etMailReg.getText().toString();
            password = etPasswordReg.getText().toString();
            plantCount = "0";
            isAdmin = 0;

            Users users = Users.getUsers();
            String newUserId = String.valueOf(users.get(users.size() - 1).getID() + 1);

            User newUser = new User(Integer.parseInt(newUserId) ,firstName, lastName, phoneNumber, eMail, password, Integer.parseInt(plantCount), Utils.getCurrentDate(), isAdmin);

            if (Integer.parseInt(newUserId) == 1) {
                newUser.setIsAdmin(1);
            }

            @SuppressLint("DefaultLocale")
            String insertUser = String.format("INSERT INTO users (id, firstName, lastName, phoneNumber, eMail, password, plantCounter, joinDate, isAdmin) " +
                    "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d)  ",
                    newUser.getID(),
                    newUser.getFirstName(),
                    newUser.getLastName(),
                    newUser.getPhoneNumber(),
                    newUser.getEmail(),
                    newUser.getPassword(),
                    newUser.getPlantCounter(),
                    newUser.getJoinDate(),
                    newUser.isAdmin() ? 1 : 0);

            String res = RestApi.sqlCommand(insertUser);

            if (!res.trim().isEmpty()) {
                Toast.makeText(this, "Email is already in use", Toast.LENGTH_SHORT).show();
            } else {
                Users.setLoggedOnUser(newUser);
                Toast.makeText(RegisterActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
                finishAffinity();
                SaveUserToFile.saveString(RegisterActivity.this, "user", newUser);
            }
        }
    }
}