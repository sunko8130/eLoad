package com.frontiertechnologypartners.mykyat_topup.model;

import com.google.gson.annotations.SerializedName;

public class Logout {


    @SerializedName("status")
    private ResponseMessage responseMessage;

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public String toString() {
        return
                "TopUpResponse{" +
                        "status = '" + responseMessage + '\'' +
                        "}";
    }
}
