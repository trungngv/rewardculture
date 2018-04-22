package com.rewardculture.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rewardculture.R;
import com.rewardculture.model.database.Database;
import com.rewardculture.model.database.LocalDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	Database database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = LocalDatabase.getInstance();
		List<String> categories = database.getBookCategories();
		setContentView(R.layout.activity_main);

		final ListView listView = findViewById(R.id.listview);
		listView.setAdapter(new CategoriesAdapter(this, categories));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
									int position, long id) {
				String selectedCategory = (String) listView.getSelectedItem();

				Intent intent = new Intent(MainActivity.this, BooksActivity.class);
				intent.putExtra(BooksActivity.ARG_BOOKS,
						(ArrayList) database.getBooks(selectedCategory));
				startActivity(intent);
			}
		});
	}

	public class CategoriesAdapter extends ArrayAdapter<String> {

		public CategoriesAdapter(Context c, List<String> categories) {
			super(c, R.layout.cardview_category, categories);
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.cardview_category, parent, false);
			}
			String category = getItem(position);
			((TextView) convertView.findViewById(R.id.category_name)).setText(category);
			return convertView;
		}

	}
}
