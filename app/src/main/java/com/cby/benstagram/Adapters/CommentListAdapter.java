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
import com.cby.benstagram.Util.UniversalImageLoader;
import com.cby.benstagram.models.CommentUser;

import java.util.List;

public class CommentListAdapter extends ArrayAdapter<CommentUser> {

    private static final String TAG = "CommentListAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mLayoutResourceId;

    public CommentListAdapter(
            @NonNull Context context,
            int resource,
            @NonNull List<CommentUser> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResourceId = resource;
    }

    private static class ViewHolder{
        ImageView imgUser;
        TextView txtUserName;
        TextView txtComment;
        TextView txtDays;
        TextView txtLikeCount;
        TextView txtReply;
        ImageView imgHeart;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final CommentListAdapter.ViewHolder holder;

        if (convertView == null){
            convertView = mLayoutInflater.inflate(mLayoutResourceId , parent, false);
            holder = new CommentListAdapter.ViewHolder();
            holder.imgUser = convertView.findViewById(R.id.imgUser);
            holder.txtUserName = convertView.findViewById(R.id.txtUserName);
            holder.txtComment = convertView.findViewById(R.id.txtComment);
            holder.txtDays = convertView.findViewById(R.id.txtDays);
            holder.txtLikeCount = convertView.findViewById(R.id.txtLikeCount);
            holder.txtReply = convertView.findViewById(R.id.txtReply);
            holder.imgHeart = convertView.findViewById(R.id.imgHeart);

            convertView.setTag(holder);
        }else{
            holder = (CommentListAdapter.ViewHolder) convertView.getTag();
        }

        CommentUser comment = getItem(position);

        holder.txtUserName.setText(comment.getUserAccountSettings().getDisplay_name());
        holder.txtComment.setText(comment.getComment().getComment());
        holder.txtDays.setText("2 Days");
        holder.txtLikeCount.setText(comment.getComment().getLikesCountText());

        UniversalImageLoader.setImage(comment.getUserAccountSettings().getProfile_photo() , holder.imgUser , null , "");

        return convertView;
    }
}
