package com.cby.benstagram.Profile;

import android.media.Image;
import android.os.Bundle;
import android.print.PrinterId;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cby.benstagram.R;
import com.cby.benstagram.Util.FirebaseHelper;
import com.cby.benstagram.Util.StringManipulation;
import com.cby.benstagram.Util.UniversalImageLoader;
import com.cby.benstagram.models.User;
import com.cby.benstagram.models.UserAccountSettings;
import com.cby.benstagram.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EditProfileFragment extends Fragment
        implements FirebaseAuth.AuthStateListener, View.OnClickListener {
    private static final String TAG = "EditProfileFragment";

    private ImageView mProfileImage;
    private EditText mEditUserName , mEditDisplayName, mEditWebsite,
            mEditDescription, mEditEmail, mEditPhoneNumber;
    private String mUserId;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;
    private FirebaseHelper mFirebaseHelper;
    private UserSettings mUserSettings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile , container , false);

        mProfileImage = view.findViewById(R.id.profile_image);

        mEditUserName = view.findViewById(R.id.editText_userName);
        mEditDisplayName = view.findViewById(R.id.editText_displayName);
        mEditWebsite = view.findViewById(R.id.editText_website);
        mEditDescription = view.findViewById(R.id.editText_description);
        mEditEmail = view.findViewById(R.id.editText_email);
        mEditPhoneNumber = view.findViewById(R.id.editText_phoneNumber);

        ImageView imageChecked = view.findViewById(R.id.image_checked);
        imageChecked.setOnClickListener(this);

        ImageView backArrowImg = view.findViewById(R.id.backArrow);
        backArrowImg.setOnClickListener(this);

        setProfileImage();

        setupFirebaseAuth();

        return view;
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: ");

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        mUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDbReference = mFirebaseDatabase.getReference();
        mFirebaseHelper = new FirebaseHelper(getActivity());

        mDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // retrieve user information from the database
                UserSettings userSettings = mFirebaseHelper.getUserSettings(dataSnapshot);
                setProfileWidgets(userSettings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveProfileSettings(){
        final String displayName = mEditDisplayName.getText().toString();
        final String userName = mEditUserName.getText().toString();
        final String website = mEditWebsite.getText().toString();
        final String description = mEditDescription.getText().toString();
        final String email = mEditEmail.getText().toString();
        final String phoneNumber = mEditPhoneNumber.getText().toString();

        mDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!mUserSettings.getUser().getUsername().equals(userName)){
                    checkIfUsernameExists(userName);
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

                if (!dataSnapshot.exists()){
                    mFirebaseHelper.updateUsername(userName);
                    Toast.makeText(getActivity(), "saved username.", Toast.LENGTH_SHORT).show();
                }

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Log.d(TAG, "onDataChange: found a match : " + singleSnapshot.getValue(User.class).getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setProfileWidgets(UserSettings userSettings){

        mUserSettings = userSettings;
        User user = userSettings.getUser();
        UserAccountSettings accountSettings = userSettings.getSetting();

        UniversalImageLoader.setImage(accountSettings.getProfile_photo() , mProfileImage , null , "");

        mEditUserName.setText(user.getUsername());
        mEditEmail.setText(user.getEmail());
        mEditPhoneNumber.setText(user.getPhone_number());

        mEditDisplayName.setText(accountSettings.getDisplay_name());
        mEditWebsite.setText(accountSettings.getWebsite());
        mEditDescription.setText(accountSettings.getDescription());
    }


    private void setProfileImage() {
        Log.d(TAG, "setProfileImage: setting Profile Image");

        String imgURL = "tr2.cbsistatic.com/hub/i/r/2017/01/31/7e355c52-c68f-4389-825f-392f2dd2ac19/resize/770x/d19d6c021f770122da649e2a77bd1404/androiddatahero.jpg";

        UniversalImageLoader.setImage(imgURL , mProfileImage , null, "https://");
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.image_checked){

            saveProfileSettings();

        }else if (viewId == R.id.backArrow){
            getActivity().finish();
        }
    }
}
