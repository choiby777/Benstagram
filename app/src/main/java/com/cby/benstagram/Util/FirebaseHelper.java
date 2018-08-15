package com.cby.benstagram.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.cby.benstagram.R;
import com.cby.benstagram.models.User;
import com.cby.benstagram.models.UserAccountSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private String mUserId;

    public FirebaseHelper(Context mContext) {
        this.mContext = mContext;

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

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

    public void registerNewEmail(final String email , String userName, String password){

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
                    }
                });
    }

    public void sendVerificationEmail(){
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
                username,
                website
        );

        mDatabaseReference.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(mUserId)
                .setValue(setting);
    }
}
