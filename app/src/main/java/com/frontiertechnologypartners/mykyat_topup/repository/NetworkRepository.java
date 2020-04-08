package com.frontiertechnologypartners.mykyat_topup.repository;

import com.frontiertechnologypartners.mykyat_topup.model.ChangePasswordResponse;
import com.frontiertechnologypartners.mykyat_topup.model.LoginResponse;
import com.frontiertechnologypartners.mykyat_topup.model.Logout;
import com.frontiertechnologypartners.mykyat_topup.model.PreTopUpResponse;
import com.frontiertechnologypartners.mykyat_topup.model.ProvidersResponse;
import com.frontiertechnologypartners.mykyat_topup.model.ResponseMessage;
import com.frontiertechnologypartners.mykyat_topup.model.SVAResponse;
import com.frontiertechnologypartners.mykyat_topup.model.TopUpResponse;
import com.frontiertechnologypartners.mykyat_topup.model.TransactionResponse;
import com.frontiertechnologypartners.mykyat_topup.network.RetrofitService;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;

public class NetworkRepository {

    private RetrofitService retrofitService;

    @Inject
    public NetworkRepository(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    public Single<LoginResponse> getLoginUserData(String username, String password) {
        return retrofitService.getLoginUserData(username, password);
    }

    public Single<ProvidersResponse> getProviders() {
        return retrofitService.getProviders();
    }

    public Single<ProvidersResponse> getAllOperators() {
        return retrofitService.getAllOperators();
    }

    public Single<PreTopUpResponse> preTopUp(String userId, String provider, String mobile, String amount) {
        return retrofitService.preTopUp(userId, provider, mobile, amount);
    }

    public Single<TopUpResponse> topUp(String userId, String provider, String mobile, String amount) {
        return retrofitService.topUp(userId, provider, mobile, amount);
    }

    public Single<SVAResponse> refreshAmount(String userId) {
        return retrofitService.refreshAmount(userId);
    }

    public Single<TransactionResponse> transactionHistory(Map<String, Object> body) {
        return retrofitService.transactionHistory(body);
    }

    public Single<ChangePasswordResponse> changePassword(String password, int userId, String currentPassword) {
        return retrofitService.changePassword(password, userId, currentPassword);
    }

    public Single<Logout> logout(int userId) {
        return retrofitService.logout(userId);
    }

    public Single<ChangePasswordResponse> forgotPassword(String mobile) {
        return retrofitService.forgotPassword(mobile);
    }
}
