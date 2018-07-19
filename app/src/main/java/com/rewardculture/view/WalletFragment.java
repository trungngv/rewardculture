package com.rewardculture.view;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.rewardculture.R;
import com.rewardculture.database.FirebaseDatabaseHelper;
import com.rewardculture.model.User;

/**
 * A simple {@link ListFragment} subclass.
 *
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFragment extends android.support.v4.app.ListFragment {
    private static final String ARG_USER = "user";

    FirebaseDatabaseHelper dbHelper;
    User user;

    public WalletFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment WalletFragment.
     */
    public static WalletFragment newInstance(User user) {
        WalletFragment fragment = new WalletFragment();
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
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        return view;
        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(createListAdapter());
    }

    private ListAdapter createListAdapter() {
        String[] values = {"First", "Second", "Third"};
        ListAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.cardview_transaction,
                R.id.txt_transaction,
                values);

        return adapter;
    }
}
