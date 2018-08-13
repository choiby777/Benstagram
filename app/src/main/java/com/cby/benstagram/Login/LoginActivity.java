package com.cby.benstagram.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cby.benstagram.Home.HomeActivity;
import com.cby.benstagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    private Context mContext  = LoginActivity.this;
    private AppCompatButton mBtnLogin;
    private TextView mTxtSignUp;
    private EditText mInputEmail, mInputPass;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        setupActivityWidgets();
        setupWidgetEvents();

        mProgressBar.setVisibility(View.GONE);

        if (mAuth.getCurrentUser() != null){
            Intent intent = new Intent(mContext , HomeActivity.class);
            startActivity(intent);
            finish();
        }

        Log.d(TAG, "onCreate: Login Activity Created");
    }

    private void setupActivityWidgets() {
        mProgressBar = findViewById(R.id.progress_Login);
        mBtnLogin = findViewById(R.id.btn_login);
        mTxtSignUp = findViewById(R.id.txt_sign_up);
        mInputEmail = findViewById(R.id.input_email);
        mInputPass = findViewById(R.id.input_password);
    }

    private void setupWidgetEvents() {
        if (mBtnLogin != null) mBtnLogin.setOnClickListener(this);
        if (mTxtSignUp != null) mTxtSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.btn_login){

            String email = mInputEmail.getText().toString();
            String password = mInputPass.getText().toString();

            if (isStringNull(email) || isStringNull(password)){
                Toast.makeText(mContext, "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mProgressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            mProgressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(mContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }else if (viewId == R.id.txt_sign_up){
            Intent intent = new Intent(mContext , RegisterActivity.class);
            startActivity(intent);
        }
    }

    private boolean isStringNull(String string) {
        return string.equals("");         
    }
}
