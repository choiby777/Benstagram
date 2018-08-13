package com.cby.benstagram.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cby.benstagram.R;
import com.cby.benstagram.Util.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "RegisterActivity";

    private Context mContext = RegisterActivity.this;
    private FirebaseAuth mAuth;
    private EditText mTxtUserEmail , mTxtFullName, mTxtPassword;
    private ProgressBar mProgressBar;
    private Button mRegisterButton;

    private FirebaseHelper mFirebaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseHelper = new FirebaseHelper(mContext);

        initWidgets();
        setupWidgetEvents();

        mProgressBar.setVisibility(View.GONE);

        Log.d(TAG, "onCreate: Register Activity Created");
    }

    private void setupWidgetEvents() {
        mRegisterButton.setOnClickListener(this);
    }

    private void initWidgets() {
        mTxtUserEmail = findViewById(R.id.input_email);
        mTxtFullName = findViewById(R.id.input_fullName);
        mTxtPassword = findViewById(R.id.input_password);
        mProgressBar = findViewById(R.id.progress_register);
        mRegisterButton = findViewById(R.id.btn_register);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        //mAuth.createUserWithEmailAndPassword()
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.btn_register) {

            String email = mTxtUserEmail.getText().toString();
            String userName = mTxtFullName.getText().toString();
            String password = mTxtPassword.getText().toString();

            if (isStringNull(email) || isStringNull(password) || isStringNull(userName)){
                Toast.makeText(mContext, "All fields must be filled out", Toast.LENGTH_SHORT).show();
                return;
            }

            mProgressBar.setVisibility(View.VISIBLE);

            mFirebaseHelper.registerNewEmail(email, userName, password);

//            mAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "createUserWithEmail:success");
//                                FirebaseUser user = mAuth.getCurrentUser();
//                                //updateUI(user);
//
//                                finish();
//
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                Toast.makeText(mContext, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                                //updateUI(null);
//                            }
//                        }
//                    });

            mProgressBar.setVisibility(View.GONE);
            finish();
        }
    }

    private boolean isStringNull(String string) {
        return string.equals("");
    }
}
