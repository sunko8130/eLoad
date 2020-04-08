package com.frontiertechnologypartners.mykyat_topup.model;

import com.google.gson.annotations.SerializedName;

public class SVAResponse{

	@SerializedName("sva")
	private int sva;

	public void setSva(int sva){
		this.sva = sva;
	}

	public int getSva(){
		return sva;
	}

	@Override
 	public String toString(){
		return 
			"SVAResponse{" + 
			"sva = '" + sva + '\'' + 
			"}";
		}
}