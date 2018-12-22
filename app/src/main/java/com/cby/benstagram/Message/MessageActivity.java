package com.cby.benstagram.Message;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.cby.benstagram.R;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";

    private Context mContext  = MessageActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Log.d(TAG, "onCreate: starting");
    }
}
