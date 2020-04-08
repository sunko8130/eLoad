package com.frontiertechnologypartners.mykyat_topup.ui.base;

import android.content.Context;
import android.view.View;
import com.frontiertechnologypartners.mykyat_topup.ui.delegate.BaseRecyclerListener;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public abstract class BaseViewHolder<T, L extends BaseRecyclerListener> extends RecyclerView.ViewHolder {

    private L mListener;

    public BaseViewHolder(View itemView, L listener) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.mListener = listener;
    }

    protected abstract void onBind(T item);

    L getListener() {
        return mListener;
    }
}