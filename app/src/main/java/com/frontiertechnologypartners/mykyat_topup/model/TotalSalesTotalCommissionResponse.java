package com.frontiertechnologypartners.mykyat_topup.model;

import com.google.gson.annotations.SerializedName;

public class TotalSalesTotalCommissionResponse{

	@SerializedName("snc")
	private Snc snc;

	public Snc getSnc(){
		return snc;
	}
}