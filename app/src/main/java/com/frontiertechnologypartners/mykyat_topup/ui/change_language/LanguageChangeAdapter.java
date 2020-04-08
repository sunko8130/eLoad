package com.frontiertechnologypartners.mykyat_topup.ui.change_language;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.model.Language;
import com.frontiertechnologypartners.mykyat_topup.model.Operators;

import org.mmtextview.components.MMTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.frontiertechnologypartners.mykyat_topup.util.Constant.ENGLISH;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.MYANMAR;

public class LanguageChangeAdapter extends RecyclerView.Adapter<LanguageChangeAdapter.RadioViewHolder> {

    private int mSelectedItem = -1;
    private List<Language> mItems = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mListener;
    private int mLangCheck;

    public LanguageChangeAdapter(Context context, OnItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setItems(List<Language> languageList, int langCheck) {
        if (languageList == null) {
            throw new IllegalArgumentException("Cannot set `null` item to the Recycler adapter");
        }
        mLangCheck = langCheck;
        mItems.clear();
        mItems.addAll(languageList);
        notifyDataSetChanged();
    }

    public Language getItem(int position) {
        return mItems.get(position);
    }

    @NonNull
    @Override
    public RadioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.language_change_item, parent, false);
        return new RadioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadioViewHolder holder, int position) {
        holder.cbLanguage.setChecked(position == mSelectedItem);
        holder.itemView.setSelected(position == mSelectedItem);
        holder.onBind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class RadioViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_language)
        CheckBox cbLanguage;
        @BindView(R.id.tv_language)
        MMTextView tvLanguage;
        @BindView(R.id.img_language)
        ImageView imgLanguage;

        RadioViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBind(Language language) {
            if (mLangCheck == getAdapterPosition()) {
                cbLanguage.setChecked(true);
            } else {
                cbLanguage.setChecked(false);
            }
            tvLanguage.setText(language.getLanguage());
            imgLanguage.setImageResource(language.getImageLanguage());
            itemView.setOnClickListener(view -> {
                mSelectedItem = getAdapterPosition();
                mListener.onItemClick(mSelectedItem);
                notifyDataSetChanged();
            });
            cbLanguage.setOnClickListener(view -> {
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
