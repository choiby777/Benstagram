package com.cby.benstagram.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cby.benstagram.R;

import java.util.ArrayList;

public class RecommendUserListAdapter extends RecyclerView.Adapter<RecommendUserListAdapter.RecommendUserListViewHolder>{

    public static class RecommendUserListViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgUserProfile;
        public ImageView imgClose;
        public TextView txtUserName;
        public TextView txtDescription;
        public Button btnFollow;

        public RecommendUserListViewHolder(View itemView) {
            super(itemView);

            imgUserProfile = itemView.findViewById(R.id.imgUserProfile);
            imgClose = itemView.findViewById(R.id.imgClose);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }

    class RecmmendUserListItem {
        private String userName;
        private String description;
        private String imageUrl;

        public RecmmendUserListItem(String userName, String description, String imageUrl) {
            this.userName = userName;
            this.description = description;
            this.imageUrl = imageUrl;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    private ArrayList<RecmmendUserListItem> mRecmmendUserListItems;

    @NonNull
    @Override
    public RecommendUserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recommend_user_list_item, parent, false);

        RecommendUserListViewHolder holder = new RecommendUserListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendUserListViewHolder holder, int position) {

        RecmmendUserListItem data = mRecmmendUserListItems.get(position);

        holder.txtUserName.setText(data.getUserName());
        holder.txtDescription.setText(data.getDescription());
        //holder.imgUserProfile.setImageURI();data.getImageUrl();
    }

    @Override
    public int getItemCount() {
        return mRecmmendUserListItems.size();
    }

}
