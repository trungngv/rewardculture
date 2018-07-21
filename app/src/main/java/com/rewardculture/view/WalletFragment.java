package com.rewardculture.view;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rewardculture.R;
import com.rewardculture.database.FirebaseDatabaseHelper;
import com.rewardculture.model.Transaction;
import com.rewardculture.model.User;
import com.rewardculture.ost.RewardCultureEconomy;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link ListFragment} subclass.
 * <p>
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFragment extends android.support.v4.app.ListFragment {
    private static final String ARG_USER = "user";

    FirebaseDatabaseHelper dbHelper;
    User user;
    ProgressDialog progressDialog;
    RewardCultureEconomy economy = new RewardCultureEconomy();
    SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy HH:mm:ss");

    public WalletFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WalletFragment.
     */
    public static WalletFragment newInstance() {
        return new WalletFragment();
    }

    public void updateUser(User user) {
        this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (user != null) {
            updateBalanceAndTransactions();
        }
    }

    /**
     * Update the balance and transaction
     */
    private void updateBalanceAndTransactions() {
        new BalanceRetrievalTask().execute(user.getOstId());
        new TransactionListRetrievalTask().execute(user.getOstId());
    }

    /**
     * Customised adapter to display transactions.
     */
    private final class TransactionListAdapter extends ArrayAdapter<Transaction> {
        public TransactionListAdapter(List<Transaction> transactions) {
            super(WalletFragment.this.getContext(), R.layout.cardview_transaction, transactions);
        }

        String formatDateTime(long timestamp) {
            return formatter.format(new Date(timestamp));
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = getLayoutInflater().inflate(
                        R.layout.cardview_transaction, parent, false);
            } else {
                view = convertView;
            }
            Transaction transaction = getItem(position);

            ((TextView) view.findViewById(R.id.txt_date)).setText(
                    formatDateTime(transaction.getTransactionTime()));
            // entity is the other party in the transaction
            String entity;
            // amount is positive if user is the recipient and negative if user is the sender
            String amount;
            String fromId = transaction.getFromUuid();
            String toId = transaction.getToUuid();
            if (fromId == user.getOstId()) {
                entity = toId;
                amount = "-" + String.format("$%.4f", transaction.getAmount());
            } else {
                entity = fromId;
                amount = "+" + String.format("$%.4f", transaction.getAmount());
            }
            ((TextView) view.findViewById(R.id.txt_entity)).setText(entity);
            TextView amountView = view.findViewById(R.id.txt_amount);
            amountView.setText(amount);
            if (amount.startsWith("+")) {
                amountView.setTextColor(getResources().getColor(android.R.color.holo_green_light));
            } else {
                amountView.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
            }
            return view;
        }
    }

    /**
     * Async task runner to get and display available balance.
     */
    private final class BalanceRetrievalTask extends AsyncTask<String, String, Float> {

        @Override
        protected Float doInBackground(String... params) {
            try {
                float balance = economy.getAvailableBalance(params[0]);
                return balance;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Float balance) {
            if (balance == null) {
                balance = 0.0f;
            }
            ((TextView) getView().findViewById(R.id.txt_balance)).setText(
                    String.format("$%.2f Available", balance));
        }
    }

    /**
     * Async task runner to get OST transactions. AsyncTask takes 3 params: first is the type
     * of the parameter array, second is the type of the object to send to progress, and third
     * is the result type.
     */
    private final class TransactionListRetrievalTask extends AsyncTask<String, String, List> {

        List<Transaction> toTransactionsArray(JsonArray array) {
            ArrayList<Transaction> transactions = new ArrayList<>();
            for (JsonElement t : array) {
                transactions.add(Transaction.fromJsonObject((JsonObject) t));
            }

            return transactions;
        }

        @Override
        protected List<Transaction> doInBackground(String... params) {
            publishProgress("getting transactions");
            try {
                return toTransactionsArray(economy.getTransactions(params[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List result) {
            progressDialog.dismiss();
            ListAdapter adapter = new TransactionListAdapter(result);
            setListAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(WalletFragment.this.getContext(),
                    "Please wait...",
                    "Retrieving transactions from OST Blockchain...");
        }

        @Override
        protected void onProgressUpdate(String... text) {
            // no progress update for this task
        }
    }
}
