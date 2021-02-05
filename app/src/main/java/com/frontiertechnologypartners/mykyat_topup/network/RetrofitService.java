package com.frontiertechnologypartners.mykyat_topup.network;

import com.frontiertechnologypartners.mykyat_topup.model.ChangePasswordResponse;
import com.frontiertechnologypartners.mykyat_topup.model.LoginResponse;
import com.frontiertechnologypartners.mykyat_topup.model.Logout;
import com.frontiertechnologypartners.mykyat_topup.model.PreTopUpResponse;
import com.frontiertechnologypartners.mykyat_topup.model.ProvidersResponse;
import com.frontiertechnologypartners.mykyat_topup.model.ResponseMessage;
import com.frontiertechnologypartners.mykyat_topup.model.SVAResponse;
import com.frontiertechnologypartners.mykyat_topup.model.TopUpResponse;
import com.frontiertechnologypartners.mykyat_topup.model.TotalSalesTotalCommissionResponse;
import com.frontiertechnologypartners.mykyat_topup.model.TransactionResponse;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {

    @FormUrlEncoded
    @POST("user/login")
    Single<LoginResponse> getLoginUserData(@Field("username") String username,
                                           @Field("password") String password);

    @FormUrlEncoded
    @POST("providers")
    Single<ProvidersResponse> getProviders(@Field("user_id") int userId);

    @POST("getAllOperators")
    Single<ProvidersResponse> getAllOperators();

    @FormUrlEncoded
    @POST("pretopup")
    Single<PreTopUpResponse> preTopUp(@Field("user_id") String userId,
                                      @Field("provider") String provider,
                                      @Field("mobile") String mobile,
                                      @Field("charge") String amount);

    @FormUrlEncoded
    @POST("topup")
    Single<TopUpResponse> topUp(@Field("user_id") String userId,
                                @Field("provider") String provider,
                                @Field("mobile") String mobile,
                                @Field("charge") String amount);

    @FormUrlEncoded
    @POST("sva")
    Single<SVAResponse> refreshAmount(@Field("user_id") String userId);

    @POST("txns")
    Single<TransactionResponse> transactionHistory(@Body Map<String, Object> body);

    @FormUrlEncoded
    @POST("user/change_password")
    Single<ChangePasswordResponse> changePassword(@Field("password") String password,
                                                  @Field("user_id") int userId,
                                                  @Field("currentpassword") String currentPassword);

    @FormUrlEncoded
    @POST("user/forgot_password")
    Single<ChangePasswordResponse> forgotPassword(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("user/logout")
    Single<Logout> logout(@Field("user_id") int userId);

    @FormUrlEncoded
    @POST("snc")
    Single<TotalSalesTotalCommissionResponse> totalSalesAndCommission(@Field("user_id") int userId);

}