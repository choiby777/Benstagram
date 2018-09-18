package com.cby.benstagram.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cby.benstagram.R;
import com.cby.benstagram.Util.BottomNavigationViewHelper;
import com.cby.benstagram.Util.FirebaseHelper;
import com.cby.benstagram.Util.SectionStatePagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class AccountSettingActivity extends AppCompatActivity {

    private static final String TAG = "AccountSettingActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context mContext;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;
    public SectionStatePagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        mContext  = AccountSettingActivity.this;
        Log.d(TAG, "onCreate: starting");

        mViewPager = findViewById(R.id.container);
        mRelativeLayout = findViewById(R.id.relLayout1);

        setupBottomNavigationView();
        setupSettingsList();
        setupFragments();
        getIncomingIntent();

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigate back to 'ProfileActivity'");

                finish();
            }
        });
    }

    private void getIncomingIntent(){
        Intent intent  = getIntent();

        String stringExtra = intent.getStringExtra(getString(R.string.return_to_fragment));

        // 사진 선택 또는 촬영후 실행되는 경우
        if (intent.hasExtra(getString(R.string.selected_image)) ||
            intent.hasExtra(getString(R.string.selected_bitmap))) {
            setViewPager(mPagerAdapter.getFragmentNumber(getString(R.string.edit_profile_fragment)));
        }

        if (intent.hasExtra(getString(R.string.calling_activity))){
            Log.d(TAG, "getIncomingIntent: received incoming intent from " + getString(R.string.calling_activity));
            setViewPager(mPagerAdapter.getFragmentNumber(getString(R.string.edit_profile_fragment)));
        }
    }

    private void setupFragments() {
        mPagerAdapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(new EditProfileFragment() , getString(R.string.edit_profile_fragment)); // fragment 0
        mPagerAdapter.addFragment(new SignOutFragment() , getString(R.string.sign_out_fragment)); // fragment 1
    }

    private void  setupSettingsList(){
        Log.d(TAG, "setupSettingsList: init 'Account Setting' list ");

        ListView listView = findViewById(R.id.lvAccountSettings);

        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_profile_fragment)); // fragment 0
        options.add(getString(R.string.sign_out_fragment)); // fragment 1

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: navigating to fragement : " + position);

                setViewPager(position);
            }
        });
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext , bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    public void setViewPager(int fragmentNumber) {

        mRelativeLayout.setVisibility(View.GONE);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(fragmentNumber);
    }
}
