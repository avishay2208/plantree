package com.example.sqlproject.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.sqlproject.R;

import java.util.Objects;

public class PurchaseDialogBoxActivity extends Dialog implements View.OnClickListener {

    Context context;
    Button btnDialogClose;

    public PurchaseDialogBoxActivity(@NonNull Context context) {
        super(context);
        this.context = context;

        Objects.requireNonNull(this.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_bg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_dialog_box);

        initButtons();
    }

    void initButtons() {
        btnDialogClose = findViewById(R.id.btnDialogClose);
        btnDialogClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (btnDialogClose.isPressed()) {
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
            ((Activity) context).finishAffinity();
            dismiss();
        }
    }
}
