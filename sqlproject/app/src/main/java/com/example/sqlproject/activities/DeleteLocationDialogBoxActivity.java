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

import java.util.Objects;

public class DeleteLocationDialogBoxActivity extends Dialog implements View.OnClickListener {
    Context context;
    Button btnDialogDeleteLocation, btnDialogCancelLocationDelete;
    int locationID;

    public DeleteLocationDialogBoxActivity (@NonNull Context context, int locationID) {
        super(context);
        this.context = context;
        this.locationID = locationID;
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_bg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_location_dialog_box_activity);
        initButtons();
    }

    void initButtons() {
        btnDialogDeleteLocation = findViewById(R.id.btnDialogDeleteLocation);
        btnDialogCancelLocationDelete = findViewById(R.id.btnDialogCancelDeleteLocation);

        btnDialogDeleteLocation.setOnClickListener(this);
        btnDialogCancelLocationDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (btnDialogDeleteLocation.isPressed()) {
            @SuppressLint("DefaultLocale")
            String deleteLocation = String.format("DELETE FROM locations WHERE id = %d ", this.locationID);
            RestApi.sqlCommand(deleteLocation);
            Toast.makeText(context, "Location deleted successfully", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        else
            dismiss();
    }
}
