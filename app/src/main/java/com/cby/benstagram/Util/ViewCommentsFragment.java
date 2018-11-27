package com.cby.benstagram.Util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.cby.benstagram.Adapters.CommentListAdapter;
import com.cby.benstagram.R;
import com.cby.benstagram.models.Comment;
import com.cby.benstagram.models.CommentUser;
import com.cby.benstagram.models.Photo;
import com.cby.benstagram.models.User;
import com.cby.benstagram.models.UserAccountSettings;
import com.cby.benstagram.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ViewCommentsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ViewCommentsFragment";

    private InputMethodManager inputMethodManager;
    private Photo photo;
    private List<CommentUser> commentList;

    // Widgets
    private ListView listComments;
    private ImageView imgUser;
    private ImageView imgCommentInputUser;
    private EditText txtInputComment;
    private ImageView imgAddComment;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;
    private FirebaseHelper mFirebaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_comments, container, false);
        commentList = new ArrayList<>();

        inputMethodManager = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);

        photo = getPhotoFromBundle();

        setupWidgets(view);

        setupFirebaseAuth();

        setupCommentsEventListener();

        setupProfilePhoto();

        imgAddComment.setOnClickListener(this);

        return view;
    }

    private void setupProfilePhoto() {

        final String userId = mAuth.getCurrentUser().getUid();

        Query query = mDbReference.child(getString(R.string.dbname_users))
                .child(userId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final User user = dataSnapshot.getValue(User.class);

                Query queryUserAccountSetting = mDbReference
                        .child(getString(R.string.dbname_user_account_settings))
                        .child(userId);

                queryUserAccountSetting.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        UserAccountSettings userAccountSettings = dataSnapshot.getValue(UserAccountSettings.class);

                        UserSettings userSettings = new UserSettings(user , userAccountSettings);

                        //setProfileWidgets(userSettings);
                        UniversalImageLoader.setImage(userAccountSettings.getProfile_photo() , imgUser , null , "");
                        UniversalImageLoader.setImage(userAccountSettings.getProfile_photo() , imgCommentInputUser , null , "");
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

    private void setupCommentsEventListener() {
        Log.d(TAG, "setupCommentsEventListener: start");

        mDbReference.child(getString(R.string.dbname_photos))
                .child(photo.getPhoto_id())
                .child(getString(R.string.field_comments))
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Log.d(TAG, "onChildAdded: " + s);

                        final Comment comment = dataSnapshot.getValue(Comment.class);

                        String commentUserId = comment.getUser_id();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Query query = reference
                                .child(getString(R.string.dbname_user_account_settings))
                                .child(commentUserId);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                UserAccountSettings userAccountSettings = dataSnapshot.getValue(UserAccountSettings.class);

                                CommentUser commentUser = new CommentUser(comment , userAccountSettings);
                                commentList.add(commentUser);

                                setupCommentListAdapter();

                                Log.d(TAG, "onDataChange: userAccountSettings : " + userAccountSettings.toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private Photo getPhotoFromBundle() {

        Bundle args = getArguments();

        if (args != null) {
            return args.getParcelable(getString(R.string.photo));
        } else {
            return null;
        }
    }
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: start");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDbReference = mFirebaseDatabase.getReference();
        mFirebaseHelper = new FirebaseHelper(getActivity());
    }

    private void setupCommentListAdapter() {
        final CommentListAdapter adapter = new CommentListAdapter(getActivity() , R.layout.layout_comment, commentList);
        listComments.setAdapter(adapter);
    }

    private void setupWidgets(View view) {
        listComments = view.findViewById(R.id.listComments);
        imgUser = view.findViewById(R.id.imgUser);
        imgCommentInputUser = view.findViewById(R.id.imgCommentInputUser);
        txtInputComment = view.findViewById(R.id.txtInputComment);
        imgAddComment = view.findViewById(R.id.imgAddComment);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgAddComment){
            Log.d(TAG, "onClick: imgAddComment clicked");

            addNewComment();

            txtInputComment.setText("");
            inputMethodManager.hideSoftInputFromWindow(txtInputComment.getWindowToken(),0);
        }
    }

    private void addNewComment() {
        Log.d(TAG, "addNewComment: start");

        Comment comment = new Comment();
        comment.setUser_id(mAuth.getUid());
        comment.setComment(txtInputComment.getText().toString());
        comment.setDate_created((new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss'Z'").format(new Date())));

        String newKey = mDbReference.push().getKey();

        mDbReference.child(getString(R.string.dbname_photos))
                .child(photo.getPhoto_id())
                .child(getString(R.string.field_comments))
                .child(newKey)
                .setValue(comment);

        mDbReference.child(getString(R.string.dbname_user_photos))
                .child(mAuth.getUid())
                .child(photo.getPhoto_id())
                .child(getString(R.string.field_comments))
                .child(newKey)
                .setValue(comment);
    }
}
