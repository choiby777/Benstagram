package com.cby.benstagram.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cby.benstagram.R;
import com.cby.benstagram.models.ChatRoom;

import java.util.List;

public class ChatRoomListAdapter extends ArrayAdapter<ChatRoom> {

    private static final String TAG = "ChatRoomListAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mLayoutResourceId;

    public ChatRoomListAdapter(
            @NonNull Context context,
            int resource,
            @NonNull List<ChatRoom> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResourceId = resource;
    }

    private static class ViewHolder{
        ImageView imgUser;
        TextView txtUserName;
        TextView txtLastMessage;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ChatRoomListAdapter.ViewHolder holder;

        if (convertView == null){
            convertView = mLayoutInflater.inflate(mLayoutResourceId , parent, false);
            holder = new ChatRoomListAdapter.ViewHolder();
            holder.imgUser = convertView.findViewById(R.id.imgUser);
            holder.txtUserName = convertView.findViewById(R.id.txtUserName);
            holder.txtLastMessage = convertView.findViewById(R.id.txtLastMessage);

            convertView.setTag(holder);
        }else{
            holder = (ChatRoomListAdapter.ViewHolder) convertView.getTag();
        }

        ChatRoom room = getItem(position);

        holder.txtUserName.setText(room.getRoomTitle());
        holder.txtLastMessage.setText(room.getLast_message());

        return convertView;
    }
}
