package com.frontiertechnologypartners.mykyat_topup.ui.change_password;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.common.ViewModelFactory;
import com.frontiertechnologypartners.mykyat_topup.model.ChangePasswordResponse;
import com.frontiertechnologypartners.mykyat_topup.model.UserData;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.ui.MainActivity;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import javax.inject.Inject;

import static com.frontiertechnologypartners.mykyat_topup.util.Constant.USER_DATA;

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.et_current_password)
    TextInputEditText etCurrentPassword;

    @BindView(R.id.et_new_password)
    TextInputEditText etNewPassword;

    @BindView(R.id.et_confirm_password)
    TextInputEditText etConfirmPassword;

    @Inject
    ViewModelFactory viewModelFactory;
    private ChangePassViewModel changePassViewModel;

    private UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        //init
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.change_password));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        changePassViewModel = ViewModelProviders.of(this, viewModelFactory).get(ChangePassViewModel.class);
        //get login user data
        Gson gson = new Gson();
        String loginUserData = prefs.fromPreference(USER_DATA, "");
        userData = gson.fromJson(loginUserData, UserData.class);

        observeChangePassword();
    }

    private void observeChangePassword() {
        changePassViewModel.changePassResponse.observe(this, this::consumeChangePassResponse);
    }

    private void consumeChangePassResponse(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
            case SUCCESS:
                loadingDialog.dismiss();
                ChangePasswordResponse changePassResponse = (ChangePasswordResponse) apiResponse.data;
                if (changePassResponse != null) {
                    if (changePassResponse.getResponseMessage().getCode() == 1) {
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                        Toast.makeText(this, R.string.sucess_change_password, Toast.LENGTH_SHORT).show();
                    } else {
                        messageDialog.show();
                        messageDialog.loadingMessage(changePassResponse.getResponseMessage().getMessage());
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

    @OnClick(R.id.btn_change_password)
    void onClickChangePassword() {

        if (TextUtils.isEmpty(etCurrentPassword.getText())) {
            etCurrentPassword.setError(getString(R.string.alert_curent_password));
        } else if (TextUtils.isEmpty(etNewPassword.getText())) {
            etNewPassword.setError(getString(R.string.alert_new_password));
        } else if (TextUtils.isEmpty(etConfirmPassword.getText())) {
            etConfirmPassword.setError(getString(R.string.alert_confirm_password));
        } else if (!etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            etNewPassword.setError(getString(R.string.alert_password_matcher));
            etConfirmPassword.setError(getString(R.string.alert_password_matcher));
        } else {
            String currentPassword = etCurrentPassword.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            changePassViewModel.changePassword(newPassword, userData.getUser().getId(), currentPassword);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
