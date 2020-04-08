package com.frontiertechnologypartners.mykyat_topup.network;

import com.frontiertechnologypartners.mykyat_topup.model.ResponseMessage;
import com.frontiertechnologypartners.mykyat_topup.model.Status;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.frontiertechnologypartners.mykyat_topup.model.Status.ERROR;
import static com.frontiertechnologypartners.mykyat_topup.model.Status.LOADING;
import static com.frontiertechnologypartners.mykyat_topup.model.Status.SUCCESS;


public class ApiResponse<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final Throwable message;

    private ApiResponse(@NonNull Status status, @Nullable T data,
                        @Nullable Throwable message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }


    public static <T> ApiResponse<T> success(@NonNull T data) {
        return new ApiResponse<>(SUCCESS, data, null);
    }


    public static <T> ApiResponse<T> error(Throwable msg) {
        return new ApiResponse<>(ERROR, null, msg);
    }

    public static <T> ApiResponse<T> loading() {
        return new ApiResponse<>(LOADING, null, null);
    }
    
}