package com.rewardculture;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rewardculture.model.database.Database;
import com.rewardculture.model.database.LocalDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {
	Database database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = LocalDatabase.getInstance();

		setContentView(R.layout.activity_main);
		ListView listView = findViewById(R.id.listview);
		listView.setAdapter(new CategoryAdapter(this, database.getBookCategories()));

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
									int position, long id) {
				Toast.makeText(MainActivity.this, "" + position,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public class CategoryAdapter extends BaseAdapter {
		private Context mContext;
		List<String> categories;

		public CategoryAdapter(Context c, List<String> categories) {
			mContext = c;
			this.categories = categories;
		}

		public int getCount() {
			return categories.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView;
			if (convertView == null) {
				textView = new TextView(mContext);
				textView.setText(categories.get(position));
			} else {
				textView = (TextView) convertView;
			}

			return textView;
		}
	}
}
