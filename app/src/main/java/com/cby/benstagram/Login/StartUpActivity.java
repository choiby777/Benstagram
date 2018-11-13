package com.cby.benstagram.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cby.benstagram.Home.HomeActivity;
import com.cby.benstagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class StartUpActivity extends AppCompatActivity {
    private static final String TAG = "StartUpActivity";
    private FirebaseAuth mAuth;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        Log.d(TAG, "onCreate: Login Activity Created");
        mContext = StartUpActivity.this;

        setupFirebaseAuth();
    }

    private void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null){
            Intent intent = new Intent(mContext , LoginActivity.class);
            startActivity(intent);
        }else{
            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: task isSuccessful");

                        Intent intent = new Intent(mContext , HomeActivity.class);
                        intent.putExtra(getString(R.string.calling_activity) , getString(R.string.startup_activity));
                        startActivity(intent);

                    } else {
                        Log.d(TAG, "onComplete: task fail");

                        Intent intent = new Intent(mContext , LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }
    }
}
