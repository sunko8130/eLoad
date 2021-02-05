package com.frontiertechnologypartners.mykyat_topup.model;

import com.google.gson.annotations.SerializedName;

public class Operators{

	@SerializedName("percentage")
	private double percentage;

	@SerializedName("operatorId")
	private int operatorId;

	@SerializedName("operatorName")
	private String operatorName;

	@SerializedName("status")
	private int status;

	public void setPercentage(double percentage){
		this.percentage = percentage;
	}

	public double getPercentage(){
		return percentage;
	}

	public void setOperatorId(int operatorId){
		this.operatorId = operatorId;
	}

	public int getOperatorId(){
		return operatorId;
	}

	public void setOperatorName(String operatorName){
		this.operatorName = operatorName;
	}

	public String getOperatorName(){
		return operatorName;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}
}