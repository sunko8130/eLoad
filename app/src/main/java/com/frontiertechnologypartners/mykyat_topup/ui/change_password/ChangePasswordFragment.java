package com.frontiertechnologypartners.mykyat_topup.ui.change_password;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.frontiertechnologypartners.mykyat_topup.R;
import com.frontiertechnologypartners.mykyat_topup.common.ViewModelFactory;
import com.frontiertechnologypartners.mykyat_topup.model.ChangePasswordResponse;
import com.frontiertechnologypartners.mykyat_topup.model.UserData;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseFragment;
import com.frontiertechnologypartners.mykyat_topup.ui.top_up.PreTopupFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import javax.inject.Inject;

import static com.frontiertechnologypartners.mykyat_topup.util.Constant.USER_DATA;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends BaseFragment {

    @BindView(R.id.et_current_password)
    TextInputEditText etCurentPassword;

    @BindView(R.id.et_new_password)
    TextInputEditText etNewPassword;

    @BindView(R.id.et_confirm_password)
    TextInputEditText etConfirmPassword;

    @Inject
    ViewModelFactory viewModelFactory;
    private ChangePassViewModel changePassViewModel;

    private UserData userData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //init
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                        if (getFragmentManager() != null) {
                            getFragmentManager().beginTransaction().replace(R.id.frame, new PreTopupFragment())
                                    .commit();
                        }
                        Toast.makeText(mContext, R.string.sucess_change_password, Toast.LENGTH_SHORT).show();
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

        if (TextUtils.isEmpty(etCurentPassword.getText())) {
            etCurentPassword.setError(getString(R.string.alert_curent_password));
        } else if (TextUtils.isEmpty(etNewPassword.getText())) {
            etNewPassword.setError(getString(R.string.alert_new_password));
        } else if (TextUtils.isEmpty(etConfirmPassword.getText())) {
            etConfirmPassword.setError(getString(R.string.alert_confirm_password));
        } else if (!etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            etNewPassword.setError(getString(R.string.alert_password_matcher));
            etConfirmPassword.setError(getString(R.string.alert_password_matcher));
        } else {
            String currentPassword = etCurentPassword.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            changePassViewModel.changePassword(newPassword, userData.getUser().getId(), currentPassword);
        }
    }
}
