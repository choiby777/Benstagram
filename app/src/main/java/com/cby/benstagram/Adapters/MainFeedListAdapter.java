package com.cby.benstagram.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cby.benstagram.R;
import com.cby.benstagram.Util.UniversalImageLoader;
import com.cby.benstagram.models.Photo;

import java.util.List;

public class MainFeedListAdapter extends ArrayAdapter<Photo> implements View.OnClickListener {
    private static final String TAG = "MainFeedListAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mLayoutResourceId;

    public MainFeedListAdapter(
            @NonNull Context context,
            int resource,
            @NonNull List<Photo> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResourceId = resource;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageViewPhoto){

        }else  if (v.getId() == R.id.imgComments){

        }
    }

    private static class ViewHolder{
        ImageView imgUserProfile;
        ImageView imgMoreMenu;
        ImageView imageViewPhoto;
        ImageView imgHeartWhite;
        ImageView imgHeartRed;
        ImageView imgComments;
        ImageView imageSend;
        ImageView imageClip;

        TextView txtUserName;
        TextView txtLikedInfo;
        TextView txtTags;
        TextView txtCommentInfo;
        TextView txtDaysInfo;
    }

    private static class ViewHolderEx{
        TextView txtTitle;
        TextView txtShowAll;
        RecyclerView rvRecommendUserList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final MainFeedListAdapter.ViewHolder holder;

        if (position == 1){
//            LinearLayoutManager mLayoutManager;
//            mLayoutManager = new LinearLayoutManager(parent.getContext());
//            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL
//            convertView = mLayoutInflater.inflate(R.layout.layout_recommend_user_list, parent, false);
//            return convertView;
//            final MainFeedListAdapter.ViewHolderEx holder2;
//
//            holder2 = new MainFeedListAdapter.ViewHolderEx();
//
//            RecyclerView.LayoutManager layoutManager = new CustomLinearLayoutManager(parent.getContext() , LinearLayoutManager.HORIZONTAL, false);
//            holder2.rvRecommendUserList.setLayoutManager(layoutManager);
//            convertView = mLayoutInflater.inflate(R.layout.layout_recommend_user_list, parent, false);
//            convertView.setTag(holder2);
//
//            return convertView;
        }

        if (convertView == null){
            convertView = mLayoutInflater.inflate(mLayoutResourceId , parent, false);
            holder = new MainFeedListAdapter.ViewHolder();
            holder.imgUserProfile = convertView.findViewById(R.id.imgUserProfile);
            holder.imgMoreMenu = convertView.findViewById(R.id.imgMoreMenu);
            holder.imageViewPhoto = convertView.findViewById(R.id.imageViewPhoto);
            holder.imgHeartWhite = convertView.findViewById(R.id.imgHeartWhite);
            holder.imgHeartRed = convertView.findViewById(R.id.imgHeartRed);
            holder.imgComments = convertView.findViewById(R.id.imgComments);
            holder.imageSend = convertView.findViewById(R.id.imageSend);
            holder.imageClip = convertView.findViewById(R.id.imageClip);
            holder.txtUserName = convertView.findViewById(R.id.txtUserName);
            holder.txtLikedInfo = convertView.findViewById(R.id.txtLikedInfo);
            holder.txtTags = convertView.findViewById(R.id.txtTags);
            holder.txtCommentInfo = convertView.findViewById(R.id.txtCommentInfo);
            holder.txtDaysInfo = convertView.findViewById(R.id.txtDaysInfo);

            convertView.setTag(holder);
        }else{
            holder = (MainFeedListAdapter.ViewHolder) convertView.getTag();
        }

        holder.imgComments.setOnClickListener(this);
        holder.imgMoreMenu.setOnClickListener(this);
        holder.imgUserProfile.setOnClickListener(this);
        holder.imageViewPhoto.setOnClickListener(this);
        holder.imageSend.setOnClickListener(this);
        holder.imageClip.setOnClickListener(this);

        holder.txtUserName.setOnClickListener(this);
        holder.txtLikedInfo.setOnClickListener(this);
        holder.txtTags.setOnClickListener(this);
        holder.txtCommentInfo.setOnClickListener(this);

        Photo photo = getItem(position);

        UniversalImageLoader.setImage(photo.getImage_path() , holder.imageViewPhoto , null , "");

        return convertView;
    }
}
