package com.rewardculture.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.rewardculture.R;
import com.rewardculture.database.FirebaseDatabaseHelper;
import com.rewardculture.misc.Constants;
import com.rewardculture.misc.Utils;
import com.rewardculture.model.Book;
import com.rewardculture.model.PostedBySnippet;
import com.rewardculture.model.Review;
import com.rewardculture.model.Transaction;
import com.rewardculture.model.User;
import com.rewardculture.ost.OstEconomy;
import com.rewardculture.ost.TokenEconomy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// TODO record transactions in database
public class BookActivity extends AppCompatActivity {

    private static final String TAG = "BookActivity";

    FirebaseDatabaseHelper dbHelper;
    TokenEconomy economy;
    Book book;
    DatabaseReference bookRef;
    FirebaseListAdapter<Review> reviewsAdapter;
    FirebaseUser firebaseUser;
    User user;
    int lastPosition = -1;

    @BindView(R.id.book_title)
    TextView bookTitle;

    @BindView(R.id.book_author_year)
    TextView bookAuthorYear;

    @BindView(R.id.reviews)
    ListView listView;

    @BindView(R.id.txt_review)
    EditText inputReview;

    @BindView(R.id.btn_review)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String bookId = intent.getStringExtra(Constants.INTENT_BOOK);
        user = (User) intent.getSerializableExtra(Constants.INTENT_USER);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        economy = new OstEconomy();
        dbHelper = FirebaseDatabaseHelper.getInstance();
        bookRef = dbHelper.getBook(bookId);
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                book = dataSnapshot.getValue(Book.class);
                Log.d(TAG, dataSnapshot.getValue().toString());
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btn_review)
    public void onSendReviewClick(View v) {
        String txtReview = inputReview.getText().toString();
        if (!txtReview.isEmpty()) {
            Review review = new Review(txtReview, new PostedBySnippet(firebaseUser.getUid(),
                    firebaseUser.getDisplayName()));
            // write review to database and execute ost transaction
            dbHelper.addReview(bookRef, review, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Utils.showToastAndLog(BookActivity.this,
                            "review pushed" + databaseReference.getKey(), TAG);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String response = economy.executeReviewTransaction(user.getOstId());
                                Log.d(TAG, "review transaction response: " + response);
                                logTransaction(response);
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage(), e);
                            } catch (JSONException e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                        }
                    }).start();

                }
            });
            // reset state of input review
            inputReview.getText().clear();
        }
    }

    void updateUI() {
        bookTitle.setText(book.getTitle());
        bookAuthorYear.setText(String.format("by %s -- published %d",
                book.getAuthor(), book.getYear()));
        reviewsAdapter = createListAdapter(dbHelper.getReviews(bookRef));
        listView.setAdapter(reviewsAdapter);
    }

    private FirebaseListAdapter<Review> createListAdapter(Query query) {
        FirebaseListOptions options = new FirebaseListOptions.Builder<Review>()
                .setQuery(query, Review.class)
                .setLifecycleOwner(this)
                .setLayout(R.layout.cardview_review)
                .build();

        FirebaseListAdapter adapter = new ReviewsListAdapter(options);
        return adapter;
    }

    void onLikeClick(int position, final Review model) {
        dbHelper.likeReview(firebaseUser.getUid(), reviewsAdapter.getRef(position));
        lastPosition = position;

        // posts by anonymous users
        if (model.getPostedBy() == null) return;

        // reward is given to the author of the review so need to retrieve his ost id from database
        final DatabaseReference reference = dbHelper.getUser(model.getPostedBy().getId());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User reviewer = dataSnapshot.getValue(User.class);
                if (reviewer == null) return;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String response = economy.executeLikeTransaction(reviewer.getOstId());
                            Log.d(TAG, "like transaction response: " + response);
                            logTransaction(response);
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage(), e);
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }).start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void logTransaction(String transactionResponse) throws JSONException {
        Transaction t = Transaction.fromJsonObject(
                economy.parseTransactionResponse(transactionResponse));
        t.setTransactionTime(System.currentTimeMillis());
        dbHelper.logTransaction(t);
    }

    class ReviewsListAdapter extends FirebaseListAdapter<Review> {

        public ReviewsListAdapter(@NonNull FirebaseListOptions<Review> options) {
            super(options);
        }

        @Override
        protected void populateView(View v, final Review model, final int position) {
            String posterName = model.getPosterName();
            if (posterName == null) {
                posterName = "Anonymous user";
            }
            ((TextView) v.findViewById(R.id.cv_posted_by)).setText(posterName);
            ((TextView) v.findViewById(R.id.cv_review_text)).setText(model.getText());
            ((TextView) v.findViewById(R.id.txt_likes)).setText(
                    String.valueOf(model.getNumberOfLikes()));
            LikeButton btn = v.findViewById(R.id.btn_like);
            btn.setLiked(model.likedByUser(firebaseUser.getUid()));
            btn.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    onLikeClick(position, model);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    dbHelper.unlikeReview(firebaseUser.getUid(), reviewsAdapter.getRef(position));
                    lastPosition = position;
                }
            });
        }

        @Override
        public void onDataChanged() {
            super.onDataChanged();
            if (lastPosition == -1) {
                listView.smoothScrollToPosition(reviewsAdapter.getCount());
            } else {
                Log.d(TAG, "last position = " + lastPosition);
                listView.smoothScrollToPosition(lastPosition);
            }
        }

    }
}

