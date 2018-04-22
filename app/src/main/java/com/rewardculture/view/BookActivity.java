package com.rewardculture.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rewardculture.R;
import com.rewardculture.model.Book;
import com.rewardculture.model.Review;
import com.rewardculture.model.database.Database;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {
	public static final String ARG_BOOK = "book";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Book book = (Book) getIntent().getSerializableExtra(ARG_BOOK);
		setContentView(R.layout.activity_book);

		// set up book title
		TextView bookTitle = findViewById(R.id.book_title);
		bookTitle.setText(book.getTitle());

        // set up reviews
		final ListView listView = findViewById(R.id.reviews);
		listView.setAdapter(new ReviewsAdapter(this, book.getReviews()));
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
