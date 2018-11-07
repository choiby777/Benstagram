package com.cby.benstagram.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cby.benstagram.R;
import com.cby.benstagram.Util.ViewCommentsFragment;
import com.cby.benstagram.Util.ViewPostFragment;
import com.cby.benstagram.Util.ViewProfileFragment;
import com.cby.benstagram.models.Photo;
import com.cby.benstagram.models.User;

public class ProfileActivity extends AppCompatActivity implements
        ProfileFragment.OnGridImageSelectedListener,
        ViewPostFragment.OnCommentThreadSelectedListener{

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

        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.calling_activity))){
            if (intent.hasExtra(getString(R.string.selected_user))){
                User user = intent.getParcelableExtra(getString(R.string.selected_user));
                setViewProfileFragment(user);
            }else{
                Toast.makeText(mContext , "user info is not exit", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            setProfileFragment();
        }
    }

    private void setViewProfileFragment(User user) {
        Log.d(TAG, "setViewProfileFragment: user : " + user.toString());
        
        ViewProfileFragment fragment = new ViewProfileFragment();

        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.user) , user);
        fragment.setArguments(args);

        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container , fragment);
        transaction.addToBackStack(getString(R.string.view_profile_fragment));
        transaction.commit();
    }

    private void setProfileFragment() {
        Log.d(TAG, "setProfileFragment: start");
        
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

    @Override
    public void OnCommentThreadSelectedListener(Photo photo) {
        ViewCommentsFragment viewCommentsFragment = new ViewCommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo) , photo);
        viewCommentsFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container , viewCommentsFragment);
        transaction.addToBackStack(getString(R.string.view_post_fragment));
        transaction.commit();
    }
}
