package com.frontiertechnologypartners.mykyat_topup.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TransactionResponse{

	@SerializedName("status")
	private ResponseMessage responseMessage;

	@SerializedName("data")
	private List<Transaction> data;

	public void setData(List<Transaction> data){
		this.data = data;
	}

	public List<Transaction> getData(){
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
			"TransactionResponse{" + 
			"data = '" + data + '\'' + 
			",status = '" + responseMessage + '\'' +
			"}";
		}
}