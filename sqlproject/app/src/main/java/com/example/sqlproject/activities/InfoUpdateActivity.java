package com.example.sqlproject.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlproject.R;
import com.example.sqlproject.RestApi;
import com.example.sqlproject.entities.User;
import com.example.sqlproject.entities.Users;

public class InfoUpdateActivity extends AppCompatActivity implements View.OnClickListener{

    Context context;
    EditText etFirstName, etLastName, eTeMail, etPhoneNumber, etPassword, etConfirmPassword;
    Button btnSaveChanges, btnDeleteUser;
    String firstName = "", lastName = "", phoneNumber = "", eMail = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_update_activity);

        context = this;

        initButtons();
        initEditTexts();
    }

    void initButtons() {
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);
        btnSaveChanges.setOnClickListener(this);
        btnDeleteUser.setOnClickListener(this);
    }

    void initEditTexts() {
        etFirstName = findViewById(R.id.eTfirstNameUpdate);
        etLastName = findViewById(R.id.eTlastNameUpdate);
        eTeMail = findViewById(R.id.eTeMailUpdate);
        etPhoneNumber = findViewById(R.id.eTphoneNumberUpdate);
        etPassword = findViewById(R.id.eTpasswordUpdate);
        etConfirmPassword = findViewById(R.id.eTconfirmPasswordUpdate);

        etFirstName.setText(Users.loggedOnUser.getFirstName());
        etLastName.setText(Users.loggedOnUser.getLastName());
        eTeMail.setText(Users.loggedOnUser.getEmail());
        etPhoneNumber.setText(Users.loggedOnUser.getPhoneNumber());
        etPassword.setText(Users.loggedOnUser.getPassword());
        etConfirmPassword.setText(Users.loggedOnUser.getPassword());
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;

        if (btnSaveChanges.isPressed()) {
            if (!etFirstName.getText().toString().isEmpty()) {
                if (etFirstName.getText().toString().length() < 2) {
                    Toast.makeText(this, "First name is too short", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
            }
            if (!etLastName.getText().toString().isEmpty()) {
                if (etLastName.getText().toString().length() < 2) {
                    Toast.makeText(this, "Last name is too short", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
            }
            if (!eTeMail.getText().toString().isEmpty()) {
                if (!eTeMail.getText().toString().contains("@") && !eTeMail.getText().toString().contains(".com")) {
                    Toast.makeText(this, "Invalid eMail address", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
            }
            if (!etPhoneNumber.getText().toString().isEmpty()) {
                if (etPhoneNumber.getText().toString().length() != 10) {
                    Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
            }
            if (!etPassword.getText().toString().isEmpty() || !etConfirmPassword.getText().toString().isEmpty()) {
                if (etPassword.getText().toString().length() < 3) {
                    Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show();
                    if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString()))
                        Toast.makeText(this, "The passwords don't match", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
            }

            if (valid) {
                firstName = etFirstName.getText().toString();
                lastName = etLastName.getText().toString();
                phoneNumber = etPhoneNumber.getText().toString();
                eMail = eTeMail.getText().toString();
                password = etPassword.getText().toString();
                int loggedOnUserID = Users.loggedOnUser.getID();

                @SuppressLint("DefaultLocale")
                String updateUser = String.format("UPDATE users SET " +
                                "firstName = COALESCE('%s', firstName)," +
                                "lastName = COALESCE('%s', lastName)," +
                                "phoneNumber = COALESCE('%s', phoneNumber)," +
                                "eMail = COALESCE('%s', eMail)," +
                                "password = COALESCE('%s', password) " +
                                "WHERE id = %d",
                        firstName, lastName, phoneNumber, eMail, password, loggedOnUserID);
                String res = RestApi.sqlCommand(updateUser);

                if (!res.trim().isEmpty()) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Info was changed successfully", Toast.LENGTH_SHORT).show();
                    User updatedUser = new User(firstName, lastName, phoneNumber, eMail, password);
                    Users.setLoggedOnUser(updatedUser);
                }
            }
        }

        if (btnDeleteUser.isPressed()) {
            if (Users.loggedOnUser.getID() == 1)
                Toast.makeText(this, "Can not delete user", Toast.LENGTH_SHORT).show();
            else {
                DeleteUserDialogBoxActivity deleteUserDialogBoxActivity = new DeleteUserDialogBoxActivity(this, Users.loggedOnUser.getID());
                deleteUserDialogBoxActivity.show();
            }
        }
    }
}