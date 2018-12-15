package com.cby.benstagram.Login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cby.benstagram.Home.HomeActivity;
import com.cby.benstagram.R;
import com.cby.benstagram.Util.FirebaseHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";

    private Context mContext  = LoginActivity.this;
    private Button mBtnLogin , mBtnLoginGoogle;
    private TextView mTxtSignUp;
    private EditText mInputEmail, mInputPass;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseHelper mFirebaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseHelper = new FirebaseHelper(mContext);

        setupActivityWidgets();
        setupWidgetEvents();

        mProgressBar.setVisibility(View.GONE);

        if (mAuth.getCurrentUser() != null){
            Intent intent = new Intent(mContext , HomeActivity.class);
            startActivity(intent);
            finish();
        }

        Log.d(TAG, "onCreate: Login Activity Created");
    }

    private void setupActivityWidgets() {
        mProgressBar = findViewById(R.id.progress_Login);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnLoginGoogle = findViewById(R.id.btn_login_google);
        mTxtSignUp = findViewById(R.id.txt_sign_up);
        mInputEmail = findViewById(R.id.input_email);
        mInputPass = findViewById(R.id.input_password);
    }

    private void setupWidgetEvents() {
        if (mBtnLogin != null) mBtnLogin.setOnClickListener(this);
        if (mTxtSignUp != null) mTxtSignUp.setOnClickListener(this);
        if (mBtnLoginGoogle != null) mBtnLoginGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.btn_login){

            String email = mInputEmail.getText().toString();
            String password = mInputPass.getText().toString();

            if (isStringNull(email) || isStringNull(password)){
                Toast.makeText(mContext, "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mProgressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            Log.d(TAG, "onComplete: signIn Result : " + task.isSuccessful());

                            mProgressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");

                                FirebaseUser user = mAuth.getCurrentUser();

                                try {
                                    if (user.isEmailVerified()){
                                        Log.d(TAG, "onComplete: success email verification");

                                        Intent intent = new Intent(mContext , HomeActivity.class);
                                        startActivity(intent);

                                    }else{
                                        Log.e(TAG, "onComplete: fail email verification");

                                        Toast.makeText(
                                                mContext ,
                                                "Email is not verified. Check you emal." ,
                                                Toast.LENGTH_SHORT).show();

                                        mAuth.signOut();
                                    }
                                    
                                }catch (NullPointerException e){
                                    Log.e(TAG, "onComplete: NullPointerException : " + e.getMessage());
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(mContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }else if (viewId == R.id.btn_login_google) {

            startGoogleLogin();

        }else if (viewId == R.id.txt_sign_up){
            Intent intent = new Intent(mContext , RegisterActivity.class);
            startActivity(intent);
        }
    }

    private int RC_SIGN_IN = 100;

    private void startGoogleLogin() {
        Log.d(TAG, "loginByGoogle: start");

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this , gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        String email = account.getEmail();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

        mProgressBar.setVisibility(View.VISIBLE);

        Log.d(TAG, "loginByGoogle: end");
    }

    private boolean isStringNull(String string) {
        return string.equals("");         
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(mContext , "Google sign in failed" , Toast.LENGTH_SHORT).show();
                // ...
            }

            //handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Log.d(TAG, "handleSignInResult: " + account.toString());
            Toast.makeText(mContext , "Success Google Login" , Toast.LENGTH_SHORT).show();

            firebaseAuthWithGoogle(account);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Log.w(TAG, "signInResult:failed message=" + e.getMessage());

            //updateUI(null);
            Toast.makeText(mContext , "Fail to Google Login" , Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount googleAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + googleAccount.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(googleAccount.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userEmail = user.getEmail();

                            mFirebaseHelper.addNewUser(
                                    user.getEmail() ,
                                    user.getDisplayName(),
                                    "aaaaaa" , "www.google.com" , "");

                            mProgressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(mContext , HomeActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(mContext , "Authentication Failed" , Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
