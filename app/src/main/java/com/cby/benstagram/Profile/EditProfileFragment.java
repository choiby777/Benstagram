package com.cby.benstagram.Profile;

import android.os.Bundle;
import android.print.PrinterId;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cby.benstagram.R;
import com.cby.benstagram.Util.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";
    private ImageView mProfileImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile , container , false);

        mProfileImage = view.findViewById(R.id.profile_image);

        initImageLoader();

        setProfileImage();

        return view;
    }

    private void setProfileImage() {
        Log.d(TAG, "setProfileImage: setting Profile Image");

        String imgURL = "tr2.cbsistatic.com/hub/i/r/2017/01/31/7e355c52-c68f-4389-825f-392f2dd2ac19/resize/770x/d19d6c021f770122da649e2a77bd1404/androiddatahero.jpg";

        UniversalImageLoader.setImage(imgURL , mProfileImage , null, "https://");
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
}
