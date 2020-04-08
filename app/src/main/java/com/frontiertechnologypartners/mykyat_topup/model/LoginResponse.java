package com.frontiertechnologypartners.mykyat_topup.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse{

	@SerializedName("data")
	private UserData userData;

	@SerializedName("status")
	private ResponseMessage responseMessage;

	public void setUserData(UserData userData){
		this.userData = userData;
	}

	public UserData getUserData(){
		return userData;
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
			"LoginResponse{" + 
			"userData = '" + userData + '\'' +
			",responseMessage = '" + responseMessage + '\'' +
			"}";
		}
}