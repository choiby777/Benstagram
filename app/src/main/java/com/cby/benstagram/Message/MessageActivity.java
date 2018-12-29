package com.cby.benstagram.Message;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cby.benstagram.Adapters.MessageListAdapter;
import com.cby.benstagram.R;
import com.cby.benstagram.models.ChattingMessage;
import com.cby.benstagram.models.MessageListItem;
import com.cby.benstagram.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAA1fbjQME:APA91bHx9zRGPfMC7soGkW0iuQmLJ05Zp6s4BvQE5VkZFNUDfJQwXIyWrCCl2F9Mlvp9_TGMW-Cwu_rdjENEKmZuZLINDU0di2bOVEpVNNDCRoYy9Pr0Zgt7INilXZISVLGYtDAZOpgG8AG1QmJAnJ2h2F6Gn0RTaw";

    private Context mContext  = MessageActivity.this;
    private InputMethodManager inputMethodManager;
    private User targetUser;
    private String chattingRoomKey;
    private ArrayList<MessageListItem> messageListItems;

    @BindView(R.id.imgBackArrow) ImageView imgBackArrow;
    @BindView(R.id.imgSearch) ImageView imgSearch;
    @BindView(R.id.imgMenu) ImageView imgMenu;
    @BindView(R.id.txtUserName) TextView txtUserName;

    @BindView(R.id.imgInputUser) ImageView imgInputUser;
    @BindView(R.id.imgSendMessage) ImageView imgSendMessage;
    @BindView(R.id.txtMessage) EditText txtMessage;
    @BindView(R.id.listMessages) RecyclerView listMessages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Log.d(TAG, "onCreate: starting");
        inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        ButterKnife.bind(this);

        targetUser = getIntent().getParcelableExtra(getString(R.string.selected_user));
        chattingRoomKey = getIntent().getStringExtra(getString(R.string.chatting_room_key));

        txtUserName.setText(targetUser.getUsername());

        setupMessageListAdapter();
    }

    @OnClick(R.id.imgBackArrow)
    void onBackArrowImageClick(){
        Log.d(TAG, "onBackArrowImageClick: ");
    }

    @OnClick(R.id.imgSearch)
    void onSearchImageClick(){
        Log.d(TAG, "onSearchImageClick: ");
    }

    @OnClick(R.id.imgMenu)
    void onMenuImageClick(){
        Log.d(TAG, "onMenuImageClick: ");
    }

    @OnClick(R.id.imgSendMessage)
    void onSendMessageClick(){
        Log.d(TAG, "onSendMessageClick: ");

        String message = txtMessage.getText().toString();

        String sendUserId = FirebaseAuth.getInstance().getUid();
        DatabaseReference mDbReference = FirebaseDatabase.getInstance().getReference();

        String messageId = mDbReference.push().getKey();

        ChattingMessage chatMessage = new ChattingMessage(messageId , "Text" , message , sendUserId, chattingRoomKey);

        mDbReference.child(getString(R.string.dbname_chatting_messages))
                .child(chattingRoomKey)
                .child(messageId)
                .setValue(chatMessage);

        mDbReference.child(getString(R.string.dbname_chatting_rooms))
                .child(chattingRoomKey)
                .child(getString(R.string.field_last_message))
                .setValue(message);

        mDbReference.child(getString(R.string.dbname_user_chatting_rooms))
                .child(sendUserId)
                .child(chattingRoomKey)
                .child(getString(R.string.field_last_message))
                .setValue(message);

        mDbReference.child(getString(R.string.dbname_user_chatting_rooms))
                .child(targetUser.getUser_id())
                .child(chattingRoomKey)
                .child(getString(R.string.field_last_message))
                .setValue(message);

        String userToken = targetUser.getToken();

        sendMessageToUser(userToken, message);

        txtMessage.setText("");
        inputMethodManager.hideSoftInputFromWindow(txtMessage.getWindowToken(),0);
    }

    private void sendMessageToUser(final String userToken , final String message) {

        String sendUserId = FirebaseAuth.getInstance().getUid();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // FMC 메시지 생성 start
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", message);
                    notification.put("title", getString(R.string.app_name));
                    root.put("notification", notification);
                    root.put("to", userToken);
                    // FMC 메시지 생성 end

                    URL Url = new URL(FCM_MESSAGE_URL);
                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-type", "application/json");
                    OutputStream os = conn.getOutputStream();
                    os.write(root.toString().getBytes("utf-8"));
                    os.flush();
                    conn.getResponseCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void setupMessageListAdapter() {

        messageListItems = new ArrayList<>();

        User curUser = new User();
        curUser.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        curUser.setUsername("나야나");

        User user2 = new User();
        user2.setUser_id("sdjdfhskjdfhkjsjahjdfh");
        user2.setUsername("너너너");

        messageListItems.add(new MessageListItem(new ChattingMessage("안녕하세요") , curUser));
        messageListItems.add(new MessageListItem(new ChattingMessage("네 안녕하세요 반갑습니다.") , user2));
        messageListItems.add(new MessageListItem(new ChattingMessage("성함이??") , curUser));
        messageListItems.add(new MessageListItem(new ChattingMessage("홍길동 입니다.") , user2));


        MessageListAdapter adapter = new MessageListAdapter(mContext , messageListItems);

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);

        listMessages.setHasFixedSize(true);
        listMessages.setLayoutManager(linearLayoutManager);
        listMessages.setAdapter(adapter);
    }
}
