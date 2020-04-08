package com.frontiertechnologypartners.mykyat_topup.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.frontiertechnologypartners.mykyat_topup.App;
import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.util.PreferenceUtils;
import com.frontiertechnologypartners.mykyat_topup.widget.LoadingDialog;
import com.frontiertechnologypartners.mykyat_topup.widget.MessageDialog;

import androidx.annotation.Nullable;
import dagger.android.support.DaggerFragment;

public class BaseFragment extends DaggerFragment {

    protected LoadingDialog loadingDialog;
    protected MessageDialog messageDialog;
    protected PreferenceUtils prefs;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init loading
        loadingDialog = new LoadingDialog(mContext, R.style.CustomProgressBarTheme);
        messageDialog = new MessageDialog(mContext);

        prefs = new PreferenceUtils(getActivity());
    }

    protected void setNewLocale(String language) {
        App.localeManager.persistLanguage(language);
        //reload parent activity
        new Handler().post(() -> {
            if (getActivity()!=null){
                Intent intent = getActivity().getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });
    }
}
