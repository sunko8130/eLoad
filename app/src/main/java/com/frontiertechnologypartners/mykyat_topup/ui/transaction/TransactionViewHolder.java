package com.frontiertechnologypartners.mykyat_topup.ui.transaction;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.frontiertechnologypartners.mykyat_topup.App;
import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.model.Transaction;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseViewHolder;
import com.frontiertechnologypartners.mykyat_topup.ui.delegate.OnRecyclerItemClickListener;
import com.frontiertechnologypartners.mykyat_topup.util.Util;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;

public class TransactionViewHolder extends BaseViewHolder<Transaction, OnRecyclerItemClickListener> {

    @BindView(R.id.tv_transaction_provider)
    TextView tvTransactionProvider;

    @BindView(R.id.tv_transaction_mobile)
    TextView tvTransactionMobile;

    @BindView(R.id.tv_transaction_amount)
    TextView tvTransactionAmount;

    @BindView(R.id.tv_transaction_date)
    TextView tvTransactionDate;

    private Context mContext;

    TransactionViewHolder(View itemView, OnRecyclerItemClickListener listener) {
        super(itemView, listener);
        mContext = itemView.getContext();
    }

    @Override
    public void onBind(Transaction item) {
        tvTransactionProvider.setText(mContext.getResources()
                .getString(R.string.transaction_provider, item.getOperator()));
        tvTransactionMobile.setText(mContext.getResources()
                .getString(R.string.transaction_mobile, item.getPhoneNo()));
        String[] topUpAmount = Util.convertAmountWithFormat(item.getAmount()).split("\\.");
        tvTransactionAmount.setText(mContext.getResources()
                .getString(R.string.transaction_amount, topUpAmount[0]));
        String dateFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
//        tvTransactionDate.setText(mContext.getResources()
//                .getString(R.string.transaction_date, sdf.format(item.getCreatedDate())));
        tvTransactionDate.setText(mContext.getResources()
                .getString(R.string.transaction_date, item.getCreatedDate()));
    }
}
