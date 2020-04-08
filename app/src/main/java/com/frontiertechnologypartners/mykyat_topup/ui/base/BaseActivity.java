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

import org.mmtextview.MMFontUtils;

import androidx.annotation.Nullable;
import dagger.android.support.DaggerAppCompatActivity;

public class BaseActivity extends DaggerAppCompatActivity {

    protected LoadingDialog loadingDialog;
    protected MessageDialog messageDialog;
    protected PreferenceUtils prefs;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(App.localeManager.setLocale(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MMFontUtils.initMMTextView(this);
        //init loading
        loadingDialog = new LoadingDialog(this, R.style.CustomProgressBarTheme);
        messageDialog = new MessageDialog(this);

        prefs = new PreferenceUtils(this);
    }

    protected void setNewLocale(String language) {
        App.localeManager.persistLanguage(language);
        //reload parent activity
        new Handler().post(() -> {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }
}
