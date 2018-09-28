package com.cby.benstagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cby.benstagram.Util.SquareImageView;
import com.cby.benstagram.Util.UniversalImageLoader;
import com.cby.benstagram.models.Photo;

public class ViewPostFragment extends Fragment{

    SquareImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_post , container , false);

        imageView = view.findViewById(R.id.imageViewPhoto);

        Bundle args = getArguments();

        int activityNumber = args.getInt(getString(R.string.activity_number));
        Photo photo = args.getParcelable(getString(R.string.photo));

        UniversalImageLoader.setImage(photo.getImage_path() , imageView , null, "");

        return view;
    }
}
