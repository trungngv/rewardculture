package com.rewardculture.view;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.rewardculture.model.User;
import com.rewardculture.ost.RewardCultureEconomy;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link ListFragment} subclass.
 *
 * Use the {@link MainCategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainCategoriesFragment extends android.support.v4.app.ListFragment {
    private static final String ARG_USER = "user";

    FirebaseDatabaseHelper dbHelper;
    User user;

    public MainCategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MainCategoriesFragment.
     */
    public static MainCategoriesFragment newInstance(User user) {
        MainCategoriesFragment fragment = new MainCategoriesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_USER);
        }
        dbHelper = FirebaseDatabaseHelper.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_categories, container, false);
        return view;
        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Query query = dbHelper.getBookCategories();
        setListAdapter(createListAdapter(query));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CategorySnippet category = (CategorySnippet) l.getItemAtPosition(position);
        Intent intent = new Intent(getActivity(), BooksActivity.class);
        intent.putExtra(Constants.INTENT_CATEGORY, category.id);
        intent.putExtra(Constants.INTENT_USER, user);
        startActivity(intent);
    }

    // TODO change to FirebaseRecyclerAdapter for efficiency later
    private ListAdapter createListAdapter(Query query) {
        // NOTE: setLifecycleOwner must be set for this to start listening to database events
        FirebaseListOptions options = new FirebaseListOptions.Builder<CategorySnippet>()
                .setQuery(query, CategorySnippet.class)
                .setLifecycleOwner(getActivity())
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
