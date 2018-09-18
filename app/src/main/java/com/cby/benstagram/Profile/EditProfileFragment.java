package com.cby.benstagram.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cby.benstagram.Dialogs.ConfirmPasswordDialog;
import com.cby.benstagram.R;
import com.cby.benstagram.EventListeners.UploadTaskEventListener;
import com.cby.benstagram.Share.ShareActivity;
import com.cby.benstagram.Util.FirebaseHelper;
import com.cby.benstagram.Util.ImageManager;
import com.cby.benstagram.Util.UniversalImageLoader;
import com.cby.benstagram.models.User;
import com.cby.benstagram.models.UserAccountSettings;
import com.cby.benstagram.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfileFragment extends Fragment
        implements FirebaseAuth.AuthStateListener, View.OnClickListener, ConfirmPasswordDialog.OnConfirmPasswordListener {
    private static final String TAG = "EditProfileFragment";

    private ImageView mProfileImage;
    private EditText mEditUserName , mEditDisplayName, mEditWebsite,
            mEditDescription, mEditEmail, mEditPhoneNumber;
    private TextView txtChangePhoto;
    private ProgressBar progressLoading;
    private String mUserId;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;
    private FirebaseHelper mFirebaseHelper;
    private UserSettings mUserSettings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile , container , false);

        mProfileImage = view.findViewById(R.id.profile_image);

        mEditUserName = view.findViewById(R.id.editText_userName);
        mEditDisplayName = view.findViewById(R.id.editText_displayName);
        mEditWebsite = view.findViewById(R.id.editText_website);
        mEditDescription = view.findViewById(R.id.editText_description);
        mEditEmail = view.findViewById(R.id.editText_email);
        mEditPhoneNumber = view.findViewById(R.id.editText_phoneNumber);
        txtChangePhoto = view.findViewById(R.id.txtChangePhoto);
        progressLoading = view.findViewById(R.id.progressLoading);
        progressLoading.setVisibility(View.INVISIBLE);

        ImageView imageChecked = view.findViewById(R.id.image_checked);
        ImageView backArrowImg = view.findViewById(R.id.backArrow);

        imageChecked.setOnClickListener(this);
        backArrowImg.setOnClickListener(this);
        txtChangePhoto.setOnClickListener(this);

        setProfileImage();
        setupFirebaseAuth();
        getIncomingIntent();

        return view;
    }

    private void getIncomingIntent() {
        Intent intent  = getActivity().getIntent();

        String stringExtra = intent.getStringExtra(getString(R.string.return_to_fragment));

        // 사진 선택 또는 촬영후 실행되는 경우
        if (intent.hasExtra(getString(R.string.selected_image)) ||
            intent.hasExtra(getString(R.string.selected_bitmap))) {

            if (stringExtra.equals(getString(R.string.edit_profile_fragment))){

                Bitmap bitmap = null;

                // 사진첩에서 선택한 경우 선택한 이미지 경로로부터 이미지를 가져온다.
                if (intent.hasExtra(getString(R.string.selected_image))) {
                    Log.d(TAG, "getIncomingIntent: received change profile Photo");

                    String selectedImageUrl = intent.getStringExtra(getString(R.string.selected_image));

                    bitmap = ImageManager.getBitmap(selectedImageUrl);
                }
                // 카메라를 실행하여 새로 사진을 촬영한 경우
                else if (intent.hasExtra(getString(R.string.selected_bitmap))){

                    // ShareActivity에서 전달받은 이미지를 가져온다
                    bitmap = intent.getParcelableExtra(getString(R.string.selected_bitmap));
                }

                if (bitmap != null){
                    // 사진을 업로드를 시작 한다.
                    RunUploadProfileImage(bitmap);
                }
            }
        }
    }

    public void RunUploadProfileImage(Bitmap bitmap){

        progressLoading.setVisibility(View.VISIBLE);

        // 업로드 상태 이벤트를 등록하고
        mFirebaseHelper.setOnUploadTaskEvent(new UploadTaskEventListener() {
            @Override
            public void onSuccessEvent() {
                progressLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailureEvent() {
                progressLoading.setVisibility(View.GONE);
            }

            @Override
            public void onProgressEvent(double progress) {

            }
        });

        // 서버에 사진파일을 업로드한다.
        mFirebaseHelper.uploadPhotoByBitmap(getString(R.string.profile_photo) , null, 0, bitmap);
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: ");

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        mUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDbReference = mFirebaseDatabase.getReference();
        mFirebaseHelper = new FirebaseHelper(getActivity());

        mDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // retrieve user information from the database
                UserSettings userSettings = mFirebaseHelper.getUserSettings(dataSnapshot);
                setProfileWidgets(userSettings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveProfileSettings(){
        String displayName = mEditDisplayName.getText().toString();
        String userName = mEditUserName.getText().toString();
        String website = mEditWebsite.getText().toString();
        String description = mEditDescription.getText().toString();
        String email = mEditEmail.getText().toString();
        String phoneNumber = mEditPhoneNumber.getText().toString();

//        mDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // case 1 : 사용자 이름이 수정된 경우
                if (!mUserSettings.getUser().getUsername().equals(userName)){
                    checkIfUsernameExists(userName);
                }

                // case 2 : email이 변경된 경우
                if (!mUserSettings.getUser().getEmail().equals(email)) {

                    // step 1 : 현재 이메일 계정의 비밀번호 확인
                    ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
                    dialog.show(getFragmentManager() , getString(R.string.confirm_password_dialog));
                    dialog.setTargetFragment(EditProfileFragment.this, 1);

                    // step 2 : 수정입력한 email이 이미 등록되 있는지 체크
                    // step 3 : auth와 database에 새로운 email 로 업데이트
                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        if (mUserSettings.getSetting().getDisplay_name().equals(displayName)) displayName = null;
        if (mUserSettings.getSetting().getWebsite().equals(website)) website = null;
        if (mUserSettings.getSetting().getDescription().equals(description)) description = null;

        mFirebaseHelper.updateUserAccountSettings(displayName , website , description , phoneNumber);

    }

    private void checkIfUsernameExists(final String userName) {
        Log.d(TAG, "checkIfUsernameExists: userName : " + userName);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(userName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()){
                    mFirebaseHelper.updateUsername(userName);
                    Toast.makeText(getActivity(), "saved username.", Toast.LENGTH_SHORT).show();
                }

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Log.d(TAG, "onDataChange: found a match : " + singleSnapshot.getValue(User.class).getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setProfileWidgets(UserSettings userSettings){

        mUserSettings = userSettings;
        User user = userSettings.getUser();
        UserAccountSettings accountSettings = userSettings.getSetting();

        UniversalImageLoader.setImage(accountSettings.getProfile_photo() , mProfileImage , null , "");

        mEditUserName.setText(user.getUsername());
        mEditEmail.setText(user.getEmail());
        mEditPhoneNumber.setText(user.getPhone_number());

        mEditDisplayName.setText(accountSettings.getDisplay_name());
        mEditWebsite.setText(accountSettings.getWebsite());
        mEditDescription.setText(accountSettings.getDescription());
    }


    private void setProfileImage() {
        Log.d(TAG, "setProfileImage: setting Profile Image");

        String imgURL = "tr2.cbsistatic.com/hub/i/r/2017/01/31/7e355c52-c68f-4389-825f-392f2dd2ac19/resize/770x/d19d6c021f770122da649e2a77bd1404/androiddatahero.jpg";

        UniversalImageLoader.setImage(imgURL , mProfileImage , null, "https://");
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.image_checked){

            saveProfileSettings();

        }else if (viewId == R.id.backArrow){

            getActivity().finish();

        }else if (viewId == R.id.txtChangePhoto){

            Intent intent = new Intent(getActivity() , ShareActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onConfirmPasswordListener(String password) {
        Log.d(TAG, "onConfirmPasswordListener: password : " + password);

        // firebase 문서 참조
        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getCurrentUser().getEmail(), password);

        // Prompt the user to re-provide their sign-in credentials
        mAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "User re-authenticated.");

                            mAuth.fetchProvidersForEmail(mEditEmail.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                                            if (task.isSuccessful()){
                                                try {

                                                    if (task.getResult().getProviders().size() == 1){
                                                        Log.d(TAG, "onComplete: the email is already in use");
                                                        Toast.makeText(getActivity(), "The email is already in use", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Log.d(TAG, "onComplete: the email is not exist");

                                                        mAuth.getCurrentUser().updateEmail(mEditEmail.getText().toString())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Log.d(TAG, "onComplete: task result : " + task.isSuccessful());

                                                                        if (task.isSuccessful()){
                                                                            Toast.makeText(getActivity(), "email updated", Toast.LENGTH_SHORT).show();
                                                                            mFirebaseHelper.updateEmail(mEditEmail.getText().toString());
                                                                        }
                                                                    }
                                                                });
                                                    }

                                                }catch (NullPointerException e){
                                                    Log.e(TAG, "onComplete: NullPointerException : " + e.getMessage());
                                                }
                                            }
                                        }
                                    });
                        }else {
                            Log.d(TAG, "onComplete: User re-authentication failed");
                        }
                    }
                });

    }

}
