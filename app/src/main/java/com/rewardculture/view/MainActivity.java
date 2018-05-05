package com.rewardculture.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.rewardculture.BuildConfig;
import com.rewardculture.R;
import com.rewardculture.database.FirebaseDatabaseHelper;
import com.rewardculture.misc.Constants;
import com.rewardculture.misc.Utils;
import com.rewardculture.model.CategorySnippet;
import com.rewardculture.model.User;
import com.rewardculture.ost.OstEconomy;
import com.rewardculture.ost.TokenEconomy;

import org.json.JSONException;
import org.json.JSONObject;

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
    FirebaseUser user;
    TokenEconomy economy;

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

        economy = new OstEconomy();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
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
            generateOstId(user);

            //IdpResponse response = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);
            Utils.showToastAndLog(this, "signed in user: " + user.getDisplayName(), TAG);
            Query query = dbHelper.getBookCategories();
            listView.setAdapter(createListAdapter(query));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    CategorySnippet category = (CategorySnippet) listView.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, BooksActivity.class);
                    intent.putExtra(Constants.INTENT_CATEGORY, category.id);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * Check if user has an associated ost id. if not then generate id.
     *
     * @param user
     */
    private void generateOstId(final FirebaseUser user) {
        final DatabaseReference userRef = dbHelper.getUser(user.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // u will be null if this user does not exist in Firebase
                User u = (dataSnapshot.getValue(User.class));
                // If user does not exist or does not have an associated ost id, create the user
                if (u == null || u.getOstId() == null) {
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                JSONObject result = economy.parseUserResponse(
                                        economy.createUser(user.getUid()));
                                userRef.setValue(new User(user.getUid(), result.getString("uuid")));
                                //userRef.setValue(new User(user.getUid(), "fake-uuid"));
                                Log.d(TAG, "Ost id generated for user " + user.getDisplayName());
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage(), e);
                            } catch (JSONException e) {
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
        if (auth != null) {
            auth.signOut();
            Log.d(TAG, "signing out user");
        }
    }
}
