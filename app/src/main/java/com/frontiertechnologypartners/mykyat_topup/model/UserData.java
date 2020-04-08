package com.frontiertechnologypartners.mykyat_topup.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserData implements Serializable {

	@SerializedName("createdDate")
	private long createdDate;

	@SerializedName("balance")
	private double balance;

	@SerializedName("id")
	private int id;

	@SerializedName("updatedDate")
	private long updatedDate;

	@SerializedName("user")
	private User user;

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

	public void setUpdatedDate(long updatedDate){
		this.updatedDate = updatedDate;
	}

	public long getUpdatedDate(){
		return updatedDate;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	@Override
 	public String toString(){
		return 
			"UserData{" +
			"createdDate = '" + createdDate + '\'' + 
			",balance = '" + balance + '\'' + 
			",id = '" + id + '\'' + 
			",updatedDate = '" + updatedDate + '\'' + 
			",user = '" + user + '\'' + 
			"}";
		}
}