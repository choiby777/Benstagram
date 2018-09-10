package com.cby.benstagram.Share;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cby.benstagram.R;
import com.cby.benstagram.Util.FirebaseHelper;
import com.cby.benstagram.Util.UniversalImageLoader;
import com.cby.benstagram.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.net.URISyntaxException;

public class NextActivity extends AppCompatActivity
        implements View.OnClickListener, FirebaseAuth.AuthStateListener{

    private static final String TAG = "NextActivity";

    //widgets
    private ImageView imgBack;
    private ImageView imgShare;
    private TextView txtShare;
    private EditText txtDescription;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;
    private FirebaseHelper mFirebaseHelper;

    //vars
    private String mAppend = "file:/";
    private int imageCount = 0;
    private String imageURL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Log.d(TAG, "onCreate: selected Image : " + getIntent().getStringExtra(getString(R.string.selected_image)));

        setupWidgets();

        setupFirebaseAuth();

        setImage();
    }

    private void setImage() {
        Intent intent = getIntent();

        imageURL = getIntent().getStringExtra(getString(R.string.selected_image));
        UniversalImageLoader.setImage(imageURL , imgShare , null , mAppend);
    }

    private void setupWidgets() {
        imgBack = findViewById(R.id.imgBack);
        imgShare = findViewById(R.id.imgShare);
        txtShare = findViewById(R.id.txtShare);
        txtDescription = findViewById(R.id.txtDescription);

        imgBack.setOnClickListener(this);
        txtShare.setOnClickListener(this);
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: ");

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDbReference = mFirebaseDatabase.getReference();
        mFirebaseHelper = new FirebaseHelper(NextActivity.this);

        mDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageCount = mFirebaseHelper.getImageCount(dataSnapshot);

                Log.d(TAG, "onDataChange: user image count : " + imageCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgBack){
            Log.d(TAG, "onClick: imgBack clicked");

            finish();

        }else if (v.getId() == R.id.txtShare){
            Log.d(TAG, "onClick: share clicked");

            // Upload image to firebase
            Toast.makeText(this, "Attempting to upload new photo", Toast.LENGTH_SHORT).show();

            String description = txtDescription.getText().toString();
            mFirebaseHelper.uploadPhoto(getString(R.string.new_photo) , description, imageCount , imageURL);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null){
            Log.d(TAG, "onAuthStateChanged: signed_in : " + user.getUid());
        }else{
            Log.d(TAG, "onAuthStateChanged: signed_out");
        }
    }
}
