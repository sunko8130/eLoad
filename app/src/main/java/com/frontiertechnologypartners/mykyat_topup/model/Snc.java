package com.frontiertechnologypartners.mykyat_topup.model;

import com.google.gson.annotations.SerializedName;

public class Snc{

	@SerializedName("totalTodayCommissions")
	private double totalTodayCommissions;

	@SerializedName("totalTodaySales")
	private double totalTodaySales;

	public double getTotalTodayCommissions(){
		return totalTodayCommissions;
	}

	public double getTotalTodaySales(){
		return totalTodaySales;
	}
}