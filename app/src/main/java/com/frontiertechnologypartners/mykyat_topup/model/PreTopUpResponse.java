package com.frontiertechnologypartners.mykyat_topup.model;

import com.google.gson.annotations.SerializedName;

public class PreTopUpResponse {

	@SerializedName("data")
	private double data;

	@SerializedName("status")
	private ResponseMessage responseMessage;

	public void setData(double data){
		this.data = data;
	}

	public double getData(){
		return data;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	@Override
 	public String toString(){
		return 
			"PreTopUpResponse{" + 
			"data = '" + data + '\'' + 
			",status = '" + responseMessage + '\'' +
			"}";
		}
}