package com.frontiertechnologypartners.mykyat_topup.ui.transaction;

import com.frontiertechnologypartners.mykyat_topup.model.ProvidersResponse;
import com.frontiertechnologypartners.mykyat_topup.model.TransactionResponse;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.repository.NetworkRepository;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseViewModel;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TransactionViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    TransactionViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    void transactionHistory(Map<String, Object> body) {
        disposable.add(networkRepository.transactionHistory(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> transactionResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<TransactionResponse>() {
                    @Override
                    public void onSuccess(TransactionResponse value) {
                        transactionResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        transactionResponse.setValue(ApiResponse.error(e));
                    }
                }));
    }

    void getAllOperators(){
        disposable.add(networkRepository.getAllOperators()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> allOperatorsResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<ProvidersResponse>() {
                    @Override
                    public void onSuccess(ProvidersResponse value) {
                        allOperatorsResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        allOperatorsResponse.setValue(ApiResponse.error(e));
                    }
                }));
    }
}
