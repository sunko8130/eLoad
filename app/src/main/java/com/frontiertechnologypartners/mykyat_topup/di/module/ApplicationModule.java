package com.frontiertechnologypartners.mykyat_topup.di.module;


import com.frontiertechnologypartners.mykyat_topup.network.RetrofitService;
import com.frontiertechnologypartners.mykyat_topup.repository.NetworkRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.frontiertechnologypartners.mykyat_topup.util.Constant.BASE_URL;
import static com.frontiertechnologypartners.mykyat_topup.util.Constant.CONNECTION_TIMEOUT;

@Module(includes = {ViewModelModule.class})
public class ApplicationModule {

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    static RetrofitService provideRetrofitService(Retrofit retrofit) {
        return retrofit.create(RetrofitService.class);
    }


    @Singleton
    @Provides
    static NetworkRepository provideNetworkRepository(RetrofitService retrofitService) {
        return new NetworkRepository(retrofitService);
    }

}
