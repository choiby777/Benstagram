package com.cby.benstagram.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;

import com.cby.benstagram.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupClickEvents();

        Log.d(TAG, "onCreate: Register Activity Created");
    }

    private void setupClickEvents() {
        AppCompatButton registerButton = findViewById(R.id.btn_register);
        registerButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.btn_register) {
            finish();
        }
    }
}
