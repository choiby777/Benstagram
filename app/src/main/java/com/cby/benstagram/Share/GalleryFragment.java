package com.cby.benstagram.Share;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.cby.benstagram.Profile.AccountSettingActivity;
import com.cby.benstagram.R;
import com.cby.benstagram.Util.FilePaths;
import com.cby.benstagram.Util.FileSearch;
import com.cby.benstagram.Adapters.GridImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class GalleryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "GalleryFragment";

    //constance
    private final static int NUM_GRID_COLUMNS = 3;

    //widgets
    private GridView gridView;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Spinner spinner;
    private ImageView imgClose;
    private TextView txtNext;

    //vars
    private ArrayList<String> directories;
    private String mAppend = "file:/";
    private String mSelectedImagePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        gridView = view.findViewById(R.id.girdImages);
        imageView = view.findViewById(R.id.imageView);
        progressBar = view.findViewById(R.id.proglessLoadingImage);
        spinner = view.findViewById(R.id.spinnerDirectory);
        imgClose = view.findViewById(R.id.imgClose);
        txtNext = view.findViewById(R.id.txtNext);

        imgClose.setOnClickListener(this);
        txtNext.setOnClickListener(this);

        progressBar.setVisibility(View.GONE);

        init();

        return view;
    }

    private void init() {

        // 폴더 리스트를 가져와서 콤보박스를 구성한다.
        FilePaths filePaths = new FilePaths();

        if (FileSearch.getDirectoryPaths(filePaths.PICTURES) != null){
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }

        directories.add(filePaths.CAMERA);

        ArrayList<String> directoryNames = new ArrayList<>();

        for (int i=0; i<directories.size(); i++){
            String directoryPath = directories.get(i);
            int indexOfSlash = directoryPath.lastIndexOf("/");
            String directoryName = directoryPath.substring(indexOfSlash + 1);
            directoryNames.add(directoryName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                directoryNames);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDirectoryPath = directories.get(position);
                Log.d(TAG, "onItemSelected: " + selectedDirectoryPath);

                setupGridView(selectedDirectoryPath);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean isRootTask(){
        return ((ShareActivity)getActivity()).getTask() == 0;
    }

    private void setupGridView(String selectedDirectoryPath) {
        Log.d(TAG, "setupGridView: " + selectedDirectoryPath);

        // 선택한 폴더의 이미지 리스트를 가져온다.
        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectoryPath);

        // 해상도를 기준으로 이미지의 크기를 정하여 Grid에 set한다.
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        // GridImageAdapter 생성하여 해당 widget에 set한다.
        GridImageAdapter adapter = new GridImageAdapter(getActivity() , R.layout.layout_grid_imageview, mAppend, imgURLs);
        gridView.setAdapter(adapter);

        if (imgURLs.size() > 0){
            mSelectedImagePath = imgURLs.get(0);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setImage(imgURLs.get(position) , imageView, mAppend);
                mSelectedImagePath = imgURLs.get(position);
            }
        });
    }

    private void setImage(String imgURL, ImageView imageView, String append) {
        Log.d(TAG, "setImage: " + append + imgURL);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(append + imgURL, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgClose){

            Log.d(TAG, "onClick: clicked close button");
            getActivity().finish();

        }else if (v.getId() == R.id.txtNext){
            Log.d(TAG, "onClick: clicked next button");

            if (isRootTask()){
                Intent intent = new Intent(getActivity(), NextActivity.class);
                intent.putExtra(getString(R.string.selected_image) , mSelectedImagePath);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
                intent.putExtra(getString(R.string.selected_image) , mSelectedImagePath);
                intent.putExtra(getString(R.string.return_to_fragment) , getString(R.string.edit_profile_fragment));
                startActivity(intent);
                getActivity().finish();
            }
        }
    }
}
