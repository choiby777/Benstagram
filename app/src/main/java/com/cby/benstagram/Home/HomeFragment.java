package com.cby.benstagram.Home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cby.benstagram.Adapters.MainFeedDataAdapter;
import com.cby.benstagram.R;
import com.cby.benstagram.Adapters.MainFeedListAdapter;
import com.cby.benstagram.models.Like;
import com.cby.benstagram.models.Photo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    // Widgets
    private ListView lvMainFeeds;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;

    // vars
    private ArrayList<Photo> mainFeedList;
    private MainFeedListAdapter mainFeedListAdapter;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        lvMainFeeds = view.findViewById(R.id.lvMainFeeds);
        mainFeedList = new ArrayList<>();
        mContext = getActivity();

//        for (int i=0; i<10; i++){
//            mainFeedList.add(new Photo());
//            mainFeedList.add(new Photo());
//            mainFeedList.add(new Photo());
//            mainFeedList.add(new Photo());
//        }


        setupFirebaseAuth();

        Query query = mDbReference.child(mContext.getString(R.string.dbname_photos))
                .orderByChild(mContext.getString(R.string.field_date_created));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mainFeedList.clear();

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                    Photo photo = new Photo();
                    Map<String , Object> objectMap = (HashMap<String , Object>)singleSnapshot.getValue();
                    photo.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
                    photo.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                    photo.setTags(objectMap.get(getString(R.string.field_tags)).toString());
                    photo.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());
                    photo.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
                    photo.setCaption(objectMap.get(getString(R.string.field_caption)).toString());

                    List<Like> likeList = new ArrayList<Like>();

                    for (DataSnapshot subSnapshot : singleSnapshot
                            .child(getString(R.string.field_likes)).getChildren()){

                        Like like = subSnapshot.getValue(Like.class);
                        likeList.add(like);
                    }

                    photo.setLikes(likeList);

                    mainFeedList.add(photo);
                }

                setupMainFeedListAdapter();

                setupMainFeedDataAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }

    private void setupMainFeedDataAdapter() {
        RecyclerView rvMainFeeds = getActivity().findViewById(R.id.rvMainFeeds);

        rvMainFeeds.setHasFixedSize(true);

        ArrayList<Photo> mainFeedDatas = new ArrayList<>();

        MainFeedDataAdapter adapter = new MainFeedDataAdapter(getActivity(), mainFeedList);

        rvMainFeeds.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        rvMainFeeds.setAdapter(adapter);
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
