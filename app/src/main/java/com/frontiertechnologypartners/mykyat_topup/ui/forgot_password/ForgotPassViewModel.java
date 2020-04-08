package com.frontiertechnologypartners.mykyat_topup.ui.forgot_password;

import com.frontiertechnologypartners.mykyat_topup.model.ChangePasswordResponse;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.repository.NetworkRepository;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ForgotPassViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    public ForgotPassViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    void forgotPassword(String mobile) {
        disposable.add(networkRepository.forgotPassword(mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> forgotPassResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<ChangePasswordResponse>() {
                    @Override
                    public void onSuccess(ChangePasswordResponse value) {
                        forgotPassResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        forgotPassResponse.setValue(ApiResponse.error(e));
                    }
                }));
    }
}
