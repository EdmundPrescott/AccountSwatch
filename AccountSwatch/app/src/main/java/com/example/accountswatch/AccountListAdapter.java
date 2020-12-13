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

    //region recyclerview variables

    // Recyclerview displays this list
    private List<Account> listItems = new LinkedList<>();

    private LayoutInflater mInflater;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    //endregion

    //region constructor
    public AccountListAdapter(List<Account> accounts, MainActivity context, MainActivity recyclerViewClickInterface) {
        // Initializes the layout inflater, recyclerview values, and the recyclerview onClick
        mInflater = LayoutInflater.from(context);
        listItems = accounts;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    //endregion

    //region recyclerview methods

    // Inflate the individual views
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.accountlist_item, parent, false);
        return new ViewHolder(view, this);
    }

    // Initialize the individual titles
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account account = listItems.get(position);
        holder.title.setText(account.getWebsite());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    //endregion

    //region recyclerview node

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

    //endregion

}
