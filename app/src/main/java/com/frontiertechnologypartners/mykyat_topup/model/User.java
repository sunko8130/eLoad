package com.frontiertechnologypartners.mykyat_topup.model;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("firstName")
	private String firstName;

	@SerializedName("lastName")
	private String lastName;

	@SerializedName("password")
	private String password;

	@SerializedName("createdDate")
	private long createdDate;

	@SerializedName("id")
	private int id;

	@SerializedName("userType")
	private String userType;

	@SerializedName("updatedDate")
	private long updatedDate;

	@SerializedName("email")
	private String email;

	@SerializedName("username")
	private String username;

	@SerializedName("status")
	private int status;

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setCreatedDate(long createdDate){
		this.createdDate = createdDate;
	}

	public long getCreatedDate(){
		return createdDate;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setUserType(String userType){
		this.userType = userType;
	}

	public String getUserType(){
		return userType;
	}

	public void setUpdatedDate(long updatedDate){
		this.updatedDate = updatedDate;
	}

	public long getUpdatedDate(){
		return updatedDate;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
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
			"User{" + 
			"firstName = '" + firstName + '\'' + 
			",lastName = '" + lastName + '\'' + 
			",password = '" + password + '\'' + 
			",createdDate = '" + createdDate + '\'' + 
			",id = '" + id + '\'' + 
			",userType = '" + userType + '\'' + 
			",updatedDate = '" + updatedDate + '\'' + 
			",email = '" + email + '\'' + 
			",username = '" + username + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}