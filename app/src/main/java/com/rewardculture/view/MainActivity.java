package com.rewardculture.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.rewardculture.BuildConfig;
import com.rewardculture.R;
import com.rewardculture.database.FirebaseDatabaseHelper;
import com.rewardculture.misc.Constants;
import com.rewardculture.misc.Utils;
import com.rewardculture.model.CategorySnippet;
import com.rewardculture.model.User;
import com.rewardculture.ost.RewardCultureEconomy;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 100;

    FirebaseDatabaseHelper dbHelper = FirebaseDatabaseHelper.getInstance();
    FirebaseAuth auth;
    RewardCultureEconomy economy;
    User user;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.listview)
    ListView listView;

    public static Intent createIntent(Context context, IdpResponse response) {
        return new Intent().setClass(context, MainActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        economy = new RewardCultureEconomy();
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            //startActivity(AuthUiActivity.createIntent(this));
            //finish();
            //return;

            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setAvailableProviders(getSelectedProviders())
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .build(),
                    RC_SIGN_IN);
            //finish();
            //return;
        } else {
            generateOstId(firebaseUser);

            //IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
            Utils.showToastAndLog(this, "signed in user: " + firebaseUser.getDisplayName(), TAG);
            Query query = dbHelper.getBookCategories();
            listView.setAdapter(createListAdapter(query));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    CategorySnippet category = (CategorySnippet) listView.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, BooksActivity.class);
                    intent.putExtra(Constants.INTENT_CATEGORY, category.id);
                    intent.putExtra(Constants.INTENT_USER, user);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // open the drawer when selected
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Check if user has an associated ost id. if not then generate id.
     *
     * @param firebaseUser
     */
    private void generateOstId(final FirebaseUser firebaseUser) {
        DatabaseReference userRef = dbHelper.getUser(firebaseUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Will be null if this user does not exist in Firebase
                user = (dataSnapshot.getValue(User.class));
                // If user does not exist or does not have an associated ost id, create the user
                if (user == null || user.getOstId() == null) {
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                JsonObject result = economy.parseUserResponse(
                                        economy.createUser(firebaseUser.getUid()));
                                user = new User(firebaseUser.getUid(), result.get("uuid").getAsString());
                                dbHelper.updateUser(user);
                                Log.d(TAG, "Ost id generated for " + firebaseUser.getDisplayName());
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage(), e);
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // TODO change to FirebaseRecyclerAdapter for efficiency later on
    private ListAdapter createListAdapter(Query query) {
        // NOTE: setLifecycleOwner must be set for this to start listening to database events
        FirebaseListOptions options = new FirebaseListOptions.Builder<CategorySnippet>()
                .setQuery(query, CategorySnippet.class)
                .setLifecycleOwner(this)
                .setLayout(R.layout.cardview_category)
                .build();

        FirebaseListAdapter adapter = new FirebaseListAdapter<CategorySnippet>(options) {
            @Override
            protected void populateView(View v, CategorySnippet model, int position) {
                ((TextView) v.findViewById(R.id.cv_category_name)).setText(model.getName());
            }
        };

        return adapter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
        }
    }

    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (resultCode == RESULT_OK) {
            Utils.showToastAndLog(this, "signed in successfully " + response.getEmail(), TAG);
            startActivity(createIntent(this, response));
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            showSnackbar(R.string.unknown_error);
            Log.e(TAG, "Sign-in error: ", response.getError());
        }
    }

    private List<AuthUI.IdpConfig> getSelectedProviders() {
        return Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build());
    }


    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(listView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (auth != null) {
//            auth.signOut();
//            Log.d(TAG, "signing out user");
//        }
    }
}
