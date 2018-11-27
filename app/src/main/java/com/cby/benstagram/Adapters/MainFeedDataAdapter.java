package com.cby.benstagram.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cby.benstagram.R;
import com.cby.benstagram.models.MainFeedData;

import java.util.ArrayList;

public class MainFeedDataAdapter extends RecyclerView.Adapter<MainFeedDataAdapter.ItemRowHolder>{
    private ArrayList<MainFeedData> dataList;
    private Context mContext;

    public MainFeedDataAdapter(Context context, ArrayList<MainFeedData> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {

        final String sectionName = dataList.get(i).getHeaderTitle();

        ArrayList singleSectionItems = dataList.get(i).getAllItemsInSection();

        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, singleSectionItems);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);


        itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolderForPhoto extends RecyclerView.ViewHolder {

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

        public ItemRowHolderForPhoto(View view) {
            super(view);

            imgUserProfile = view.findViewById(R.id.imgUserProfile);
            imgMoreMenu = view.findViewById(R.id.imgMoreMenu);
            imageViewPhoto = view.findViewById(R.id.imageViewPhoto);
            imgHeartWhite = view.findViewById(R.id.imgHeartWhite);
            imgHeartRed = view.findViewById(R.id.imgHeartRed);
            imgComments = view.findViewById(R.id.imgComments);
            imageSend = view.findViewById(R.id.imageSend);
            imageClip = view.findViewById(R.id.imageClip);
            txtUserName = view.findViewById(R.id.txtUserName);
            txtLikedInfo = view.findViewById(R.id.txtLikedInfo);
            txtTags = view.findViewById(R.id.txtTags);
            txtCommentInfo = view.findViewById(R.id.txtCommentInfo);
            txtDaysInfo = view.findViewById(R.id.txtDaysInfo);
        }
    }

    public class ItemRowHolderForRecommendUsers extends RecyclerView.ViewHolder {

        protected RecyclerView recommend_user_list_View;
        public ItemRowHolderForRecommendUsers(View view) {
            super(view);

            this.recommend_user_list_View = (RecyclerView) view.findViewById(R.id.recycler_view_list);
        }
    }

}
