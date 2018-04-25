package com.rewardculture.view;

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
import com.rewardculture.model.CategorySnippet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    FirebaseDatabaseHelper dbHelper;

    @BindView(R.id.listview)
    ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dbHelper = FirebaseDatabaseHelper.getInstance();
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
}
