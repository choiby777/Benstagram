package com.cby.benstagram.Home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.cby.benstagram.Adapters.ChatRoomListAdapter;
import com.cby.benstagram.Adapters.UserListAdapter;
import com.cby.benstagram.Profile.ProfileActivity;
import com.cby.benstagram.R;
import com.cby.benstagram.models.ChatRoom;
import com.cby.benstagram.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageFragment extends Fragment {

    private static final String TAG = "MessageFragment";
    private FirebaseAuth mAuth;
    private DatabaseReference mDbReference;
    private ChatRoomListAdapter chatRoomListAdapter;
    private List<ChatRoom> chatRoomList;

    private ListView listChatRooms;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        listChatRooms = view.findViewById(R.id.listChatRooms);

        setupFirebaseAuth();

        chatRoomList = new ArrayList<>();

        Query query = mDbReference.child(getString(R.string.dbname_user_chatting_rooms))
                .child(mAuth.getUid())
                .orderByChild(getString(R.string.field_roomId));

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: ");

                ChatRoom chatRoom = dataSnapshot.getValue(ChatRoom.class);
                chatRoomList.add(chatRoom);
                setupChatRoomListAdapter();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged: ");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: ");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setupChatRoomListAdapter();

        return view;
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: start");

        mAuth = FirebaseAuth.getInstance();
        mDbReference = FirebaseDatabase.getInstance().getReference();
    }

    private void setupChatRoomListAdapter() {

        chatRoomListAdapter = new ChatRoomListAdapter(getActivity(), R.layout.layout_chatroomlist_item , chatRoomList);
        listChatRooms.setAdapter(chatRoomListAdapter);

        listChatRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: clicked Item position : " + position);

                ChatRoom clickedChatRoom = chatRoomList.get(position);

                Log.d(TAG, "onItemClick: clickedChatRoom : " + clickedChatRoom.toString());

            }
        });
    }
}
