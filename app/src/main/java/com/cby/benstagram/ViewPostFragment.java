package com.cby.benstagram;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cby.benstagram.Util.BottomNavigationViewHelper;
import com.cby.benstagram.Util.FirebaseHelper;
import com.cby.benstagram.Util.SquareImageView;
import com.cby.benstagram.Util.UniversalImageLoader;
import com.cby.benstagram.models.Photo;
import com.cby.benstagram.models.UserAccountSettings;
import com.cby.benstagram.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ViewPostFragment extends Fragment {
    private static final String TAG = "ViewPostFragment";

    private int mActivityNumber = 0;
    private Photo mPhoto;
    private UserAccountSettings mUserAccountSettings;

    // Widgets
    private SquareImageView mPostImage;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private ImageView imgUser, mBackArrow, mEllipses, mHeartWhite, mHeartRed, mMessage, mSend;
    private TextView txtUserName, txtLikedInfo, txtTags, txtCommentInfo, txtDaysInfo;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;
    private FirebaseHelper mFirebaseHelper;

    public ViewPostFragment() {
        super();
        setArguments(new Bundle());
    }

    private void setupWidgets(View view) {
        imgUser = view.findViewById(R.id.imgUser);
        txtUserName = view.findViewById(R.id.txtUserName);
        mPostImage = view.findViewById(R.id.imageViewPhoto);
        bottomNavigationViewEx = view.findViewById(R.id.bottomNavViewBar);
        mBackArrow = view.findViewById(R.id.backArrow);
        txtLikedInfo = view.findViewById(R.id.txtLikedInfo);
        txtTags = view.findViewById(R.id.txtTags);
        txtCommentInfo = view.findViewById(R.id.txtCommentInfo);
        txtDaysInfo = view.findViewById(R.id.txtDaysInfo);
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: start ");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDbReference = mFirebaseDatabase.getReference();
        mFirebaseHelper = new FirebaseHelper(getActivity());
    }

    private void getPhotoDetails(){
        Log.d(TAG, "getPhotoDetails: retrieving photo details.");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_user_account_settings))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(mPhoto.getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    mUserAccountSettings = singleSnapshot.getValue(UserAccountSettings.class);
                }

                setupProfileWidgets();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    private void setupProfileWidgets() {
        txtUserName.setText(mUserAccountSettings.getDisplay_name());
        UniversalImageLoader.setImage(mUserAccountSettings.getProfile_photo(), imgUser, null, "");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ViewPostFragment");

        View view = inflater.inflate(R.layout.fragment_view_post, container, false);

        setupWidgets(view);

        try {
            mActivityNumber = getActivityNumberFromBundle();
            mPhoto = getPhotoFromBundle();

            UniversalImageLoader.setImage(mPhoto.getImage_path(), mPostImage, null, "");

        } catch (NullPointerException e) {
            Log.e(TAG, "onCreateView: " + e.getMessage());
        }

        setupFirebaseAuth();
        getPhotoDetails();
        setupWidgetValues();
        setupBottomNavigationView();

        return view;
    }

    private void setupWidgetValues() {
        String value = getTimestampDifference();
        if (!value.equals("0")) {
            txtDaysInfo.setText(value + " DAYS AGO");
        } else {
            txtDaysInfo.setText("TODAY");
        }

        txtTags.setText(mPhoto.getTags());
        txtCommentInfo.setText(mPhoto.getCaption());
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(getActivity(), getActivity(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(mActivityNumber);
        menuItem.setChecked(true);
    }

    public int getActivityNumberFromBundle() {
        Bundle args = getArguments();

        if (args != null) {
            return args.getInt(getString(R.string.activity_number));
        } else {
            return 0;
        }
    }

    public Photo getPhotoFromBundle() {

        Bundle args = getArguments();

        if (args != null) {
            return args.getParcelable(getString(R.string.photo));
        } else {
            return null;
        }
    }

    private String getTimestampDifference() {
        Log.d(TAG, "getTimestampDifference: start");

        String differenceString = "";

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss'Z'", Locale.ROOT);
        sdf.setTimeZone(TimeZone.getDefault());
        Date today = calendar.getTime();
        sdf.format(today);
        Date timestamp;

        final String photoTimestamp = mPhoto.getDate_created();

        try {
            timestamp = sdf.parse(photoTimestamp);
            long differenceValue = today.getTime() - timestamp.getTime();
            differenceValue = differenceValue / 1000 / 60 / 60 / 24;
            differenceString = String.valueOf(Math.round(differenceValue));
        } catch (ParseException e) {
            e.printStackTrace();

            differenceString = "0";
        }

        return differenceString;
    }
}
