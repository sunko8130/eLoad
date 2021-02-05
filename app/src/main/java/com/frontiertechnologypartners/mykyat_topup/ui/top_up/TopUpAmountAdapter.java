package com.frontiertechnologypartners.mykyat_topup.ui.top_up;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TopUpAmountAdapter extends RecyclerView.Adapter<TopUpAmountAdapter.RadioViewHolder> {

    private int mSelectedItem = -1;
    private List<String> mItems = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mListener;

    public TopUpAmountAdapter(Context context, OnItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setItems(List<String> amounts) {
        if (amounts == null) {
            throw new IllegalArgumentException("Cannot set `null` item to the Recycler adapter");
        }
        mItems.clear();
        mItems.addAll(amounts);
        notifyDataSetChanged();
    }

    public String getItem(int position) {
        return mItems.get(position);
    }

    @NonNull
    @Override
    public RadioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.topup_item, parent, false);
        return new RadioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadioViewHolder holder, int position) {
        holder.cbOperator.setChecked(position == mSelectedItem);
        holder.onBind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class RadioViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_operator)
        CheckBox cbOperator;

        RadioViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBind(String amount) {

            cbOperator.setText(Util.convertAmountWithFormat(Double.parseDouble(amount)));

            cbOperator.setOnClickListener(view -> {
                mSelectedItem = getAdapterPosition();
                mListener.onItemClick(mSelectedItem);
                notifyDataSetChanged();
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
