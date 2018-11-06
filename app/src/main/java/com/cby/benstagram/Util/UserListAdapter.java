package com.cby.benstagram.Util;

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
import com.cby.benstagram.models.UserSettings;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<UserSettings> {

    private static final String TAG = "UserListAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mLayoutResourceId;

    public UserListAdapter(
            @NonNull Context context,
            int resource,
            @NonNull List<UserSettings> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayoutResourceId = resource;
    }

    private static class ViewHolder{
        ImageView imgUser;
        TextView tvUserName;
        TextView tvUserEmail;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final UserListAdapter.ViewHolder holder;

        if (convertView == null){
            convertView = mLayoutInflater.inflate(mLayoutResourceId , parent, false);
            holder = new UserListAdapter.ViewHolder();
            holder.imgUser = convertView.findViewById(R.id.imgUser);
            holder.tvUserName = convertView.findViewById(R.id.tvUserName);
            holder.tvUserEmail = convertView.findViewById(R.id.tvUserEmail);

            convertView.setTag(holder);
        }else{
            holder = (UserListAdapter.ViewHolder) convertView.getTag();
        }

        UserSettings userSettings = getItem(position);

        holder.tvUserName.setText(userSettings.getUser().getUsername());
        holder.tvUserEmail.setText(userSettings.getUser().getEmail());

        UniversalImageLoader.setImage(userSettings.getSetting().getProfile_photo() , holder.imgUser , null , "");

        return convertView;
    }
}
