package com.cby.benstagram.Util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cby.benstagram.Adapters.GridImageAdapter;
import com.cby.benstagram.Message.MessageActivity;
import com.cby.benstagram.Profile.AccountSettingActivity;
import com.cby.benstagram.R;
import com.cby.benstagram.models.Comment;
import com.cby.benstagram.models.Like;
import com.cby.benstagram.models.Photo;
import com.cby.benstagram.models.User;
import com.cby.benstagram.models.UserAccountSettings;
import com.cby.benstagram.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileFragment extends Fragment
        implements View.OnClickListener, FirebaseAuth.AuthStateListener{

    private static final String TAG = "ViewProfileFragment";
    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 4;

    public interface OnGridImageSelectedListener{
         void onGridImageSelected(Photo photo, int activityNumber);
    }

    OnGridImageSelectedListener mOnGridImageSelectedListener;

    // Widgets
    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mWebSite, mDescription;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private BottomNavigationViewEx bottomNavigationView;
    private Button btnFollow;
    private Button btnMessage;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;
    private FirebaseHelper mFirebaseHelper;

    // Vars
    private User mUser;
    private Context mContext;
    private boolean mIsFollowing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_profile , container , false);
        Log.d(TAG, "onCreateView: Started");

        mContext = getActivity();
        mUser = getUserFromBundle();

        mPosts = view.findViewById(R.id.txtPostValue);
        mFollowers = view.findViewById(R.id.txtfollowersValue);
        mFollowing = view.findViewById(R.id.txtFollowingValue);
        mDisplayName = view.findViewById(R.id.txt_display_name);
        mWebSite = view.findViewById(R.id.txt_website);
        mDescription= view.findViewById(R.id.txt_description);

        mProgressBar = view.findViewById(R.id.profileProgressBar);
        mProfilePhoto = view.findViewById(R.id.profile_image);
        gridView = view.findViewById(R.id.gridImages);
        toolbar = view.findViewById(R.id.profileToolbar);
        bottomNavigationView= view.findViewById(R.id.bottomNavViewBar);
        btnFollow = view.findViewById(R.id.btnFollow);
        btnMessage = view.findViewById(R.id.btnMessage);
        btnFollow.setOnClickListener(this);
        btnMessage.setOnClickListener(this);

        setupBottomNavigationView();
        setupFirebaseAuth();

        setProfileWidgets();
        setupGridView();

        updateFollowingStatus();
        
        mDbReference.child(getString(R.string.dbname_followers))
                .child(mUser.getUser_id())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long count = dataSnapshot.getChildrenCount();
                        mFollowers.setText(String.format("%d", count));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        mDbReference.child(getString(R.string.dbname_following))
                .child(mUser.getUser_id())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long count = dataSnapshot.getChildrenCount();
                        mFollowing.setText(String.format("%d", count));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        mDbReference.child(getString(R.string.dbname_user_photos))
                .child(mUser.getUser_id())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long count = dataSnapshot.getChildrenCount();
                        mPosts.setText(String.format("%d", count));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return view;
    }

    private void updateFollowingStatus() {

        String curUserId = FirebaseAuth.getInstance().getUid();

        Query query = mDbReference.child(getString(R.string.dbname_following))
                .child(curUserId)
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(mUser.getUser_id());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mIsFollowing = dataSnapshot.hasChildren();

                if (!mIsFollowing){
                    btnFollow.setText("Unfollow");
                }else{
                    btnFollow.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private User getUserFromBundle() {

        Bundle args = getArguments();

        if (args != null) {
            return args.getParcelable(getString(R.string.user));
        } else {
            return null;
        }
    }

    private void setProfileWidgets() {

        Query query = mDbReference.child(getString(R.string.dbname_users))
                .child(mUser.getUser_id());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final User user = dataSnapshot.getValue(User.class);

                Query queryUserAccountSetting = mDbReference
                        .child(mContext.getString(R.string.dbname_user_account_settings))
                        .child(mUser.getUser_id());

                queryUserAccountSetting.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        UserAccountSettings userAccountSettings = dataSnapshot.getValue(UserAccountSettings.class);

                        UserSettings userSettings = new UserSettings(user , userAccountSettings);

                        setProfileWidgets(userSettings);

                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {

        try {
            mOnGridImageSelectedListener = (OnGridImageSelectedListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException " + e.getMessage() );
        }

        super.onAttach(context);
    }

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext , getActivity(), bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void setupGridView() {

        Log.d(TAG, "setupGridView: start");

        final ArrayList<Photo> photos = new ArrayList<>();

        Query query = mDbReference
                .child(getString(R.string.dbname_user_photos))
                .child(mUser.getUser_id());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for ( DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

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

                    //photos.add(singleSnapshot.getValue(Photo.class));
                    photos.add(photo);
                }

                setupGridImages(photos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query canclled");
            }
        });

        Log.d(TAG, "setupGridView: end");
    }

    private void setupGridImages(final ArrayList<Photo> photos) {

        final ArrayList<String> imgURLs = new ArrayList<>();

        for (int i=0; i<photos.size(); i++){
            imgURLs.add(photos.get(i).getImage_path());
        }

        int gridWidth = getResources().getDisplayMetrics().widthPixels;

        int imageWidth = gridWidth / NUM_GRID_COLUMNS;

        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "" , imgURLs);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnGridImageSelectedListener.onGridImageSelected(photos.get(position) , ACTIVITY_NUM);
            }
        });
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: ");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDbReference = mFirebaseDatabase.getReference();
        mFirebaseHelper = new FirebaseHelper(mContext);
    }

    private void setProfileWidgets(UserSettings userSettings){

        User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSetting();

        UniversalImageLoader.setImage(settings.getProfile_photo() , mProfilePhoto , null , "");

        mDisplayName.setText(settings.getDisplay_name());
        mWebSite.setText(settings.getWebsite());
        mPosts.setText(String.valueOf(settings.getPosts()));
        mFollowers.setText(String.valueOf(settings.getFollowers()));
        mFollowing.setText(String.valueOf(settings.getFollowing()));
        mDescription.setText(settings.getDescription());
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.txtEditProfile){
            Intent intent = new Intent(mContext , AccountSettingActivity.class);
            intent.putExtra(getString(R.string.calling_activity) , getString(R.string.profile_activity));
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fade_in , R.anim.fade_out);
        }else if (view.getId() == R.id.btnFollow){

            if (mIsFollowing){
                UnFollowUser();
            }else {
                FollowUser();
            }
        }else if (view.getId() == R.id.btnMessage){
            startMessageActivity();
        }
    }

    private void startMessageActivity() {
        Log.d(TAG, "startMessageActivity: start");

        final String userId = FirebaseAuth.getInstance().getUid();
        Query queryUserChattingRooms = mDbReference
                .child(mContext.getString(R.string.dbname_user_chatting_rooms))
                .child(userId);

        queryUserChattingRooms.addListenerForSingleValueEvent(new ValueEventListener() {

              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                  String chattingRoomKey = null;

                  if (dataSnapshot.exists()) {

                      // chattingRoom id들을 가져와서
                      for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                          List<String> chatUserIds = new ArrayList<>();
                          for (DataSnapshot ds : postSnapshot.getChildren()) {
                              String chatUserId = ds.getKey();
                              chatUserIds.add(chatUserId);
                          }

                          // 해당 유저와의 대화방이 있는경우 해당 대화방 키를 사용
                          if (chatUserIds.size() == 2 && chatUserIds.contains(mUser.getUser_id())){
                              chattingRoomKey = postSnapshot.getKey();

                              Log.d(TAG, "onDataChange: roomKey : " + chattingRoomKey);
                          }else{
                              // chatting_room 생성
                              createChattingRoom(userId , mUser.getUser_id());
                          }
                      }

                  } else {
                      // chatting_room 생성
                      createChattingRoom(userId , mUser.getUser_id());
                  }

                  if (chattingRoomKey != null){
                      Intent intent = new Intent(mContext , MessageActivity.class);
                      intent.putExtra(getString(R.string.selected_user) , mUser);
                      intent.putExtra(getString(R.string.chatting_room_key) , chattingRoomKey);
                      startActivity(intent);
                  }else{
                      Toast.makeText(mContext , "Chatting information is not valid" , Toast.LENGTH_SHORT).show();
                  }
              }

            private void createChattingRoom(String userId1, String userId2) {
                String chattingRoomKey = mDbReference.push().getKey();
                Map<String, Boolean> userIds = new HashMap<>();
                userIds.put(userId1, true);
                userIds.put(userId2, true);

                mDbReference.child(getString(R.string.dbname_chatting_rooms))
                        .child(chattingRoomKey)
                        .setValue(userIds);

                mDbReference.child(getString(R.string.dbname_user_chatting_rooms))
                        .child(userId1)
                        .child(chattingRoomKey)
                        .setValue(userIds);

                mDbReference.child(getString(R.string.dbname_user_chatting_rooms))
                        .child(userId2)
                        .child(chattingRoomKey)
                        .setValue(userIds);
            }

            @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
    }

    private void FollowUser() {
        String curUserId = FirebaseAuth.getInstance().getUid();

        mDbReference.child(getString(R.string.dbname_followers))
                .child(mUser.getUser_id())
                .child(curUserId)
                .child(getString(R.string.field_user_id))
                .setValue(curUserId);

        mDbReference.child(getString(R.string.dbname_following))
                .child(curUserId)
                .child(mUser.getUser_id())
                .child(getString(R.string.field_user_id))
                .setValue(mUser.getUser_id());

        updateFollowingStatus();
    }

    private void UnFollowUser() {
        String curUserId = FirebaseAuth.getInstance().getUid();

        mDbReference.child(getString(R.string.dbname_followers))
                .child(mUser.getUser_id())
                .child(curUserId)
                .removeValue();

        mDbReference.child(getString(R.string.dbname_following))
                .child(curUserId)
                .child(mUser.getUser_id())
                .removeValue();

        updateFollowingStatus();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null){
            Log.d(TAG, "onAuthStateChanged: signed_in : " + user.getUid());


        }else{
            Log.d(TAG, "onAuthStateChanged: signed_out");
        }
    }
}
