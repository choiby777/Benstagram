package com.cby.benstagram.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cby.benstagram.R;
import com.cby.benstagram.Util.ViewPostFragment;
import com.cby.benstagram.models.Photo;

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.OnGridImageSelectedListener{

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

        init();
    }

    private void init(){
        Log.d(TAG, "init: inflating " + R.layout.activity_profile);

        ProfileFragment fragment = new ProfileFragment();
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container , fragment);
        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();
    }

    @Override
    public void onGridImageSelected(Photo photo, int activityNumber) {
        Log.d(TAG, "onGridImageSelected: selected photo : " + photo.toString());

        ViewPostFragment viewPostFragment = new ViewPostFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo) , photo);
        args.putInt(getString(R.string.activity_number) , activityNumber);
        viewPostFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container , viewPostFragment);
        transaction.addToBackStack(getString(R.string.view_post_fragment));
        transaction.commit();

    }

}
