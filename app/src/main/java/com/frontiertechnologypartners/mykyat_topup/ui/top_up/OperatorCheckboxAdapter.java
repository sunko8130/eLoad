package com.frontiertechnologypartners.mykyat_topup.ui.top_up;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.model.Operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.frontiertechnologypartners.mykyat_topup.util.Constant.MPT;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.MYTEL;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.OOREDOO;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.TELENOL;

public class OperatorCheckboxAdapter extends RecyclerView.Adapter<OperatorCheckboxAdapter.RadioViewHolder> {

    private int mSelectedItem = -1;
    private List<Operators> mItems = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mListener;

    public OperatorCheckboxAdapter(Context context, OnItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setItems(List<Operators> operators) {
        if (operators == null) {
            throw new IllegalArgumentException("Cannot set `null` item to the Recycler adapter");
        }
        mItems.clear();
        mItems.addAll(operators);
        notifyDataSetChanged();
    }

    public Operators getItem(int position) {
        return mItems.get(position);
    }

    @NonNull
    @Override
    public RadioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.operator_item, parent, false);
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

        @BindView(R.id.iv_operator)
        ImageView ivOperator;

        RadioViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBind(Operators operator) {
            cbOperator.setText(String.format(Locale.getDefault(), "%s", operator.getOperator()));
            //show operator logo
            switch (operator.getOperator()) {
                case MPT:
                    ivOperator.setImageResource(R.drawable.ic_mpt);
                    break;
                case OOREDOO:
                    ivOperator.setImageResource(R.drawable.ic_ooredoo);
                    break;
                case TELENOL:
                    ivOperator.setImageResource(R.drawable.ic_telenol);
                    break;
                case MYTEL:
                    ivOperator.setImageResource(R.drawable.ic_mytel);
                    break;
            }

            cbOperator.setOnClickListener(view -> {
                mSelectedItem = getAdapterPosition();
                mListener.onItemClick(mSelectedItem);
                notifyDataSetChanged();
            });

            ivOperator.setOnClickListener(view -> {
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
