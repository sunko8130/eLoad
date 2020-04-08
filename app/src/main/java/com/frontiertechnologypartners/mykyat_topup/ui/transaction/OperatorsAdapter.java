package com.frontiertechnologypartners.mykyat_topup.ui.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.model.Operators;

import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

public class OperatorsAdapter extends ArrayAdapter<Operators> {
    private List<Operators> operators;

    @BindView(R.id.spinner_text)
    TextView spinnerText;

    public OperatorsAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<Operators> operators) {
        super(context, resource, spinnerText, operators);
        this.operators = operators;
    }

    @Override
    public Operators getItem(int position) {
        return operators.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);
    }

    private View initView(int position) {
        Operators operator = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = null;
        if (inflater != null) {
            v = inflater.inflate(R.layout.dropdown_menu_popup_item, null);
            ButterKnife.bind(this, v);
        }
        if (operator != null) {
            spinnerText.setText(operator.getOperator());
        }
        return v;
    }
}