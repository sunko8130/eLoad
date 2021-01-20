package com.frontiertechnologypartners.mykyat_topup.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.common.ViewModelFactory;
import com.frontiertechnologypartners.mykyat_topup.model.LoginResponse;
import com.frontiertechnologypartners.mykyat_topup.model.UserData;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.ui.MainActivity;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseActivity;
import com.frontiertechnologypartners.mykyat_topup.ui.change_password.ChangePasswordActivity;
import com.frontiertechnologypartners.mykyat_topup.ui.forgot_password.ForgotPasswordActivity;
import com.google.gson.Gson;

import org.mmtextview.MMFontUtils;
import org.mmtextview.components.MMRadioButton;

import java.util.Locale;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.frontiertechnologypartners.mykyat_topup.util.Constant.ENGLISH;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.FORGOT_PASS;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.MYANMAR;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.USER_DATA;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.cb_password_toggle)
    CheckBox passToggle;

    @BindView(R.id.et_login_name)
    EditText etLoginName;

    @BindView(R.id.et_login_password)
    EditText etPassword;

    @BindView(R.id.lang_switch)
    ToggleButton langSwitch;

    @BindView(R.id.rg_language)
    RadioGroup radioGroupLanguage;

    @BindView(R.id.rdMyanmar)
    MMRadioButton radioMyanmar;

    @BindView(R.id.rdEnglish)
    MMRadioButton radioEnglish;

    @Inject
    ViewModelFactory viewModelFactory;

    private LoginViewModel loginViewModel;
    private Boolean checked;
    private boolean isForgetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //init
        ButterKnife.bind(this);

        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);

        if (MMFontUtils.isSupportUnicode(this)) {
            etLoginName.setHint(R.string.login_name);
            etPassword.setHint(R.string.password);
        } else {
            etLoginName.setHint(Html.fromHtml(MMFontUtils.uni2zg(getString(R.string.login_name))));
            etPassword.setHint(Html.fromHtml(MMFontUtils.uni2zg(getString(R.string.password))));
        }

        //show hide password
        passToggle.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                //show password
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                //hide password
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        //change language
        String defLanguage = Locale.getDefault().getLanguage();
        if (defLanguage.equals(MYANMAR)) {
            langSwitch.setChecked(false);
            radioMyanmar.setChecked(true);
        } else if (defLanguage.equals(ENGLISH)) {
            langSwitch.setChecked(true);
            radioEnglish.setChecked(true);
        }
        langSwitch.setOnClickListener(view -> {
            checked = langSwitch.isChecked();
            if (checked.equals(false)) {
                setNewLocale(MYANMAR);
            } else if (checked.equals(true)) {
                setNewLocale(ENGLISH);
            }
        });

        radioGroupLanguage.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId) {
                case R.id.rdMyanmar:
                    setNewLocale(MYANMAR);
                    break;
                case R.id.rdEnglish:
                    setNewLocale(ENGLISH);
                    break;
                default:
                    break;
            }

        });

        if (getIntent() != null) {
            isForgetPass = getIntent().getBooleanExtra(FORGOT_PASS, false);
        }

        //observe login data
        observeLoginData();
    }

    @OnClick(R.id.tv_forgot_password)
    void onClickForgotPass() {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    @OnClick(R.id.btn_login)
    void onClickLogin() {
        String username = etLoginName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (username.isEmpty()) {
            etLoginName.setError(getResources().getString(R.string.login_name_require));
        } else if (password.isEmpty()) {
            etPassword.setError(getResources().getString(R.string.login_password_require));
        } else {
            loginViewModel.loadLoginUserData(username, password);
        }
    }

    private void observeLoginData() {
        loginViewModel.loginResponse.observe(this, this::consumeResponse);
    }

    private void consumeResponse(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                LoginResponse loginResponse = (LoginResponse) apiResponse.data;
                if (loginResponse != null) {
                    int statusCode = loginResponse.getResponseMessage().getCode();
                    if (statusCode == 1) {
                        UserData userData = loginResponse.getUserData();
                        Gson gson = new Gson();
                        String loginUserData = gson.toJson(userData);
                        prefs.toPreference(USER_DATA, loginUserData);

                        if (isForgetPass) {
                            Intent forgotIntent = new Intent(this, ChangePasswordActivity.class);
                            startActivity(forgotIntent);
                        } else {
                            Intent mainIntent = new Intent(this, MainActivity.class);
                            startActivity(mainIntent);
                        }

                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(loginResponse.getResponseMessage().getMessage());
                    }
                }
                break;
            case ERROR:
                loadingDialog.dismiss();
                if (apiResponse.message != null) {
                    messageDialog.show();
                    messageDialog.loadingMessage(apiResponse.message);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
