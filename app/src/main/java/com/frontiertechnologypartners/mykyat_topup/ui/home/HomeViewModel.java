package com.frontiertechnologypartners.mykyat_topup.ui.home;

import com.frontiertechnologypartners.mykyat_topup.model.SVAResponse;
import com.frontiertechnologypartners.mykyat_topup.model.TotalSalesTotalCommissionResponse;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.repository.NetworkRepository;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    public HomeViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    void totalSalesAndCommission(int userId) {
        disposable.add(networkRepository.totalSalesAndCommission(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> totalSalesAndCommissionResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<TotalSalesTotalCommissionResponse>() {
                    @Override
                    public void onSuccess(TotalSalesTotalCommissionResponse value) {
                        totalSalesAndCommissionResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        totalSalesAndCommissionResponse.setValue(ApiResponse.error(getErrorMessage(e)));
                    }
                }));
    }
}
