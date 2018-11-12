package com.cby.benstagram.Home;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cby.benstagram.R;
import com.cby.benstagram.Util.FirebaseHelper;
import com.cby.benstagram.Util.MainFeedListAdapter;
import com.cby.benstagram.models.Photo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    // Widgets
    private ListView lvMainFeeds;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;

    // vars
    private List<Photo> mainFeedList;
    private MainFeedListAdapter mainFeedListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        lvMainFeeds = view.findViewById(R.id.lvMainFeeds);
        mainFeedList = new ArrayList<>();

        for (int i=0; i<10; i++){
            mainFeedList.add(new Photo());
            mainFeedList.add(new Photo());
            mainFeedList.add(new Photo());
            mainFeedList.add(new Photo());
        }

        setupFirebaseAuth();

        setupMainFeedListAdapter();

        return view;
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: start");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDbReference = mFirebaseDatabase.getReference();
    }

    private void setupMainFeedListAdapter() {
        mainFeedListAdapter = new MainFeedListAdapter(getActivity() , R.layout.layout_mainfeed_listitem, mainFeedList);
        lvMainFeeds.setAdapter(mainFeedListAdapter);
    }
}
