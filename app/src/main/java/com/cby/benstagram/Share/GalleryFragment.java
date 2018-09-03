package com.cby.benstagram.Share;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.cby.benstagram.R;
import com.cby.benstagram.Util.FilePaths;
import com.cby.benstagram.Util.FileSearch;

import java.util.ArrayList;

public class GalleryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "GalleryFragment";

    //widgets
    private GridView gridView;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Spinner spinner;
    private ImageView imgClose;
    private TextView txtNext;

    //vars
    private ArrayList<String> directories;

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                directories);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        }
    }
}
