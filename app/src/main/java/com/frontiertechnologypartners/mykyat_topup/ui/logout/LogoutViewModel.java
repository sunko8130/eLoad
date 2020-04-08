package com.frontiertechnologypartners.mykyat_topup.ui.logout;

import com.frontiertechnologypartners.mykyat_topup.model.Logout;
import com.frontiertechnologypartners.mykyat_topup.model.SVAResponse;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.repository.NetworkRepository;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LogoutViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    public LogoutViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    public void logout(int userId) {
        disposable.add(networkRepository.logout(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> logoutResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<Logout>() {
                    @Override
                    public void onSuccess(Logout value) {
                        logoutResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        logoutResponse.setValue(ApiResponse.error(e));
                    }
                }));
    }
}
