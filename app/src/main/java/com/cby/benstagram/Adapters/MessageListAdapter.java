package com.cby.benstagram.Adapters;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cby.benstagram.R;
import com.cby.benstagram.Util.UniversalImageLoader;
import com.cby.benstagram.models.MessageListItem;
import com.cby.benstagram.models.Photo;
import com.cby.benstagram.models.RecmmendUserInfo;
import com.cby.benstagram.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<MessageListItem> dataList;
    private Context mContext;
    public static final int VIEWTYPE_SEND_MESSGE = 0;
    public static final int VIEWTYPE_RECV_MESSGE = 1;

    public MessageListAdapter(Context context, ArrayList<MessageListItem> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        String userId = dataList.get(position).getUserInfo().getUser_id();
        String fbUid = FirebaseAuth.getInstance().getUid();

        if (userId.equals(fbUid)) return VIEWTYPE_SEND_MESSGE;
        else return VIEWTYPE_RECV_MESSGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == VIEWTYPE_SEND_MESSGE){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_messagelist_send_item, null);
            SendMessageViewHolder sendMessageViewHolder = new SendMessageViewHolder(v);
            return sendMessageViewHolder;
        }
        else{// if (viewType == 1){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_messagelist_recv_item, null);
            ReceiveMessageViewHolder receiveMessageViewHolder = new ReceiveMessageViewHolder(v);
            return receiveMessageViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemRowHolder, int position) {

        MessageListItem messageListItem = dataList.get(position);

        if (itemRowHolder.getItemViewType() == VIEWTYPE_SEND_MESSGE) {
            SendMessageViewHolder sendMessageViewHolder = (SendMessageViewHolder)itemRowHolder;
            sendMessageViewHolder.txtMessage.setText(messageListItem.getMessageInfo().getMessageText());

        }
        else if (itemRowHolder.getItemViewType() == VIEWTYPE_RECV_MESSGE){
            ReceiveMessageViewHolder receiveMessageViewHolder = (ReceiveMessageViewHolder)itemRowHolder;
            receiveMessageViewHolder.txtMessage.setText(messageListItem.getMessageInfo().getMessageText());
        }
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class SendMessageViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUserProfile;
        TextView txtMessage;

        public SendMessageViewHolder(View view) {
            super(view);

            imgUserProfile = view.findViewById(R.id.imgUser);
            txtMessage = view.findViewById(R.id.txtSendMessage);
        }
    }

    public class ReceiveMessageViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUserProfile;
        TextView txtMessage;
        TextView txtSendUserName;

        public ReceiveMessageViewHolder(View view) {
            super(view);

            imgUserProfile = view.findViewById(R.id.imgUser);
            txtMessage = view.findViewById(R.id.txtReceiveMessage);
            txtSendUserName = view.findViewById(R.id.txtUserName);
        }
    }
}
