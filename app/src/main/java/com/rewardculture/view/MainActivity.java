package com.rewardculture.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.rewardculture.BuildConfig;
import com.rewardculture.R;
import com.rewardculture.database.FirebaseDatabaseHelper;
import com.rewardculture.misc.Utils;
import com.rewardculture.model.User;
import com.rewardculture.ost.RewardCultureEconomy;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 100;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    FirebaseAuth auth;
    User user;

    FirebaseDatabaseHelper dbHelper = FirebaseDatabaseHelper.getInstance();
    RewardCultureEconomy economy = new RewardCultureEconomy();

    MainCategoriesFragment categoriesFragment;
    WalletFragment walletFragment;

    public static Intent createIntent(Context context, IdpResponse response) {
        return new Intent().setClass(context, MainActivity.class)
                .putExtra(ExtraConstants.IDP_RESPONSE, response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Set up tool bar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // set up navigation drawer view
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setAvailableProviders(getSelectedProviders())
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .build(),
                    RC_SIGN_IN);
        } else {
            Utils.showToastAndLog(this, "signed in user: " + firebaseUser.getDisplayName(), TAG);
            generateOstId(firebaseUser);
            //IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);


            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            categoriesFragment = MainCategoriesFragment.newInstance();
            walletFragment = WalletFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, categoriesFragment, "products").commit();

        }
    }

    private void logOut() {
        if (auth != null) {
            auth.signOut();
            Log.d(TAG, "signed out");
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setAvailableProviders(getSelectedProviders())
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .build(),
                    RC_SIGN_IN);
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
                // because user may be null when these were created
                walletFragment.updateUser(user);
                categoriesFragment.updateUser(user);
                // If user does not exist or does not have an associated ost id, create the user
                if (user == null || user.getOstId() == null) {
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                JsonObject result = economy.parseUserResponse(
                                        economy.createUser(firebaseUser.getUid()));
                                user = new User(firebaseUser.getUid(), result.get("uuid").getAsString());
                                walletFragment.updateUser(user);
                                categoriesFragment.updateUser(user);
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

    /**
     * Selected providers for authentication.
     *
     * @return
     */
    private List<AuthUI.IdpConfig> getSelectedProviders() {
        return Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build());
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(findViewById(R.id.content_frame), errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_logout:
                logOut();
                break;
            case R.id.nav_wallet:
            case R.id.nav_products:
                showFragment(item.getItemId());
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return true;

    }

    /**
     * Switching between fragments.
     * @param fragmentId
     */
    private void showFragment(int fragmentId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (fragmentId) {
            case R.id.nav_products:
                transaction.replace(R.id.fragment_container, categoriesFragment, "products");
                break;
            case R.id.nav_wallet:
                transaction.replace(R.id.fragment_container, walletFragment, "wallet");
                break;
            default:
                break;
        }
        // and add the transaction to the back stack so the user can navigate back
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
