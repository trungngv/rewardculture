package com.rewardculture.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rewardculture.R;
import com.rewardculture.database.FirebaseDatabaseHelper;
import com.rewardculture.misc.Constants;
import com.rewardculture.misc.Utils;
import com.rewardculture.model.Book;
import com.rewardculture.model.BookSnippet;
import com.rewardculture.model.Review;
import com.rewardculture.database.Database;
import com.rewardculture.database.LocalDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookActivity extends AppCompatActivity {

    private static final String TAG = "BookActivity";

    LocalDatabase database = LocalDatabase.getInstance();
    FirebaseDatabaseHelper dbHelper;

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
    DatabaseReference reviewsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);

        String bookId = getIntent().getStringExtra(Constants.INTENT_BOOK);
        dbHelper = FirebaseDatabaseHelper.getInstance();
        bookRef = dbHelper.getBook(bookId);
        reviewsRef = bookRef.child("reviews");
        bookRef.addValueEventListener(new ValueEventListener() {
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

        // set up write review functionality
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtReview = inputReview.getText().toString();
                if (!txtReview.isEmpty()) {
                    // TODO use logged in user
                    String userId = database.getUsers().get(0).getUserId();
                    Review review = new Review(txtReview, userId, 0, 0);
                    reviewsRef.push().setValue(review, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            Utils.showToastAndLog(BookActivity.this,
                                    "review pushed" + databaseReference.getKey(), TAG);
                        }
                    });
                    inputReview.getText().clear();
                }
            }
        });
    }

    void updateUI() {
        bookTitle.setText(book.getTitle());
        listView.setAdapter(createListAdapter(reviewsRef));
    }

    private ListAdapter createListAdapter(Query query) {
        FirebaseListOptions options = new FirebaseListOptions.Builder<Review>()
                .setQuery(query, Review.class)
                .setLifecycleOwner(this)
                .setLayout(R.layout.cardview_review)
                .build();

        FirebaseListAdapter adapter = new FirebaseListAdapter<Review>(options) {
            @Override
            protected void populateView(View v, Review model, int position) {
                ((TextView) v.findViewById(R.id.cv_review_text)).setText(model.getText());
            }
        };

        return adapter;
    }

}
