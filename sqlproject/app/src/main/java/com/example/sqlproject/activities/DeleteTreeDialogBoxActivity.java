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

public class DeleteTreeDialogBoxActivity extends Dialog implements View.OnClickListener {

    Context context;
    Button btnDialogDeleteTree, btnDialogCancelTreeDelete;
    int treeID;

    public DeleteTreeDialogBoxActivity (@NonNull Context context, int treeID) {
        super(context);
        this.context = context;
        this.treeID = treeID;
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_bg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_tree_dialog_box_activity);
        initButtons();
    }

    void initButtons() {
        btnDialogDeleteTree = findViewById(R.id.btnDialogDeleteTree);
        btnDialogCancelTreeDelete = findViewById(R.id.btnDialogCancelDeleteTree);

        btnDialogDeleteTree.setOnClickListener(this);
        btnDialogCancelTreeDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (btnDialogDeleteTree.isPressed()) {
            @SuppressLint("DefaultLocale")
            String deleteTree = String.format("DELETE FROM trees WHERE id = %d ", this.treeID);
            RestApi.sqlCommand(deleteTree);
            Toast.makeText(context, "Tree deleted successfully", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        else
            dismiss();
    }
}
