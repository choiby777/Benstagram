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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "RegisterActivity";

    private Context mContext = RegisterActivity.this;
    private EditText mTxtUserEmail , mTxtFullName, mTxtPassword;
    private ProgressBar mProgressBar;
    private Button mRegisterButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;
    private FirebaseHelper mFirebaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseHelper = new FirebaseHelper(mContext);

        setupFirebaseAuth();
        initWidgets();
        setupWidgetEvents();

        mProgressBar.setVisibility(View.GONE);

        Log.d(TAG, "onCreate: Register Activity Created");
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: ");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDbReference = mFirebaseDatabase.getReference();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null){
                    Log.d(TAG, "onAuthStateChanged: signed_in : " + firebaseUser.getUid());

                    mDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        String username = mTxtFullName.getText().toString();
                        String append;

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (mFirebaseHelper.checkIfUsernameExists(username , dataSnapshot)){
                                append = mDbReference.push().getKey().substring(3,10);
                                Log.d(TAG, "onDataChange: append : " + append);
                            }

                            username = username + append;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        };

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
