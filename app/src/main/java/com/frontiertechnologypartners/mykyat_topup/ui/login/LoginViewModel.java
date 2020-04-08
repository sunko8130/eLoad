package com.frontiertechnologypartners.mykyat_topup.ui.login;

import com.frontiertechnologypartners.mykyat_topup.model.LoginResponse;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.repository.NetworkRepository;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    LoginViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    void loadLoginUserData(String username, String password) {
        disposable.add(networkRepository.getLoginUserData(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> loginResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse value) {
                        loginResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginResponse.setValue(ApiResponse.error(e));
                    }
                }));
    }


}
