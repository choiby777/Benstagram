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

public class ViewPostFragment extends Fragment{
    private static final String TAG = "ViewPostFragment";

    private int mActivityNumber = 0;
    private Photo mPhoto;

    // Widgets
    private SquareImageView mPostImage;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private ImageView mBackArrow , mEllipses, mHeartWhite, mHeartRed, mMessage, mSend;
    private TextView txtText1 , txtText2, txtText3, txtText4;

    public ViewPostFragment() {
        super();
        setArguments(new Bundle());
    }

    private void setupWidgets(View view) {

        mPostImage = view.findViewById(R.id.imageViewPhoto);
        bottomNavigationViewEx = view.findViewById(R.id.bottomNavViewBar);
        mBackArrow = view.findViewById(R.id.backArrow);
        txtText1 = view.findViewById(R.id.txtText1);
        txtText2 = view.findViewById(R.id.txtText2);
        txtText3 = view.findViewById(R.id.txtText3);
        txtText4 = view.findViewById(R.id.txtText4);
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

        setupBottomNavigationView();

        return view;
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
}
