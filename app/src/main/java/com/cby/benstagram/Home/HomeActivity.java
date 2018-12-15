package com.cby.benstagram.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cby.benstagram.Login.LoginActivity;
import com.cby.benstagram.Login.StartUpActivity;
import com.cby.benstagram.Profile.AccountSettingActivity;
import com.cby.benstagram.R;
import com.cby.benstagram.Util.BottomNavigationViewHelper;
import com.cby.benstagram.Util.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Context mContext  = HomeActivity.this;
    private static final int ACTIVITY_NUM = 0;
    private static final int STARTUP_REQUEST_CODE = 10;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: starting");

        setupFirebaseAuth();
        initImageLoader();
        setupBottomNavigationView();
        setupViewPager();
        runStartUp();
    }

    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking current user");

        if (user == null){
            Intent intent = new Intent(mContext , LoginActivity.class);
            startActivity(intent);
        }else{
            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()){
                        Log.d(TAG, "onComplete: task isSuccessful");
                    }else {
                        Log.d(TAG, "onComplete: task fail");

                        Intent intent = new Intent(mContext , LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void setupFirebaseAuth() {

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();

//        Intent intent = getIntent();
//
//        if (intent.hasExtra(getString(R.string.calling_activity)) &&
//            intent.getStringExtra(getString(R.string.calling_activity)).equals(getString(R.string.startup_activity))){
//
//            mAuth.addAuthStateListener(mAuthListener);
//            checkCurrentUser(mAuth.getCurrentUser());
//
//        }else{
//            runStartUp();
//        }
    }

    private void runStartUp() {

        FirebaseUser user = mAuth.getCurrentUser();

        // login 사용자 정보가 없으면 Login Activity 실행
         if (user == null){
            Intent intent = new Intent(mContext , LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
             //mAuth.signOut();
        }

        //Intent intent = new Intent(mContext , StartUpActivity.class);
        //startActivityForResult(intent , STARTUP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == STARTUP_REQUEST_CODE){
                boolean isUserValid = data.getBooleanExtra(getString(R.string.is_user_valid), false);

                if (!isUserValid){
                    Intent intent = new Intent(mContext , LoginActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * ViewPager에 3개의 Tab 추가(Camera, Home , Message)
     */
    private void  setupViewPager(){
       SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new CameraFragment());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new MessageFragment());

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_instagram);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrow);
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext , this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

}
