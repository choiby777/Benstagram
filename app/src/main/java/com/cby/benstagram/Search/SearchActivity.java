package com.cby.benstagram.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.cby.benstagram.Profile.ProfileActivity;
import com.cby.benstagram.R;
import com.cby.benstagram.Util.BottomNavigationViewHelper;
import com.cby.benstagram.Util.FirebaseHelper;
import com.cby.benstagram.Adapters.UserListAdapter;
import com.cby.benstagram.models.User;
import com.cby.benstagram.models.UserAccountSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    private Context mContext  = SearchActivity.this;
    private static final int ACTIVITY_NUM = 1;

    // widgets
    private EditText edtSearch;
    private ListView lsvUsers;

    // vars
    private List<User> userList;
    private UserListAdapter userListAdapter;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDbReference;
    private FirebaseHelper mFirebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Log.d(TAG, "onCreate: starting");

        setupFirebaseAuth();
        setupWidgets();
        hideSoftKeyboard();
        setupBottomNavigationView();

        userList = new ArrayList<>();

        setupUserListAdapter();
        searchForMatch("");
        iniTextListener();
        //loadAllUsers();
    }

    private void iniTextListener() {
        Log.d(TAG, "iniTextListener: start");

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //String text = edtSearch.getText().toString().toLowerCase(Locale.getDefault());
                String text = edtSearch.getText().toString();
                searchForMatch(text);
            }
        });
    }

    private void searchForMatch(String keyword) {
        Log.d(TAG, "searchForMath: keyword : " + keyword);

        userList.clear();

//        if (keyword.isEmpty()){
//            setupUserListAdapter();
//            return;
//        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(keyword);

        if (keyword.isEmpty()){
            query = reference.child(getString(R.string.dbname_users))
                    .orderByChild(getString(R.string.field_username));
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot singleSanpshot : dataSnapshot.getChildren()){

                        User user = singleSanpshot.getValue(User.class);

                        Log.d(TAG, "onDataChange: found user : " + user.getUsername());

                        userList.add(user);
                    }
                }

                setupUserListAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: start");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDbReference = mFirebaseDatabase.getReference();
        mFirebaseHelper = new FirebaseHelper(this);
    }

    private void loadAllUsers(){

        mDbReference.child(getString(R.string.dbname_users))
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Log.d(TAG, "onChildAdded: s");

                        final User user = dataSnapshot.getValue(User.class);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Query query = reference
                                .child(getString(R.string.dbname_user_account_settings))
                                .child(user.getUser_id());

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                UserAccountSettings userAccountSettings = dataSnapshot.getValue(UserAccountSettings.class);

                                //UserSettings userSettings = new UserSettings(user , userAccountSettings);
                                //userList.add(userSettings);

                                setupUserListAdapter();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void setupUserListAdapter() {
        userListAdapter = new UserListAdapter(this , R.layout.layout_userlist_item, userList);
        lsvUsers.setAdapter(userListAdapter);
        lsvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "onItemClick: clicked Item position : " + position);

                User selectedUser = userList.get(position);

                Log.d(TAG, "onItemClick: selectedUser : " + selectedUser.getUsername());

                Intent intent = new Intent(mContext , ProfileActivity.class);
                intent.putExtra(getString(R.string.calling_activity) , getString(R.string.search_activity));
                intent.putExtra(getString(R.string.selected_user) , selectedUser);
                startActivity(intent);
            }
        });

    }

    private void setupWidgets() {
        edtSearch = findViewById(R.id.edtSearch);
        lsvUsers = findViewById(R.id.lsvUsers);
    }

    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);
        }
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext , this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
