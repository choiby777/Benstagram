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
import com.cby.benstagram.models.Photo;
import com.cby.benstagram.models.RecmmendUserInfo;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Photo> dataList;
    private Context mContext;

    public MessageListAdapter(Context context, ArrayList<Photo> dataList) {
        this.dataList = dataList;
        this.mContext = context;

        if (this.dataList.size() > 0){
            // recommand story용
            this.dataList.add(0 , new Photo());

            // recommand user용
            this.dataList.add(2 , new Photo());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        else if (position == 2) return 1;
        else return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == 0){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_mainfeed_recommend_item, null);
            RecommandStoryViewHolder mh = new RecommandStoryViewHolder(v);
            return mh;
        }
        else if (viewType == 1){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recommend_user_list, null);
            RecommendUserItemViewHolder mh = new RecommendUserItemViewHolder(v);
            return mh;
        }
        else{
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_mainfeed_listitem, null);
            PhotoItemViewHolder mh = new PhotoItemViewHolder(v);
            return mh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemRowHolder, int i) {

        if (itemRowHolder.getItemViewType() == 0) {

        }
        else if (itemRowHolder.getItemViewType() == 1){
            RecommendUserItemViewHolder vh = (RecommendUserItemViewHolder)itemRowHolder;

            ArrayList<RecmmendUserInfo> mRecmmendUserListItems = new ArrayList<>();
            mRecmmendUserListItems.add(new RecmmendUserInfo("1111111" , "ssss" , "ffffffffff"));
            mRecmmendUserListItems.add(new RecmmendUserInfo("22222" , "ssss" , "ffffffffff"));
            mRecmmendUserListItems.add(new RecmmendUserInfo("333333" , "ssss" , "ffffffffff"));
            mRecmmendUserListItems.add(new RecmmendUserInfo("4444444" , "ssss" , "ffffffffff"));
            mRecmmendUserListItems.add(new RecmmendUserInfo("555555" , "ssss" , "ffffffffff"));
            mRecmmendUserListItems.add(new RecmmendUserInfo("6666666" , "ssss" , "ffffffffff"));

            RecommendUserListAdapter itemListDataAdapter = new RecommendUserListAdapter(mContext, mRecmmendUserListItems);

            vh.recommend_user_list_View.setHasFixedSize(true);
            vh.recommend_user_list_View.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            vh.recommend_user_list_View.setAdapter(itemListDataAdapter);
            vh.recommend_user_list_View.setNestedScrollingEnabled(false);
            vh.recommend_user_list_View.addItemDecoration(new RecyclerViewDecoration(10));

        }
        else{ // Photo
            PhotoItemViewHolder vh = (PhotoItemViewHolder)itemRowHolder;

            Photo photo = dataList.get(i);
            UniversalImageLoader.setImage(photo.getImage_path() , vh.imageViewPhoto , null , "");
        }
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class RecommandStoryViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProfile;
        ImageView img1;
        ImageView img2;
        ImageView img3;
        TextView txtMyStory;
        TextView txt1;
        TextView txt2;

        public RecommandStoryViewHolder(View view) {
            super(view);

            imgProfile = view.findViewById(R.id.imgProfile);
            img1 = view.findViewById(R.id.img1);
            img2 = view.findViewById(R.id.img2);
            img3 = view.findViewById(R.id.img3);
            txtMyStory = view.findViewById(R.id.txtMyStory);
            txt1 = view.findViewById(R.id.txt1);
            txt2 = view.findViewById(R.id.txt2);
        }
    }

    public class PhotoItemViewHolder extends RecyclerView.ViewHolder {

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

        public PhotoItemViewHolder(View view) {
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

    public class RecyclerViewDecoration extends RecyclerView.ItemDecoration{
        private int divWidth;

        public RecyclerViewDecoration(int divWidth){
            this.divWidth = divWidth;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = divWidth;
        }
    }

    public class RecommendUserItemViewHolder extends RecyclerView.ViewHolder {

        protected RecyclerView recommend_user_list_View;
        public RecommendUserItemViewHolder(View view) {
            super(view);

            this.recommend_user_list_View = (RecyclerView) view.findViewById(R.id.rvRecommendUserList);
        }
    }

}
