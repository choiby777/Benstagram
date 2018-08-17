package com.cby.benstagram.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cby.benstagram.R;
import com.cby.benstagram.Util.BottomNavigationViewHelper;
import com.cby.benstagram.Util.FirebaseHelper;
import com.cby.benstagram.Util.GridImageAdapter;
import com.cby.benstagram.Util.UniversalImageLoader;
import com.cby.benstagram.models.User;
import com.cby.benstagram.models.UserAccountSettings;
import com.cby.benstagram.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment
        implements View.OnClickListener, FirebaseAuth.AuthStateListener{

    private static final String TAG = "ProfileFragment";
    private static final int ACTIVITY_NUM = 4;

    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUserName , mWebSite, mDescription;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationViewEx bottomNavigationView;

    private Context mContext;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;
    private FirebaseHelper mFirebaseHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile , container , false);
        Log.d(TAG, "onCreateView: Started");

        mContext = getActivity();

        mPosts = view.findViewById(R.id.txtPostValue);
        mFollowers = view.findViewById(R.id.txtfollowersValue);
        mFollowing = view.findViewById(R.id.txtFollowingValue);
        mDisplayName = view.findViewById(R.id.txt_display_name);
        mUserName = view.findViewById(R.id.txt_user_name);
        mWebSite = view.findViewById(R.id.txt_website);
        mDescription= view.findViewById(R.id.txt_description);

        mProgressBar = view.findViewById(R.id.profileProgressBar);
        mProfilePhoto = view.findViewById(R.id.profile_image);
        gridView = view.findViewById(R.id.gridImages);
        toolbar = view.findViewById(R.id.profileToolbar);
        profileMenu= view.findViewById(R.id.profileMoreMenu);
        bottomNavigationView= view.findViewById(R.id.bottomNavViewBar);

        setupToolbar();
        setupBottomNavigationView();
        setupFirebaseAuth();
        //setupGridImageTestDatas();

        TextView mTxtEditProfile = view.findViewById(R.id.txtEditProfile);
        mTxtEditProfile.setOnClickListener(this);

        return view;
    }

    private void setupToolbar(){

        ((ProfileActivity)getActivity()).setSupportActionBar(toolbar);

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Profile More Menu clicked");

                Intent intent = new Intent(mContext , AccountSettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext , bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    private void setupGridImageTestDatas(){

        ArrayList<String> imgURLs = new ArrayList<>();

        imgURLs.add("http://japanlinkstravel.co.uk/wp-content/uploads/2017/02/People-Places8.jpg");
        imgURLs.add("http://www.sebang.ca/files/attach/images/233/711/34c46fc64b99e02efb73c8e721ceba2c.jpg");
        imgURLs.add("http://post.phinf.naver.net/20151020_84/ulsanwbeauty_14453097928398eTGT_JPEG/mug_obj_144530979344446946.jpg");
        imgURLs.add("http://post.phinf.naver.net/MjAxODA0MTBfOTgg/MDAxNTIzMzI1MTE3MTkz.65867CEZ7AFMmIRMvXmkVJ2HmjPFLKmRcv0AW6hcamog.vB6_pfa27r2ca2LSMbhOSCvrm5QCbeLixtRy5T5MMDMg.JPEG/IRfPPO2KyzMYFdbJbMSWdvyvlUiE.jpg");
        imgURLs.add("http://imgnews.naver.net/image/082/2016/04/13/20160413000213_0_99_20160418172719.jpg");
        imgURLs.add("http://post.phinf.naver.net/MjAxODA1MTBfMTYg/MDAxNTI1OTM3MTcwMDQ0.uCCmZaJXM7GRte5fJnt-oY4kbz5s-1WZwR7eqUTE4iYg.YIz4rTMovU6xn-zRgM83WtdSz3HASBK5-QQ33tECD1Eg.JPEG/IN0JdD5y4Ph1TefGDc-Jsylhl7h4.jpg");
        imgURLs.add("http://post.phinf.naver.net/MjAxODAyMTNfMTY2/MDAxNTE4NTA4MjI4NTg4.-0CgGbb9OH18b2U46ZSP2Zoa2CwJ4JsEvk1G06pipzAg.4_jUaCI7Lwm0mcacaJqdOdunc0LakG6tn9Lwc5KERTQg.JPEG/I_jKFXxwFVyHydGqU0NjdSteKxpQ.jpg");
        imgURLs.add("http://japanlinkstravel.co.uk/wp-content/uploads/2017/02/People-Places8.jpg");
        imgURLs.add("http://www.sebang.ca/files/attach/images/233/711/34c46fc64b99e02efb73c8e721ceba2c.jpg");
        imgURLs.add("http://post.phinf.naver.net/20151020_84/ulsanwbeauty_14453097928398eTGT_JPEG/mug_obj_144530979344446946.jpg");
        imgURLs.add("http://post.phinf.naver.net/MjAxODA0MTBfOTgg/MDAxNTIzMzI1MTE3MTkz.65867CEZ7AFMmIRMvXmkVJ2HmjPFLKmRcv0AW6hcamog.vB6_pfa27r2ca2LSMbhOSCvrm5QCbeLixtRy5T5MMDMg.JPEG/IRfPPO2KyzMYFdbJbMSWdvyvlUiE.jpg");
        imgURLs.add("http://imgnews.naver.net/image/082/2016/04/13/20160413000213_0_99_20160418172719.jpg");
        imgURLs.add("http://post.phinf.naver.net/MjAxODA1MTBfMTYg/MDAxNTI1OTM3MTcwMDQ0.uCCmZaJXM7GRte5fJnt-oY4kbz5s-1WZwR7eqUTE4iYg.YIz4rTMovU6xn-zRgM83WtdSz3HASBK5-QQ33tECD1Eg.JPEG/IN0JdD5y4Ph1TefGDc-Jsylhl7h4.jpg");
        imgURLs.add("http://post.phinf.naver.net/MjAxODAyMTNfMTY2/MDAxNTE4NTA4MjI4NTg4.-0CgGbb9OH18b2U46ZSP2Zoa2CwJ4JsEvk1G06pipzAg.4_jUaCI7Lwm0mcacaJqdOdunc0LakG6tn9Lwc5KERTQg.JPEG/I_jKFXxwFVyHydGqU0NjdSteKxpQ.jpg");
        imgURLs.add("http://imgnews.naver.net/image/082/2016/04/13/20160413000213_0_99_20160418172719.jpg");
        imgURLs.add("http://post.phinf.naver.net/MjAxODA1MTBfMTYg/MDAxNTI1OTM3MTcwMDQ0.uCCmZaJXM7GRte5fJnt-oY4kbz5s-1WZwR7eqUTE4iYg.YIz4rTMovU6xn-zRgM83WtdSz3HASBK5-QQ33tECD1Eg.JPEG/IN0JdD5y4Ph1TefGDc-Jsylhl7h4.jpg");
        imgURLs.add("http://post.phinf.naver.net/MjAxODAyMTNfMTY2/MDAxNTE4NTA4MjI4NTg4.-0CgGbb9OH18b2U46ZSP2Zoa2CwJ4JsEvk1G06pipzAg.4_jUaCI7Lwm0mcacaJqdOdunc0LakG6tn9Lwc5KERTQg.JPEG/I_jKFXxwFVyHydGqU0NjdSteKxpQ.jpg");
        imgURLs.add("http://japanlinkstravel.co.uk/wp-content/uploads/2017/02/People-Places8.jpg");
        imgURLs.add("http://www.sebang.ca/files/attach/images/233/711/34c46fc64b99e02efb73c8e721ceba2c.jpg");
        imgURLs.add("http://post.phinf.naver.net/20151020_84/ulsanwbeauty_14453097928398eTGT_JPEG/mug_obj_144530979344446946.jpg");
        imgURLs.add("http://post.phinf.naver.net/MjAxODA0MTBfOTgg/MDAxNTIzMzI1MTE3MTkz.65867CEZ7AFMmIRMvXmkVJ2HmjPFLKmRcv0AW6hcamog.vB6_pfa27r2ca2LSMbhOSCvrm5QCbeLixtRy5T5MMDMg.JPEG/IRfPPO2KyzMYFdbJbMSWdvyvlUiE.jpg");
        imgURLs.add("http://imgnews.naver.net/image/082/2016/04/13/20160413000213_0_99_20160418172719.jpg");
        imgURLs.add("http://post.phinf.naver.net/MjAxODA1MTBfMTYg/MDAxNTI1OTM3MTcwMDQ0.uCCmZaJXM7GRte5fJnt-oY4kbz5s-1WZwR7eqUTE4iYg.YIz4rTMovU6xn-zRgM83WtdSz3HASBK5-QQ33tECD1Eg.JPEG/IN0JdD5y4Ph1TefGDc-Jsylhl7h4.jpg");
        imgURLs.add("http://post.phinf.naver.net/MjAxODAyMTNfMTY2/MDAxNTE4NTA4MjI4NTg4.-0CgGbb9OH18b2U46ZSP2Zoa2CwJ4JsEvk1G06pipzAg.4_jUaCI7Lwm0mcacaJqdOdunc0LakG6tn9Lwc5KERTQg.JPEG/I_jKFXxwFVyHydGqU0NjdSteKxpQ.jpg");

        setupGridImage(imgURLs);
    }

    private void setupGridImage(ArrayList<String> imgURLs) {

        GridView gridView = getActivity().findViewById(R.id.gridImages);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int gridColumnCount = 3;

        int imageWidth = gridWidth / gridColumnCount;

        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "" , imgURLs);
        gridView.setAdapter(adapter);
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: ");

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDbReference = mFirebaseDatabase.getReference();
        mFirebaseHelper = new FirebaseHelper(mContext);

        mDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // retrieve user information from the database
                UserSettings userSettings = mFirebaseHelper.getUserSettings(dataSnapshot);
                setProfileWidgets(userSettings);

                // retrieve images for the user in question

                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setProfileWidgets(UserSettings userSettings){

        User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSetting();

        UniversalImageLoader.setImage(settings.getProfile_photo() , mProfilePhoto , null , "");

        mDisplayName.setText(settings.getDisplay_name());
        mUserName.setText(settings.getUsername());
        mWebSite.setText(settings.getWebsite());
        mPosts.setText(String.valueOf(settings.getPosts()));
        mFollowers.setText(String.valueOf(settings.getFollowers()));
        mFollowing.setText(String.valueOf(settings.getFollowing()));
        mDescription.setText(settings.getDescription());
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.txtEditProfile){
            Intent intent = new Intent(mContext , AccountSettingActivity.class);
            intent.putExtra(getString(R.string.calling_activity) , getString(R.string.profile_activity));
            startActivity(intent);
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
