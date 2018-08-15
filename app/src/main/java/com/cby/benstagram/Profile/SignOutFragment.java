package com.cby.benstagram.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cby.benstagram.Home.HomeActivity;
import com.cby.benstagram.Login.LoginActivity;
import com.cby.benstagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SignOutFragment extends Fragment
        implements View.OnClickListener, FirebaseAuth.AuthStateListener{

    private static final String TAG = "SignOutFragment";

    private FirebaseAuth mAuth;

    private ProgressBar mProgressSignOut;
    private Button mBtnSingOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container , false);

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        mProgressSignOut = view.findViewById(R.id.progress_signOut);
        mProgressSignOut.setVisibility(View.GONE);

        mBtnSingOut = view.findViewById(R.id.btn_signOut);

        mBtnSingOut.setOnClickListener(this);

        Log.d(TAG, "onCreateView: SignOutFragment created");

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_signOut){
            runSignOut();
        }
    }

    private void runSignOut() {
        Log.d(TAG, "runSignOut: start sing out");

        mProgressSignOut.setVisibility(View.VISIBLE);

        mAuth.signOut();

        if (getActivity() != null) getActivity().finish();

        mProgressSignOut.setVisibility(View.GONE);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null){
            Log.d(TAG, "onAuthStateChanged: signed_in : " + user.getUid());

        }else{
            Log.d(TAG, "onAuthStateChanged: signed_out");

            Log.d(TAG, "onAuthStateChanged: navigate to login Activity");

            Intent intent = new Intent(getActivity() , LoginActivity.class);
            // Login Activity로 이동 후 뒤로가기 클릭시 HomeActivity가 다시 나오는 문제 해결
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
