package com.cby.benstagram.Message;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cby.benstagram.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";

    private Context mContext  = MessageActivity.this;

    @BindView(R.id.imgBackArrow) ImageView imgBackArrow;
    @BindView(R.id.imgSearch) ImageView imgSearch;
    @BindView(R.id.imgMenu) ImageView imgMenu;
    @BindView(R.id.txtUserName) TextView txtUserName;

    @BindView(R.id.imgInputUser) ImageView imgInputUser;
    @BindView(R.id.imgSendMessage) ImageView imgSendMessage;
    @BindView(R.id.txtMessage) EditText txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Log.d(TAG, "onCreate: starting");

        ButterKnife.bind(this);
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
    }

}
