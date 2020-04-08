package com.frontiertechnologypartners.mykyat_topup.model;


import com.google.gson.annotations.SerializedName;

public class Operators {

	@SerializedName("id")
	private int id;

	@SerializedName("operator")
	private String operator;

	@SerializedName("status")
	private int status;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setOperator(String operator){
		this.operator = operator;
	}

	public String getOperator(){
		return operator;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Operators{" +
			"id = '" + id + '\'' + 
			",operator = '" + operator + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}