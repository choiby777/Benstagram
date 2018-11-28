package com.cby.benstagram.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cby.benstagram.R;
import com.cby.benstagram.models.RecmmendUserInfo;

import java.util.ArrayList;

public class RecommendUserListAdapter extends RecyclerView.Adapter<RecommendUserListAdapter.RecommendUserViewHolder>{

    public static class RecommendUserViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgUserProfile;
        public ImageView imgClose;
        public TextView txtUserName;
        public TextView txtDescription;
        public Button btnFollow;

        public RecommendUserViewHolder(View itemView) {
            super(itemView);

            imgUserProfile = itemView.findViewById(R.id.imgUserProfile);
            imgClose = itemView.findViewById(R.id.imgClose);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }

    private ArrayList<RecmmendUserInfo> mRecmmendUserListItems;
    private Context mContext;

    public RecommendUserListAdapter(Context context, ArrayList<RecmmendUserInfo> mRecmmendUserListItems) {
        this.mContext = context;
        this.mRecmmendUserListItems = mRecmmendUserListItems;
    }

    @NonNull
    @Override
    public RecommendUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recommend_user_list_item, null);
        RecommendUserViewHolder mh = new RecommendUserViewHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendUserViewHolder holder, int position) {

        RecmmendUserInfo data = mRecmmendUserListItems.get(position);

        holder.txtUserName.setText(data.getUserName());
        holder.txtDescription.setText(data.getDescription());
        //holder.imgUserProfile.setImageURI();data.getImageUrl();
    }

    @Override
    public int getItemCount() {
        return mRecmmendUserListItems.size();
    }

}
