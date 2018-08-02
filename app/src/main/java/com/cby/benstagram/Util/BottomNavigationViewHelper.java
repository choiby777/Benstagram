package com.cby.benstagram.Util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.cby.benstagram.Home.HomeActivity;
import com.cby.benstagram.Likes.LikesActivity;
import com.cby.benstagram.Profile.ProfileActivity;
import com.cby.benstagram.R;
import com.cby.benstagram.Search.SearchActivity;
import com.cby.benstagram.Share.ShareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationViewEx");

        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent = null;

                switch (item.getItemId()){
                    case R.id.ic_house:
                        intent = new Intent(context , HomeActivity.class);
                        break;

                    case R.id.ic_search:
                        intent = new Intent(context , SearchActivity.class);
                        break;

                    case R.id.ic_circle:
                        intent = new Intent(context , ShareActivity.class);
                        break;

                    case R.id.ic_alert:
                        intent = new Intent(context , LikesActivity.class);
                        break;

                    case R.id.ic_android:
                        intent = new Intent(context , ProfileActivity.class);
                        break;
                }

                if (intent != null){
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(intent);
                }

                return false;
            }
        });
    }
}
