package com.cby.benstagram.Util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cby.benstagram.Profile.ProfileActivity;
import com.cby.benstagram.R;
import com.cby.benstagram.models.Like;
import com.cby.benstagram.models.Photo;
import com.cby.benstagram.models.User;
import com.cby.benstagram.models.UserAccountSettings;
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

public class ViewPostFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ViewPostFragment";

    public interface OnCommentThreadSelectedListener{
        void OnCommentThreadSelectedListener(Photo photo);
    }
    OnCommentThreadSelectedListener mOnCommentThreadSelectedListener;

    private int mActivityNumber = 0;
    private Photo mPhoto;
    private UserAccountSettings mUserAccountSettings;

    // Widgets
    private SquareImageView mPostImage;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private ImageView imgUser, mBackArrow, mEllipses, imgHeartWhite, imgHeartRed, mMessage, mSend;
    private TextView txtUserName, txtLikedInfo, txtTags, txtCommentInfo, txtDaysInfo;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;
    private FirebaseHelper mFirebaseHelper;

    // Vars
    private GestureDetector mGestureDetector;
    private HeartToggle mHeartToggle;
    private Boolean isLikedbyCurrentUser;

    public ViewPostFragment() {
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ViewPostFragment");

        View view = inflater.inflate(R.layout.fragment_view_post, container, false);

        setupFirebaseAuth();

        mGestureDetector = new GestureDetector(getActivity(), new GestureListener());

        setupWidgets(view);

        mHeartToggle = new HeartToggle(imgHeartWhite , imgHeartRed);

        try {
            mActivityNumber = getActivityNumberFromBundle();
            mPhoto = getPhotoFromBundle();

            isLikedbyCurrentUser = mPhoto.isExistUserInLikes(mAuth.getUid());

            if (isLikedbyCurrentUser) mHeartToggle.setToLike();
            else mHeartToggle.setToUnLike();

            UniversalImageLoader.setImage(mPhoto.getImage_path(), mPostImage, null, "");

        } catch (NullPointerException e) {
            Log.e(TAG, "onCreateView: " + e.getMessage());
        }

        getPhotoDetails();
        setupWidgetValues();
        setupBottomNavigationView();

        testToggle();

        txtCommentInfo.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnCommentThreadSelectedListener = (OnCommentThreadSelectedListener)getActivity();
        }catch (Exception ex){

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtCommentInfo){
            mOnCommentThreadSelectedListener.OnCommentThreadSelectedListener(mPhoto);
        }
    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            mHeartToggle.toggle();

            // 현재 사진의 Likes 리스트(like한 user_id 리스트) 정보를 쿼리한다.
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference
                    .child(getString(R.string.dbname_photos))
                    .child(mPhoto.getPhoto_id())
                    .child(getString(R.string.field_likes));

            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String currentUserKey = "";

                    // 등록된 like 정보가 있다면 현재 계정의 아이디가 있는지 확인 후 있다면 UnLike 없다면 Like
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                        String uderId = singleSnapshot.child(getString(R.string.field_user_id)).getValue(String.class);

                        if (mAuth.getCurrentUser().getUid().equals(uderId)){
                            currentUserKey = singleSnapshot.getKey();
                            break;
                        }
                    }

                    if (currentUserKey.isEmpty()) addNewLike();
                    else removeLike(currentUserKey);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



            return true;
        }
    }

    private void removeLike(String key) {
        Log.d(TAG, "removeLike: remove key : " + key);

        mDbReference.child(getString(R.string.dbname_photos))
                .child(mPhoto.getPhoto_id())
                .child(getString(R.string.field_likes))
                .child(key)
                .removeValue();

        mDbReference.child(getString(R.string.dbname_user_photos))
                .child(mAuth.getUid())
                .child(mPhoto.getPhoto_id())
                .child(getString(R.string.field_likes))
                .child(key)
                .removeValue();
    }

    private void addNewLike() {
        Log.d(TAG, "addNewLike: start");

        String newLikeKey = mDbReference.push().getKey();

        Log.d(TAG, "addNewLike: new key : " + newLikeKey);

        Like like = new Like();
        like.setUser_id(mAuth.getUid());

        mDbReference.child(getString(R.string.dbname_photos))
                .child(mPhoto.getPhoto_id())
                .child(getString(R.string.field_likes))
                .child(newLikeKey)
                .setValue(like);

        mDbReference.child(getString(R.string.dbname_user_photos))
                .child(mAuth.getUid())
                .child(mPhoto.getPhoto_id())
                .child(getString(R.string.field_likes))
                .child(newLikeKey)
                .setValue(like);

    }

    private String getLikesString(){
        String likesString = "";

        // 현재 사진의 Likes 리스트(like한 user_id 리스트) 정보를 쿼리한다.
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_photos))
                .orderByChild(mPhoto.getPhoto_id())
                .equalTo(getString(R.string.field_likes));

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final StringBuilder userNames = new StringBuilder();

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    String likeUserId = singleSnapshot.getValue(Like.class).getUser_id();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference
                            .child(getString(R.string.dbname_users))
                            .orderByChild(getString(R.string.field_user_id))
                            .equalTo(likeUserId);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot singleDataSnapshot : dataSnapshot.getChildren()){
                                User likeUser = singleDataSnapshot.getValue(User.class);

                                userNames.append(likeUser.getUsername());
                                userNames.append(",");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return likesString;
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
        imgHeartWhite = view.findViewById(R.id.imgHeartWhite);
        imgHeartRed = view.findViewById(R.id.imgHeartRed);
    }

    private void testToggle(){
        imgHeartRed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch: red heart touch");
                return mGestureDetector.onTouchEvent(event);
            }
        });

        imgHeartWhite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch: white heart touch");
                return mGestureDetector.onTouchEvent(event);
            }
        });
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

    private void setupWidgetValues() {
        String value = getTimestampDifference();
        if (!value.equals("0")) {
            txtDaysInfo.setText(value + " DAYS AGO");
        } else {
            txtDaysInfo.setText("TODAY");
        }

        txtTags.setText(mPhoto.getTags());
        txtCommentInfo.setText("View all 3 comments");
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
