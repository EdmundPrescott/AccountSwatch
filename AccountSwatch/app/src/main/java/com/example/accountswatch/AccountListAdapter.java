// Complete
package com.example.accountswatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {

    // Recyclerview displays this list
    List<Account> listItems = new LinkedList<>();

    // Useful :^)
    private LayoutInflater mInflater;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    // Constructor
    public AccountListAdapter(List<Account> accounts, MainActivity context, MainActivity recyclerViewClickInterface) {
        // Initializes the layout inflater, recyclerview values, and the recyclerview onClick
        mInflater = LayoutInflater.from(context);
        listItems = accounts;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    // Inflated the individual views
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.accountlist_item, parent, false);
        return new ViewHolder(view, this);
    }

    // Initializes the views fields
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account account = listItems.get(position);
        holder.title.setText(account.getUrl());
    }

    // ViewHolder method
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void remove(int position){
        listItems.remove(position);
    }

    // Individual recyclerview code
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        final AccountListAdapter mAdapter;

        public ViewHolder(@NonNull View itemView, AccountListAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;
            title = itemView.findViewById(R.id.account_item_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
