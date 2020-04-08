package com.frontiertechnologypartners.mykyat_topup.ui.top_up;

import com.frontiertechnologypartners.mykyat_topup.model.PreTopUpResponse;
import com.frontiertechnologypartners.mykyat_topup.model.ProvidersResponse;
import com.frontiertechnologypartners.mykyat_topup.model.SVAResponse;
import com.frontiertechnologypartners.mykyat_topup.model.TopUpResponse;
import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import com.frontiertechnologypartners.mykyat_topup.repository.NetworkRepository;
import com.frontiertechnologypartners.mykyat_topup.ui.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TopupViewModel extends BaseViewModel {
    private NetworkRepository networkRepository;

    @Inject
    TopupViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    void getAvailableProviders(){
        disposable.add(networkRepository.getProviders()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> providersResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<ProvidersResponse>() {
                    @Override
                    public void onSuccess(ProvidersResponse value) {
                        providersResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        providersResponse.setValue(ApiResponse.error(e));
                    }
                }));
    }

    void getPreTopUpData(String userId,String provider,String mobile,String amount){
        disposable.add(networkRepository.preTopUp(userId,provider,mobile,amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> preTopUpResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<PreTopUpResponse>() {
                    @Override
                    public void onSuccess(PreTopUpResponse value) {
                        preTopUpResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        preTopUpResponse.setValue(ApiResponse.error(e));
                    }
                }));
    }

    void topUp(String userId,String provider,String mobile,String amount){
        disposable.add(networkRepository.topUp(userId,provider,mobile,amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> topUpResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<TopUpResponse>() {
                    @Override
                    public void onSuccess(TopUpResponse value) {
                        topUpResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        topUpResponse.setValue(ApiResponse.error(e));
                    }
                }));
    }

    void refreshAmount(String userId){
        disposable.add(networkRepository.refreshAmount(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> svaResponse.setValue(ApiResponse.loading()))
                .subscribeWith(new DisposableSingleObserver<SVAResponse>() {
                    @Override
                    public void onSuccess(SVAResponse value) {
                        svaResponse.setValue(ApiResponse.success(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        svaResponse.setValue(ApiResponse.error(e));
                    }
                }));
    }
}
