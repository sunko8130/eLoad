package com.frontiertechnologypartners.mykyat_topup.ui.base;

import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;

public class BaseViewModel extends ViewModel {

    protected final CompositeDisposable disposable = new CompositeDisposable();
    public final MutableLiveData<ApiResponse> loginResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse> providersResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse> preTopUpResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse> topUpResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse> svaResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse> transactionResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse> changePassResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse> forgotPassResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse> logoutResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse> allOperatorsResponse = new MutableLiveData<>();

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
