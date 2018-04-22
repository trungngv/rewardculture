package com.rewardculture.view;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rewardculture.R;
import com.rewardculture.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends ListActivity {
	static final String ARG_BOOKS = "books";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		List<Book> books = (List<Book>) getIntent().getSerializableExtra(ARG_BOOKS);
		setListAdapter(new BooksAdapter(this, books));

		final ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) listView.getItemAtPosition(position);
                Toast.makeText(BooksActivity.this, "selected book " + book.getTitle(), Toast.LENGTH_SHORT);

                Intent intent = new Intent(BooksActivity.this, BookActivity.class);
                intent.putExtra(BookActivity.ARG_BOOK, book);
                startActivity(intent);
            }
        });
	}

	public class BooksAdapter extends ArrayAdapter<Book> {

		public BooksAdapter(@NonNull Context context, @NonNull List<Book> objects) {
			super(context, R.layout.cardview_book, objects);
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.cardview_book, parent, false);
			}
            Book book = getItem(position);
			((TextView) convertView.findViewById(R.id.cv_book_title))
					.setText(book.getTitle());
			return convertView;
		}
	}
}
