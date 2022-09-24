package com.group3.project4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.project4.R;

public class MainActivity extends AppCompatActivity implements
    LoginFragment.IListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new LoginFragment(), "LoginFragment")
                .commit();
    }

    @Override
    public void signup() {

    }

    @Override
    public void loginSuccess() {

    }
}