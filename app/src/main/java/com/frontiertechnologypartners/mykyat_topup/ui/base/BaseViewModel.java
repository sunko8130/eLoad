package com.frontiertechnologypartners.mykyat_topup.ui.base;


import com.frontiertechnologypartners.mykyat_topup.network.ApiResponse;
import java.io.IOException;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.NO_INTERNET_CONNECTION;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.UNKNOWN_ERROR;

public class BaseViewModel extends ViewModel {

    protected final CompositeDisposable disposable = new CompositeDisposable();
    public final MutableLiveData<ApiResponse<?>> loginResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> providersResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> preTopUpResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> topUpResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> svaResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> transactionResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> changePassResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> forgotPassResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> logoutResponse = new MutableLiveData<>();
    public final MutableLiveData<ApiResponse<?>> allOperatorsResponse = new MutableLiveData<>();

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    protected String getErrorMessage(Throwable errorMessage) {
        if (errorMessage instanceof HttpException) {
            return errorMessage.getMessage();
        } else if (errorMessage instanceof IOException) {
            return NO_INTERNET_CONNECTION;
        } else {
            return UNKNOWN_ERROR;
        }
    }
}
