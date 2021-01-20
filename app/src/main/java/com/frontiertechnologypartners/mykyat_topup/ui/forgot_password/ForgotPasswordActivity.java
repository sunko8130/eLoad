package com.frontiertechnologypartners.mykyat_topup.ui.forgot_password;

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
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseActivity;
import com.frontiertechnologypartners.mykyat_topup.ui.login.LoginActivity;
import com.frontiertechnologypartners.mykyat_topup.widget.MessageDialog;
import com.google.android.material.textfield.TextInputEditText;

import javax.inject.Inject;

import static com.frontiertechnologypartners.mykyat_topup.util.Constant.FORGOT_PASS;


public class ForgotPasswordActivity extends BaseActivity implements MessageDialog.OnClickListener {

    @BindView(R.id.et_mobile)
    TextInputEditText etMobile;

    @BindView(R.id.et_new_password)
    TextInputEditText etNewPassword;

    @BindView(R.id.et_confirm_password)
    TextInputEditText etConfirmPassword;

    @Inject
    ViewModelFactory viewModelFactory;
    private ForgotPassViewModel forgotPassViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_forgot_password);
        //init
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.forgot_pass_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        forgotPassViewModel = ViewModelProviders.of(this, viewModelFactory).get(ForgotPassViewModel.class);

        observeForgotPassword();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void observeForgotPassword() {
        forgotPassViewModel.forgotPassResponse.observe(this, this::consumeForgotPassword);
    }

    private void consumeForgotPassword(ApiResponse<?> apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingDialog.show();
                break;
            case SUCCESS:
                loadingDialog.dismiss();
                ChangePasswordResponse changePassResponse = (ChangePasswordResponse) apiResponse.data;
                if (changePassResponse != null) {
                    if (changePassResponse.getResponseMessage().getCode() == 1) {
                        messageDialog.setListener(this);
                        messageDialog.show();
                        messageDialog.loadingMessage(changePassResponse.getResponseMessage().getMessage());
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

    @OnClick(R.id.btn_forgot_password)
    void onClickForgotPassword() {
        if (TextUtils.isEmpty(etMobile.getText())) {
            etMobile.setError(getString(R.string.mobile_require_msg));
        } else if (etMobile.getText().length() < 9) {
            etMobile.setError(getString(R.string.invalid_mobile_no));
        } else {
            String mobile = etMobile.getText().toString().trim();
            forgotPassViewModel.forgotPassword(mobile);
        }
    }

    @Override
    public void onOkButtonClick() {
        messageDialog.dismiss();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.putExtra(FORGOT_PASS, true);
        startActivity(loginIntent);
        finish();
    }
}
