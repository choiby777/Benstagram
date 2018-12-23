package com.cby.benstagram.Login;

import android.content.Context;
import android.content.Intent;
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

import com.cby.benstagram.Home.HomeActivity;
import com.cby.benstagram.R;
import com.cby.benstagram.Util.FirebaseHelper;
import com.cby.benstagram.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class RegisterActivity extends AppCompatActivity
        implements View.OnClickListener , FirebaseAuth.AuthStateListener{

    private static final String TAG = "RegisterActivity";

    private Context mContext = RegisterActivity.this;
    private EditText mEditTextEmail, mEditTextUserName, mEditTextPassword;
    private String email, userName, password;
    private ProgressBar mProgressBar;
    private Button mRegisterButton;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;
    private FirebaseHelper mFirebaseHelper;

    private String append = "";

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
        mAuth.addAuthStateListener(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDbReference = mFirebaseDatabase.getReference();
    }

    private void setupWidgetEvents() {
        mRegisterButton.setOnClickListener(this);
    }

    private void initWidgets() {
        mEditTextEmail = findViewById(R.id.input_email);
        mEditTextUserName = findViewById(R.id.input_fullName);
        mEditTextPassword = findViewById(R.id.input_password);
        mProgressBar = findViewById(R.id.progress_register);
        mRegisterButton = findViewById(R.id.btn_register);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.btn_register) {

            email = mEditTextEmail.getText().toString();
            userName = mEditTextUserName.getText().toString();
            password = mEditTextPassword.getText().toString();

            if (isStringNull(email) || isStringNull(password) || isStringNull(userName)){
                Toast.makeText(mContext, "All fields must be filled out", Toast.LENGTH_SHORT).show();
                return;
            }

            mProgressBar.setVisibility(View.VISIBLE);

            mFirebaseHelper.registerNewEmail(email, userName, password, mProgressBar);
        }
    }

    private boolean isStringNull(String string) {
        return string.equals("");
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){
            Log.d(TAG, "onAuthStateChanged: signed_in : " + firebaseUser.getUid());

            mDbReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    checkIfUsernameExists(userName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Log.e(TAG, "onCancelled: databaseError : " + databaseError);

                    Toast.makeText(mContext, "Database Error",
                            Toast.LENGTH_LONG).show();
                }
            });

            mProgressBar.setVisibility(View.GONE);

            finish();

        }else{
            Log.d(TAG, "onAuthStateChanged: signed_out");
        }
    }


    private void checkIfUsernameExists(final String userName) {
        Log.d(TAG, "checkIfUsernameExists: userName : " + userName);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(userName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Log.d(TAG, "onDataChange: found a match : " + singleSnapshot.getValue(User.class).getUsername());
                        append = mDbReference.push().getKey().substring(3,10);
                    }
                }

                final String mUserName = userName + append;

                // 현재의 Token값을 가져온다.
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(RegisterActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String newToken = instanceIdResult.getToken();
                        Log.d(TAG, "onSuccess: newToken : " + newToken);

                        mFirebaseHelper.addNewUser(email , mUserName, "Test User", "https://www.naver.com/", "none", newToken);

                        mAuth.signOut();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
