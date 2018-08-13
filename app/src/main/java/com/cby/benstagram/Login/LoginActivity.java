package com.cby.benstagram.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cby.benstagram.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private Context mContext  = LoginActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupClickEvents();

        Log.d(TAG, "onCreate: Login Activity Created");
    }

    private void setupClickEvents() {
        AppCompatButton loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(this);

        TextView signUPText = findViewById(R.id.txt_sign_up);
        signUPText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.btn_login){
            finish();
        }else if (viewId == R.id.txt_sign_up){
            Intent intent = new Intent(mContext , RegisterActivity.class);
            startActivity(intent);
        }
    }
}
