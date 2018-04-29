package com.rewardculture.view;

import android.database.DataSetObserver;
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
import com.rewardculture.ost.OstEconomy;
import com.rewardculture.ost.TokenEconomy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookActivity extends AppCompatActivity {

    private static final String TAG = "BookActivity";

    FirebaseDatabaseHelper dbHelper;
    TokenEconomy economy;

    @BindView(R.id.book_title)
    TextView bookTitle;

    @BindView(R.id.reviews)
    ListView listView;

    @BindView(R.id.txt_review)
    EditText inputReview;

    @BindView(R.id.btn_review)
    Button btn;

    Book book;
    DatabaseReference bookRef;
    FirebaseListAdapter<Review> reviewsAdapter;
    int lastPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);
        economy = new OstEconomy();

        String bookId = getIntent().getStringExtra(Constants.INTENT_BOOK);
        dbHelper = FirebaseDatabaseHelper.getInstance();
        bookRef = dbHelper.getBook(bookId);
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.getValue().toString());
                book = dataSnapshot.getValue(Book.class);
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
            // TODO use logged in user
            String userId = "unknown";
            Review review = new Review(txtReview, new PostedBySnippet(userId, "Trung Nguyen"));
            // write review to database and execute ost transaction
            dbHelper.addReview(bookRef, review, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Utils.showToastAndLog(BookActivity.this,
                            "review pushed" + databaseReference.getKey(), TAG);
                    String response = economy.executeReviewTransaction(dbHelper.getTestUuid());
                    Log.d(TAG, "review transaction response: " + response);
                }
            });
            // reset state of input review
            inputReview.getText().clear();
        }
    }

    void updateUI() {
        bookTitle.setText(book.getTitle());
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

    class ReviewsListAdapter extends FirebaseListAdapter<Review> {

        public ReviewsListAdapter(@NonNull FirebaseListOptions<Review> options) {
            super(options);
        }

        @Override
        protected void populateView(View v, Review model, final int position) {
            // TODO use logged in user
            String posterName = model.getPosterName();
            if (posterName == null) {
                posterName = "Anonymous user";
            }
            ((TextView) v.findViewById(R.id.cv_posted_by)).setText(posterName);
            ((TextView) v.findViewById(R.id.cv_review_text)).setText(model.getText());
            ((TextView) v.findViewById(R.id.txt_likes)).setText(
                    String.valueOf(model.getNumberOfLikes()));
            LikeButton btn = v.findViewById(R.id.btn_like);
            btn.setLiked(model.likedByUser(dbHelper.getTestUuid()));
            btn.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    String response = economy.executeLikeTransaction(dbHelper.getTestUuid());
                    Log.d(TAG, "like transaction response" + response);
                    dbHelper.likeReview(dbHelper.getTestUuid(), reviewsAdapter.getRef(position));
                    lastPosition = position;
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    dbHelper.unlikeReview(dbHelper.getTestUuid(), reviewsAdapter.getRef(position));
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

