package com.frontiertechnologypartners.mykyat_topup.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProvidersResponse{

	@SerializedName("data")
	private List<Operators> data;

	@SerializedName("status")
	private ResponseMessage responseMessage;

	public void setData(List<Operators> data){
		this.data = data;
	}

	public List<Operators> getData(){
		return data;
	}

	public void setResponseMessage(ResponseMessage responseMessage){
		this.responseMessage = responseMessage;
	}

	public ResponseMessage getResponseMessage(){
		return responseMessage;
	}

	@Override
 	public String toString(){
		return 
			"ProvidersResponse{" + 
			"data = '" + data + '\'' + 
			",status = '" + responseMessage + '\'' +
			"}";
		}
}