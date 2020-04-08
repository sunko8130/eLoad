package com.frontiertechnologypartners.mykyat_topup.ui.transaction;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.model.Transaction;
import com.frontiertechnologypartners.mykyat_topup.ui.base.GenericRecyclerViewAdapter;
import com.frontiertechnologypartners.mykyat_topup.ui.delegate.OnRecyclerItemClickListener;

import androidx.annotation.NonNull;

public class TransactionAdapter extends GenericRecyclerViewAdapter<Transaction, OnRecyclerItemClickListener, TransactionViewHolder> {

    TransactionAdapter(Context context, OnRecyclerItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(inflate(R.layout.transaction_item, parent), getListener());
    }
}
