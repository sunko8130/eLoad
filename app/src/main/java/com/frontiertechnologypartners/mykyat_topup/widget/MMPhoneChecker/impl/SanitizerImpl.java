package com.frontiertechnologypartners.mykyat_topup.widget.MMPhoneChecker.impl;


import com.frontiertechnologypartners.mykyat_topup.widget.MMPhoneChecker.Sanitizer;

public class SanitizerImpl implements Sanitizer {

	private static final String COUNTRY_CODE = "^(\\+?950?9\\d+)$";

	private static final String ZERO_BEFORE_AREACODE = "^(\\+?9509\\d{7,9})$";
	
	private static final String DOUBLE_COUNTRY_CODE = "^(\\+?95950?9\\d{7,9})$";

	public String normalize(String phoneNumber) {

		if (phoneNumber == null) {
			throw new NullPointerException();
		}

		phoneNumber = phoneNumber.trim();

		// Remove White Space, Spacees and Dash
		phoneNumber = phoneNumber.replaceAll("[\\s-)(]", "");

		if (phoneNumber.matches(COUNTRY_CODE)) {			
			// Remove double country code			
			if (phoneNumber.matches(DOUBLE_COUNTRY_CODE)) {
				phoneNumber = phoneNumber.replace("9595", "95");
			}

			// remove 0 before area code
			if (phoneNumber.matches(ZERO_BEFORE_AREACODE)) {
				phoneNumber = phoneNumber.replace("9509", "959");
			}
		}

		return phoneNumber;
	}

}
