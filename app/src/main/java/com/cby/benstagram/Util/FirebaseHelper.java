package com.cby.benstagram.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cby.benstagram.R;
import com.cby.benstagram.models.User;
import com.cby.benstagram.models.UserAccountSettings;
import com.cby.benstagram.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private String mUserId;
    private double mPhotoUploadProgress;

    public FirebaseHelper(Context mContext) {
        this.mContext = mContext;

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        if (mAuth.getCurrentUser() != null){
            mUserId = mAuth.getCurrentUser().getUid();
        }
    }

    public boolean checkIfUsernameExists(String username , DataSnapshot dataSnapshot){
        Log.d(TAG, "checkIfUsernameExists: checking " + username + "already exists");

        User user = new User();

        for (DataSnapshot ds : dataSnapshot.child(mUserId).getChildren()){
            Log.d(TAG, "checkIfUsernameExists: dataSnapshot : " + ds);

            user.setUsername(ds.getValue(User.class).getUsername());
            Log.d(TAG, "checkIfUsernameExists: username : " + user.getUsername());

            if (StringManipulation.expandUsername(user.getUsername()).equals(username)){
                Log.d(TAG, "checkIfUsernameExists: found a match " + user.getUsername());
                return true;
            }
        }

        return false;
    }

    public void registerNewEmail(final String email , String userName, String password, final ProgressBar progressBar){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            mUserId = mAuth.getCurrentUser().getUid();

                            sendVerificationEmail();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(mContext , "Success to send verification email" , Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(mContext , "Fail to send verification email" , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void addNewUser(String email , String username, String description , String website , String profile_photo){

        User user = new User(mUserId , "010-1111-2222" , email , StringManipulation.condenseUsername(username));

        mDatabaseReference.child(mContext.getString(R.string.dbname_users))
                .child(mUserId)
                .setValue(user);

        UserAccountSettings setting = new UserAccountSettings(
                description,
                username,
                0,
                0,
                0,
                profile_photo,
                StringManipulation.condenseUsername(username),
                website
        );

        mDatabaseReference.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(mUserId)
                .setValue(setting);
    }
    
    public UserSettings getUserSettings(DataSnapshot dataSnapshot){
        Log.d(TAG, "getUserAccountSettings: retrieving user account setting form database");

        User user = new User();
        UserAccountSettings setting = new UserAccountSettings();

        for (DataSnapshot ds : dataSnapshot.getChildren()){
            if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))){

                Log.d(TAG, "getUserSettings: dataSnapshot : " + ds);

                try {
                    setting.setDescription(
                            ds.child(mUserId)
                                .getValue(UserAccountSettings.class)
                                .getDescription()
                    );
                    setting.setDisplay_name(
                            ds.child(mUserId)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                    );
                    setting.setFollowers(
                            ds.child(mUserId)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                    );
                    setting.setFollowing(
                            ds.child(mUserId)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );
                    setting.setPosts(
                            ds.child(mUserId)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );
                    setting.setProfile_photo(
                            ds.child(mUserId)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );
                    setting.setUsername(
                            ds.child(mUserId)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );
                    setting.setWebsite(
                            ds.child(mUserId)
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()
                    );

                    Log.d(TAG, "getUserSettings: retrieved setting :" + setting.toString());

                }catch (NullPointerException e){
                    Log.e(TAG, "getUserSettings: NullPointerException ", e);
                }
            }


            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserSettings: dataSnapshot : " + ds);

                try {
                    user.setUser_id(ds.child(mUserId)
                            .getValue(User.class)
                            .getUser_id()
                    );
                    user.setPhone_number(ds.child(mUserId)
                            .getValue(User.class)
                            .getPhone_number()
                    );
                    user.setEmail(ds.child(mUserId)
                            .getValue(User.class)
                            .getEmail()
                    );
                    user.setUsername(ds.child(mUserId)
                            .getValue(User.class)
                            .getUsername()
                    );

                    Log.d(TAG, "getUserSettings: retrieved user :" + user.toString());
                }catch (NullPointerException e){
                    Log.e(TAG, "getUserSettings: ", e);
                }
            }
        }

        return new UserSettings(user , setting);
    }

    public void updateUsername(String userName) {
        Log.d(TAG, "updateUsername: new User Name : " + userName);

        mDatabaseReference.child(mContext.getString(R.string.dbname_users))
                .child(mUserId)
                .child(mContext.getString(R.string.field_username))
                .setValue(userName);

        mDatabaseReference.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(mUserId)
                .child(mContext.getString(R.string.field_username))
                .setValue(userName);
    }

    public void updateEmail(String email) {
        Log.d(TAG, "updateEmail: email : " + email);

        mDatabaseReference.child(mContext.getString(R.string.dbname_users))
                .child(mUserId)
                .child(mContext.getString(R.string.field_email))
                .setValue(email);

    }

    public void updateUserAccountSettings(String displayName, String website, String description, String phoneNumber) {

        Log.d(TAG, "updateUserAccountSettings: ");

        if (displayName != null) updateUserAccountSetting(mContext.getString(R.string.field_display_name) , displayName);
        if (website != null) updateUserAccountSetting(mContext.getString(R.string.field_website) , website);
        if (description != null) updateUserAccountSetting(mContext.getString(R.string.field_description) , description);
    }

    private void updateUserAccountSetting(String fieldName, String value) {

        Log.d(TAG, "updateUserAccountSetting: " + String.format("%s : %s" , fieldName , value));

        mDatabaseReference.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(mUserId)
                .child(fieldName)
                .setValue(value);
    }

    public int getImageCount(DataSnapshot dataSnapshot) {

        int imageCount = 0;

        // 현재 사용자의 모든 이미지 노드들을 반복하면서 카운트를 구한다.
        for (DataSnapshot ds : dataSnapshot
                .child(mContext.getString(R.string.dbname_user_photos))
                .child(mUserId)
                .getChildren()){
            imageCount++;
        }

        return imageCount;
    }

    public void uploadNewPhoto(String photoType , String description , int imageCount, String imageUrl) {
        Log.d(TAG, "uploadNewPhoto: " + String.format("%s , %s , %d , %s" , photoType , description , imageCount , imageUrl.toString()));

        FilePaths filePaths = new FilePaths();

        if (photoType.equals(mContext.getString(R.string.new_photo))){
            Log.d(TAG, "uploadNewPhoto: new_photo");

            // 저장할 Firebase 경로 및 파일명 지정
            // photos/users/userid/photo1
            // photos/users/userid/photo2
            StorageReference storageReference = mStorageReference
                    .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + mUserId + "/photo" + (imageCount + 1));

            // 이미지를 Bitmap으로 변환한다.
            Bitmap bitmap = ImageManager.getBitmap(imageUrl);
            byte[] bytes = ImageManager.getBytesFromBitmap(bitmap, 100);

            UploadTask uploadTask = storageReference.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(mContext, "photo upload success", Toast.LENGTH_SHORT).show();

                    //Uri firebaseUrl = taskSnapshot.getDownloadUrl();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, "photo upload failed", Toast.LENGTH_SHORT).show();
                }

            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    if (progress - 15 > mPhotoUploadProgress){
                        Toast.makeText(mContext, "photo upload progress : " + String.format("%.0f" , progress), Toast.LENGTH_SHORT).show();
                        mPhotoUploadProgress = progress;
                    }
                }
            });

        }else if (photoType.equals(mContext.getString(R.string.profile_photo))){
            Log.d(TAG, "uploadNewPhoto: profile_photo");


        }

    }
}
