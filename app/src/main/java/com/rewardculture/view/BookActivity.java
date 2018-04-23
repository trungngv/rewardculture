package com.rewardculture.view;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rewardculture.R;
import com.rewardculture.model.Book;
import com.rewardculture.model.Review;
import com.rewardculture.model.database.Database;
import com.rewardculture.model.database.LocalDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {
    public static final String ARG_BOOK = "book";
    Database database = LocalDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Book book = (Book) getIntent().getSerializableExtra(ARG_BOOK);
        setContentView(R.layout.activity_book);

        // set up book title
        TextView bookTitle = findViewById(R.id.book_title);
        bookTitle.setText(book.getTitle());

        // set up reviews
        final ListView listView = findViewById(R.id.reviews);
        final ArrayAdapter adapter = new ReviewsAdapter(this, book.getReviews());
        // TODO what to here to make the list refresh
//        adapter.registerDataSetObserver(new DataSetObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
//            }
//        });
        listView.setAdapter(adapter);

        // set up write review functionality
        final EditText inputReview = findViewById(R.id.txt_review);
        final Button btn = findViewById(R.id.btn_review);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtReview = inputReview.getText().toString();
                if (!txtReview.isEmpty()) {
                    // TODO use current user
                    String userId = database.getUsers().get(0).getUserId();
                    Review review = new Review(txtReview, userId, 0, 0);
                    adapter.add(review);
                    adapter.notifyDataSetChanged();
                    // TODO update database
                    // database.addReview(book, review);
                    inputReview.getText().clear();
                }
            }
        });
    }

    public class ReviewsAdapter extends ArrayAdapter<Review> {

        public ReviewsAdapter(Context c, List<Review> reviews) {
            super(c, R.layout.cardview_review, reviews);
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.cardview_review, parent, false);
            }
            Review item = getItem(position);
            ((TextView) convertView.findViewById(R.id.cv_review_text)).setText(item.getText());
            return convertView;
        }

    }
}
