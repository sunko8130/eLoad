package com.frontiertechnologypartners.mykyat_topup.ui.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frontiertechnologypartners.mykyat_topup.ui.delegate.BaseRecyclerListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public abstract class GenericRecyclerViewAdapter<T, L extends BaseRecyclerListener, VH extends BaseViewHolder<T, L>> extends RecyclerView.Adapter<VH> {

    private List<T> items;
    private L listener;
    private LayoutInflater layoutInflater;

    public GenericRecyclerViewAdapter(Context context, L listener) {
        this.listener = listener;
        this.items = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public abstract VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (items.size() <= position) {
            return;
        }
        T item = items.get(position);
        holder.onBind(item);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }


    public void setItems(List<T> items) {
        setItemsList(items);
    }


    private void setItemsList(List<T> items) throws IllegalArgumentException {
        if (items == null) {
            throw new IllegalArgumentException("Cannot set `null` item to the Recycler adapter");
        }
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public List<T> getItems() {
        return items;
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void setListener(L listener) {
        this.listener = listener;
    }

    protected L getListener() {
        return listener;
    }

    @NonNull
    protected View inflate(@LayoutRes final int layout, final @Nullable ViewGroup parent) {
        return layoutInflater.inflate(layout, parent, false);
    }
}