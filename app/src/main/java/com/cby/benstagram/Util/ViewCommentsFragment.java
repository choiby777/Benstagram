package com.cby.benstagram.Util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cby.benstagram.R;
import com.cby.benstagram.models.Comment;
import com.cby.benstagram.models.Like;
import com.cby.benstagram.models.Photo;
import com.cby.benstagram.models.User;
import com.cby.benstagram.models.UserAccountSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ViewCommentsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ViewCommentsFragment";

    private Photo photo;
    private List<Comment> commentList;

    // Widgets
    private ListView listComments;
    private ImageView imgUser;
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

        setupFirebaseAuth();

        setupWidgets(view);

        setupCommentList();

        photo = getPhotoFromBundle();

        imgAddComment.setOnClickListener(this);

        return view;
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

    private void setupCommentList() {
        commentList = new ArrayList<>();

        commentList.add(new Comment());
        commentList.add(new Comment());
        commentList.add(new Comment());

        CommentListAdapter adapter = new CommentListAdapter(getActivity() , R.layout.layout_comment, commentList);
        listComments.setAdapter(adapter);
    }

    private void setupWidgets(View view) {
        listComments = view.findViewById(R.id.listComments);
        imgUser = view.findViewById(R.id.imgUser);
        txtInputComment = view.findViewById(R.id.txtInputComment);
        imgAddComment = view.findViewById(R.id.imgAddComment);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgAddComment){
            Log.d(TAG, "onClick: imgAddComment clicked");

            addNewComment();

            txtInputComment.setText("");
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

        commentList.add(comment);
    }
}
