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
import com.cby.benstagram.Util.SquareImageView;
import com.cby.benstagram.Util.UniversalImageLoader;
import com.cby.benstagram.models.Photo;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ViewPostFragment extends Fragment{
    private static final String TAG = "ViewPostFragment";

    private int mActivityNumber = 0;
    private Photo mPhoto;

    // Widgets
    private SquareImageView mPostImage;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private ImageView mBackArrow , mEllipses, mHeartWhite, mHeartRed, mMessage, mSend;
    private TextView txtLikedInfo , txtTags, txtCommentInfo, txtDaysInfo;

    public ViewPostFragment() {
        super();
        setArguments(new Bundle());
    }

    private void setupWidgets(View view) {
        mPostImage = view.findViewById(R.id.imageViewPhoto);
        bottomNavigationViewEx = view.findViewById(R.id.bottomNavViewBar);
        mBackArrow = view.findViewById(R.id.backArrow);
        txtLikedInfo = view.findViewById(R.id.txtLikedInfo);
        txtTags = view.findViewById(R.id.txtTags);
        txtCommentInfo = view.findViewById(R.id.txtCommentInfo);
        txtDaysInfo = view.findViewById(R.id.txtDaysInfo);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ViewPostFragment");

        View view = inflater.inflate(R.layout.fragment_view_post, container, false);

        setupWidgets(view);

        try{
            mActivityNumber = getActivityNumberFromBundle();
            mPhoto = getPhotoFromBundle();

            UniversalImageLoader.setImage(mPhoto.getImage_path(), mPostImage, null, "");

        }catch (NullPointerException e){
            Log.e(TAG, "onCreateView: " + e.getMessage() );
        }

        setupWidgetValues();
        setupBottomNavigationView();

        return view;
    }

    private void setupWidgetValues() {
        String value = getTimestampDifference();
        txtDaysInfo.setText(value + " DAYS AGO");
    }

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(getActivity() , getActivity(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(mActivityNumber);
        menuItem.setChecked(true);
    }

    public int getActivityNumberFromBundle() {
        Bundle args = getArguments();

        if (args != null){
            return args.getInt(getString(R.string.activity_number));
        }else{
            return 0;
        }
    }

    public Photo getPhotoFromBundle() {

        Bundle args = getArguments();

        if (args != null){
            return args.getParcelable(getString(R.string.photo));
        }else{
            return null;
        }
    }

    private String getTimestampDifference(){
        Log.d(TAG, "getTimestampDifference: start");

        String differenceString = "";

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss'Z'" , Locale.ROOT);
        sdf.setTimeZone(TimeZone.getDefault());
        Date today = calendar.getTime();
        sdf.format(today);
        Date timestamp;

        final String photoTimestamp = mPhoto.getDate_created();

        try {
            timestamp = sdf.parse(photoTimestamp);
            long differenceValue = today.getTime() - timestamp.getTime();
            differenceValue = differenceValue / 1000 / 60 / 60 /24;
            differenceString = String.valueOf(Math.round(differenceValue));
        } catch (ParseException e) {
            e.printStackTrace();

            differenceString = "0";
        }

        return differenceString;
    }
}
