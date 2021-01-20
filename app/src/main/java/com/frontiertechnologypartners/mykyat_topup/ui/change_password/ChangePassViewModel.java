package com.frontiertechnologypartners.mykyat_topup.ui.change_password;

import com.frontiertechnologypartners.mykyat_topup.model.ChangePasswordResponse;
import com.frontiertechnologypartners.mykyat_topup.model.LoginResponse;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.repository.NetworkRepository;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ChangePassViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    public ChangePassViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    void changePassword(String password, int userId, String currentPassword) {
        disposable.add(networkRepository.changePassword(password, userId, currentPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> changePassResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<ChangePasswordResponse>() {
                    @Override
                    public void onSuccess(ChangePasswordResponse value) {
                        changePassResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        changePassResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }
}
