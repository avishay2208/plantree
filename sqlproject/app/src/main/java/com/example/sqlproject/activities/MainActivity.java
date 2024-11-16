package com.example.sqlproject.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlproject.R;
import com.example.sqlproject.SaveUserToFile;
import com.example.sqlproject.entities.User;
import com.example.sqlproject.entities.Users;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignIn, btnSignUp;
    TextView tvSignIn, tvRegister;
    ImageView ivLogo;
    EditText eTeMail, etPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User savedUser = SaveUserToFile.getValue(this, "user");
        if (savedUser != null) {
            Users.setLoggedOnUser(savedUser);
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finishAffinity();
        }

        initButtons();
        initEditTexts();
        initImageViews();
        initTextViews();
        initProgressBars();
    }

    void initButtons() {
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    void initEditTexts() {
        tvSignIn = findViewById(R.id.textView2);
        tvRegister = findViewById(R.id.textView);
    }

    void initImageViews() {
        ivLogo = findViewById(R.id.ivLogo);
    }

    void initTextViews() {
        eTeMail = findViewById(R.id.eTeMailSignIn);
        etPassword = findViewById(R.id.eTpasswordSignIn);
    }

    void initProgressBars() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (btnSignIn.isPressed()) {
            progressBar.setVisibility(View.VISIBLE);
            eTeMail.setVisibility(View.GONE);
            etPassword.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.GONE);
            btnSignUp.setVisibility(View.GONE);
            tvSignIn.setVisibility(View.GONE);
            tvRegister.setVisibility(View.GONE);

            new SignInTask().execute(eTeMail.getText().toString(), etPassword.getText().toString());
        }

        if (btnSignUp.isPressed()) {
            Intent intent2 = new Intent(this, RegisterActivity.class);
            startActivity(intent2);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class SignInTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            Users.getUsers();
            User loggedOnUser = Users.getLoggedOnUserByMail(email);

            if (loggedOnUser != null && password.equals(loggedOnUser.getPassword())) {
                return loggedOnUser;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(User loggedOnUser) {
            progressBar.setVisibility(View.GONE);

            if (loggedOnUser != null) {
                Users.setLoggedOnUser(loggedOnUser);
                Toast.makeText(MainActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finishAffinity();
                SaveUserToFile.saveString(MainActivity.this, "user", loggedOnUser);
            } else {
                Toast.makeText(MainActivity.this, "Wrong Email or password", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                eTeMail.setVisibility(View.VISIBLE);
                etPassword.setVisibility(View.VISIBLE);
                btnSignIn.setVisibility(View.VISIBLE);
                btnSignUp.setVisibility(View.VISIBLE);
                tvSignIn.setVisibility(View.VISIBLE);
                tvRegister.setVisibility(View.VISIBLE);
            }
        }
    }
}