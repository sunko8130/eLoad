package com.frontiertechnologypartners.mykyat_topup.model;

import com.google.gson.annotations.SerializedName;

public class Transaction {

	@SerializedName("realAmount")
	private double realAmount;

	@SerializedName("amount")
	private double amount;

	@SerializedName("commissionRate")
	private double commissionRate;

	@SerializedName("commissionAmount")
	private double commissionAmount;

	@SerializedName("usecase")
	private String usecase;

	@SerializedName("createdDate")
	private long createdDate;

	@SerializedName("balance")
	private double balance;

	@SerializedName("id")
	private int id;

	@SerializedName("topupType")
	private String topupType;

	@SerializedName("userId")
	private int userId;

	@SerializedName("phoneNo")
	private String phoneNo;

	public void setRealAmount(double realAmount){
		this.realAmount = realAmount;
	}

	public double getRealAmount(){
		return realAmount;
	}

	public void setAmount(double amount){
		this.amount = amount;
	}

	public double getAmount(){
		return amount;
	}

	public void setCommissionRate(double commissionRate){
		this.commissionRate = commissionRate;
	}

	public double getCommissionRate(){
		return commissionRate;
	}

	public void setUsecase(String usecase){
		this.usecase = usecase;
	}

	public String getUsecase(){
		return usecase;
	}

	public void setCreatedDate(long createdDate){
		this.createdDate = createdDate;
	}

	public long getCreatedDate(){
		return createdDate;
	}

	public void setBalance(double balance){
		this.balance = balance;
	}

	public double getBalance(){
		return balance;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTopupType(String topupType){
		this.topupType = topupType;
	}

	public String getTopupType(){
		return topupType;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setPhoneNo(String phoneNo){
		this.phoneNo = phoneNo;
	}

	public String getPhoneNo(){
		return phoneNo;
	}

	public double getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(double commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

	@Override
 	public String toString(){
		return 
			"Transaction{" +
			"realAmount = '" + realAmount + '\'' + 
			",amount = '" + amount + '\'' + 
			",commissionRate = '" + commissionRate + '\'' + 
			",usecase = '" + usecase + '\'' + 
			",createdDate = '" + createdDate + '\'' + 
			",balance = '" + balance + '\'' + 
			",id = '" + id + '\'' + 
			",topupType = '" + topupType + '\'' + 
			",userId = '" + userId + '\'' + 
			",phoneNo = '" + phoneNo + '\'' + 
			"}";
		}
}