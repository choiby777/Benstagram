package com.cby.benstagram.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;

import com.cby.benstagram.R;
import com.cby.benstagram.Util.BottomNavigationViewHelper;
import com.cby.benstagram.Util.GridImageAdapter;
import com.cby.benstagram.Util.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private Context mContext  = ProfileActivity.this;
    private static final int ACTIVITY_NUM = 4;
    private ProgressBar mProgressBar;
    private ImageView mProfileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.d(TAG, "onCreate: starting");

//        setupBottomNavigationView();
//        setupToolbar();
//        setupActivityWidgets();
//        setProfileImage();
//        setupGridImageTestDatas();

        init();

        //mProgressBar.setVisibility(View.GONE);
    }

    private void init(){
        Log.d(TAG, "init: inflating " + R.layout.activity_profile);

        ProfileFragment fragment = new ProfileFragment();
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container , fragment);
        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();
    }

//    private void setupGridImageTestDatas(){
//
//        ArrayList<String> imgURLs = new ArrayList<>();
//
//        imgURLs.add("http://japanlinkstravel.co.uk/wp-content/uploads/2017/02/People-Places8.jpg");
//        imgURLs.add("http://www.sebang.ca/files/attach/images/233/711/34c46fc64b99e02efb73c8e721ceba2c.jpg");
//        imgURLs.add("http://post.phinf.naver.net/20151020_84/ulsanwbeauty_14453097928398eTGT_JPEG/mug_obj_144530979344446946.jpg");
//        imgURLs.add("http://post.phinf.naver.net/MjAxODA0MTBfOTgg/MDAxNTIzMzI1MTE3MTkz.65867CEZ7AFMmIRMvXmkVJ2HmjPFLKmRcv0AW6hcamog.vB6_pfa27r2ca2LSMbhOSCvrm5QCbeLixtRy5T5MMDMg.JPEG/IRfPPO2KyzMYFdbJbMSWdvyvlUiE.jpg");
//        imgURLs.add("http://imgnews.naver.net/image/082/2016/04/13/20160413000213_0_99_20160418172719.jpg");
//        imgURLs.add("http://post.phinf.naver.net/MjAxODA1MTBfMTYg/MDAxNTI1OTM3MTcwMDQ0.uCCmZaJXM7GRte5fJnt-oY4kbz5s-1WZwR7eqUTE4iYg.YIz4rTMovU6xn-zRgM83WtdSz3HASBK5-QQ33tECD1Eg.JPEG/IN0JdD5y4Ph1TefGDc-Jsylhl7h4.jpg");
//        imgURLs.add("http://post.phinf.naver.net/MjAxODAyMTNfMTY2/MDAxNTE4NTA4MjI4NTg4.-0CgGbb9OH18b2U46ZSP2Zoa2CwJ4JsEvk1G06pipzAg.4_jUaCI7Lwm0mcacaJqdOdunc0LakG6tn9Lwc5KERTQg.JPEG/I_jKFXxwFVyHydGqU0NjdSteKxpQ.jpg");
//        imgURLs.add("http://japanlinkstravel.co.uk/wp-content/uploads/2017/02/People-Places8.jpg");
//        imgURLs.add("http://www.sebang.ca/files/attach/images/233/711/34c46fc64b99e02efb73c8e721ceba2c.jpg");
//        imgURLs.add("http://post.phinf.naver.net/20151020_84/ulsanwbeauty_14453097928398eTGT_JPEG/mug_obj_144530979344446946.jpg");
//        imgURLs.add("http://post.phinf.naver.net/MjAxODA0MTBfOTgg/MDAxNTIzMzI1MTE3MTkz.65867CEZ7AFMmIRMvXmkVJ2HmjPFLKmRcv0AW6hcamog.vB6_pfa27r2ca2LSMbhOSCvrm5QCbeLixtRy5T5MMDMg.JPEG/IRfPPO2KyzMYFdbJbMSWdvyvlUiE.jpg");
//        imgURLs.add("http://imgnews.naver.net/image/082/2016/04/13/20160413000213_0_99_20160418172719.jpg");
//        imgURLs.add("http://post.phinf.naver.net/MjAxODA1MTBfMTYg/MDAxNTI1OTM3MTcwMDQ0.uCCmZaJXM7GRte5fJnt-oY4kbz5s-1WZwR7eqUTE4iYg.YIz4rTMovU6xn-zRgM83WtdSz3HASBK5-QQ33tECD1Eg.JPEG/IN0JdD5y4Ph1TefGDc-Jsylhl7h4.jpg");
//        imgURLs.add("http://post.phinf.naver.net/MjAxODAyMTNfMTY2/MDAxNTE4NTA4MjI4NTg4.-0CgGbb9OH18b2U46ZSP2Zoa2CwJ4JsEvk1G06pipzAg.4_jUaCI7Lwm0mcacaJqdOdunc0LakG6tn9Lwc5KERTQg.JPEG/I_jKFXxwFVyHydGqU0NjdSteKxpQ.jpg");
//        imgURLs.add("http://imgnews.naver.net/image/082/2016/04/13/20160413000213_0_99_20160418172719.jpg");
//        imgURLs.add("http://post.phinf.naver.net/MjAxODA1MTBfMTYg/MDAxNTI1OTM3MTcwMDQ0.uCCmZaJXM7GRte5fJnt-oY4kbz5s-1WZwR7eqUTE4iYg.YIz4rTMovU6xn-zRgM83WtdSz3HASBK5-QQ33tECD1Eg.JPEG/IN0JdD5y4Ph1TefGDc-Jsylhl7h4.jpg");
//        imgURLs.add("http://post.phinf.naver.net/MjAxODAyMTNfMTY2/MDAxNTE4NTA4MjI4NTg4.-0CgGbb9OH18b2U46ZSP2Zoa2CwJ4JsEvk1G06pipzAg.4_jUaCI7Lwm0mcacaJqdOdunc0LakG6tn9Lwc5KERTQg.JPEG/I_jKFXxwFVyHydGqU0NjdSteKxpQ.jpg");
//        imgURLs.add("http://japanlinkstravel.co.uk/wp-content/uploads/2017/02/People-Places8.jpg");
//        imgURLs.add("http://www.sebang.ca/files/attach/images/233/711/34c46fc64b99e02efb73c8e721ceba2c.jpg");
//        imgURLs.add("http://post.phinf.naver.net/20151020_84/ulsanwbeauty_14453097928398eTGT_JPEG/mug_obj_144530979344446946.jpg");
//        imgURLs.add("http://post.phinf.naver.net/MjAxODA0MTBfOTgg/MDAxNTIzMzI1MTE3MTkz.65867CEZ7AFMmIRMvXmkVJ2HmjPFLKmRcv0AW6hcamog.vB6_pfa27r2ca2LSMbhOSCvrm5QCbeLixtRy5T5MMDMg.JPEG/IRfPPO2KyzMYFdbJbMSWdvyvlUiE.jpg");
//        imgURLs.add("http://imgnews.naver.net/image/082/2016/04/13/20160413000213_0_99_20160418172719.jpg");
//        imgURLs.add("http://post.phinf.naver.net/MjAxODA1MTBfMTYg/MDAxNTI1OTM3MTcwMDQ0.uCCmZaJXM7GRte5fJnt-oY4kbz5s-1WZwR7eqUTE4iYg.YIz4rTMovU6xn-zRgM83WtdSz3HASBK5-QQ33tECD1Eg.JPEG/IN0JdD5y4Ph1TefGDc-Jsylhl7h4.jpg");
//        imgURLs.add("http://post.phinf.naver.net/MjAxODAyMTNfMTY2/MDAxNTE4NTA4MjI4NTg4.-0CgGbb9OH18b2U46ZSP2Zoa2CwJ4JsEvk1G06pipzAg.4_jUaCI7Lwm0mcacaJqdOdunc0LakG6tn9Lwc5KERTQg.JPEG/I_jKFXxwFVyHydGqU0NjdSteKxpQ.jpg");
//
//        setupGridImage(imgURLs);
//    }
//
//    private void setupGridImage(ArrayList<String> imgURLs) {
//
//        GridView gridView = findViewById(R.id.gridImages);
//
//        int gridWidth = getResources().getDisplayMetrics().widthPixels;
//        int gridColumnCount = 3;
//
//        int imageWidth = gridWidth / gridColumnCount;
//
//        gridView.setColumnWidth(imageWidth);
//
//        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "" , imgURLs);
//        gridView.setAdapter(adapter);
//    }
//
//    private void setupActivityWidgets() {
//        mProgressBar = findViewById(R.id.profileProgressBar);
//        mProfileImageView = findViewById(R.id.profile_image);
//    }
//
//
//    private void setupToolbar(){
//        Toolbar toolbar = findViewById(R.id.profileToolbar);
//        setSupportActionBar(toolbar);
//
//        ImageView imageView = findViewById(R.id.profileMoreMenu);
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: Profile More Menu clicked");
//
//                Intent intent = new Intent(mContext , AccountSettingActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void setProfileImage() {
//        Log.d(TAG, "setProfileImage: setting Profile Image");
//
//        String imgURL = "tr2.cbsistatic.com/hub/i/r/2017/01/31/7e355c52-c68f-4389-825f-392f2dd2ac19/resize/770x/d19d6c021f770122da649e2a77bd1404/androiddatahero.jpg";
//
//        UniversalImageLoader.setImage(imgURL , mProfileImageView , mProgressBar, "https://");
//    }
//
//    /**
//     * BottomNavigationView setup
//     */
//    private void setupBottomNavigationView(){
//        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
//        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
//
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        BottomNavigationViewHelper.enableNavigation(mContext , bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//    }

}
