package com.example.sqlproject.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sqlproject.R;
import com.example.sqlproject.RestApi;
import com.example.sqlproject.entities.Users;

import java.util.Objects;

public class DeleteUserDialogBoxActivity extends Dialog implements View.OnClickListener {

    Context context;
    Button btnDialogDeleteUser, btnDialogCancelUserDelete;
    int userID;

    public DeleteUserDialogBoxActivity(@NonNull Context context, int userID) {
        super(context);
        this.context = context;
        this.userID = userID;
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_bg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_user_dialog_box_activity);

        initButtons();
    }

    void initButtons() {
        btnDialogDeleteUser = findViewById(R.id.btnDialogDeleteUser);
        btnDialogCancelUserDelete = findViewById(R.id.btnDialogCancelDeleteUser);

        btnDialogDeleteUser.setOnClickListener(this);
        btnDialogCancelUserDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (btnDialogDeleteUser.isPressed()) {
            @SuppressLint("DefaultLocale")
            String deleteUser = String.format("DELETE FROM users WHERE id = %d ", userID);
            RestApi.sqlCommand(deleteUser);
            @SuppressLint("DefaultLocale")
            String deletePlants = String.format("DELETE FROM plants WHERE userID = %d ", userID);
            RestApi.sqlCommand(deletePlants);
            Toast.makeText(context, "Account deleted successfully", Toast.LENGTH_SHORT).show();
            if (userID == Users.loggedOnUser.getID()) {
                Users.userLogout(context);
            }
            dismiss();
        }
        else
            dismiss();
    }
}
