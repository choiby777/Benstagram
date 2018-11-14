package com.cby.benstagram.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cby.benstagram.Home.HomeActivity;
import com.cby.benstagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();


        if (user == null){
            Intent resultIntent = new Intent();
            resultIntent.putExtra(getString(R.string.calling_activity) , getString(R.string.startup_activity));
            resultIntent.putExtra(getString(R.string.is_user_valid) , false);
            setResult(RESULT_OK,resultIntent);
            finish();
        }else{


            user.reload().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d(TAG, "onFailure: ");

                    if (e instanceof FirebaseAuthInvalidUserException) {
                        String errorCode = ((FirebaseAuthInvalidUserException) e).getErrorCode();
                        String message = ((FirebaseAuthInvalidUserException) e).getMessage();
                    }

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(getString(R.string.calling_activity) , getString(R.string.startup_activity));
                    resultIntent.putExtra(getString(R.string.is_user_valid) , false);
                    setResult(RESULT_OK,resultIntent);
                    finish();
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: user is valid");

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(getString(R.string.calling_activity) , getString(R.string.startup_activity));
                    resultIntent.putExtra(getString(R.string.is_user_valid) , true);
                    setResult(RESULT_OK,resultIntent);
                    finish();

                }
            });
        }
    }
}
