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
import com.cby.benstagram.models.User;
import com.cby.benstagram.models.UserAccountSettings;
import com.cby.benstagram.models.UserSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<User> {

    private static final String TAG = "UserListAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mLayoutResourceId;

    public UserListAdapter(
            @NonNull Context context,
            int resource,
            @NonNull List<User> objects) {
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

        User user = getItem(position);

        holder.tvUserName.setText(user.getUsername());
        holder.tvUserEmail.setText(user.getEmail());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(mContext.getString(R.string.dbname_user_account_settings))
                .child(user.getUser_id());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserAccountSettings settings = dataSnapshot.getValue(UserAccountSettings.class);

                UniversalImageLoader.setImage(settings.getProfile_photo() , holder.imgUser , null , "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return convertView;
    }
}
