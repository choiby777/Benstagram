package com.cby.benstagram.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cby.benstagram.R;
import com.cby.benstagram.models.Comment;
import java.util.List;

public class CommentListAdapter extends ArrayAdapter<Comment> {

    private static final String TAG = "CommentListAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mLayoutResourceId;

    public CommentListAdapter(
            @NonNull Context context,
            int resource,
            @NonNull List<Comment> objects) {
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

        holder.txtUserName.setText("TestUser");
        holder.txtComment.setText(getItem(position).getComment());
        holder.txtDays.setText("2 Days");
        holder.txtLikeCount.setText("5 likes");

//        ImageLoader imageLoader = ImageLoader.getInstance();
//
//        imageLoader.displayImage(mAppend + imgURL, holder.mImageView, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//                if (holder.mProgressBar != null){
//                    holder.mProgressBar.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                if (holder.mProgressBar != null){
//                    holder.mProgressBar.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                if (holder.mProgressBar != null){
//                    holder.mProgressBar.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//                if (holder.mProgressBar != null){
//                    holder.mProgressBar.setVisibility(View.GONE);
//                }
//            }
//        });

        return convertView;
    }
}
