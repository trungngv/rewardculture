package com.rewardculture.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.Query;
import com.rewardculture.R;
import com.rewardculture.database.FirebaseDatabaseHelper;
import com.rewardculture.misc.Constants;
import com.rewardculture.misc.Utils;
import com.rewardculture.model.BookSnippet;
import com.rewardculture.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BooksActivity extends AppCompatActivity {
    public static final String TAG = "BooksActivity";

	FirebaseDatabaseHelper dbHelper;
    User user;

    @BindView(R.id.listview_books)
    ListView listView;

    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String category = intent.getStringExtra(Constants.INTENT_CATEGORY);
        user = (User) intent.getSerializableExtra(Constants.INTENT_USER);

        dbHelper = FirebaseDatabaseHelper.getInstance();
        Query query = dbHelper.getBooks(category);
		listView.setAdapter(createListAdapter(query));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookSnippet book = (BookSnippet) listView.getItemAtPosition(position);
                Utils.showToastAndLog(BooksActivity.this, "selected book " + book.getTitle(), TAG);

                Intent intent = new Intent(BooksActivity.this, BookActivity.class);
                intent.putExtra(Constants.INTENT_BOOK, book.getBookId());
                intent.putExtra(Constants.INTENT_USER, user);
                startActivity(intent);
            }
        });
	}

    private ListAdapter createListAdapter(Query query) {
        FirebaseListOptions options = new FirebaseListOptions.Builder<BookSnippet>()
                .setQuery(query, BookSnippet.class)
                .setLifecycleOwner(this)
                .setLayout(R.layout.cardview_book)
                .build();

        FirebaseListAdapter adapter = new FirebaseListAdapter<BookSnippet>(options) {
            @Override
            protected void populateView(View v, BookSnippet model, int position) {
                ((TextView) v.findViewById(R.id.cv_book_title)).setText(model.getTitle());
            }
        };

        return adapter;
    }
}
