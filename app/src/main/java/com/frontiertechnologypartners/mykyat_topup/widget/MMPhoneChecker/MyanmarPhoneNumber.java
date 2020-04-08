package com.frontiertechnologypartners.mykyat_topup.widget.MMPhoneChecker;

import com.frontiertechnologypartners.mykyat_topup.widget.MMPhoneChecker.exception.InvalidMyanmarTelecomNameException;
import com.frontiertechnologypartners.mykyat_topup.widget.MMPhoneChecker.impl.MyanmarPhoneNumberValidatorImpl;
import com.frontiertechnologypartners.mykyat_topup.widget.MMPhoneChecker.impl.SanitizerImpl;

public class MyanmarPhoneNumber {

	private Sanitizer sanitizer;

	private MyanmarPhoneNumberValidator validator;

	public MyanmarPhoneNumber() {
		this.sanitizer = new SanitizerImpl();
		this.validator = new MyanmarPhoneNumberValidatorImpl(sanitizer);
	}

	public String getTelecomName(String phoneNumber) {
		return getTelecomName(phoneNumber, true);
	}
	
	private String getTelecomName(String phoneNumber, boolean sanitize) {

		if (phoneNumber == null) {
			throw new NullPointerException();
		}

		String telecomName = "Unknown";

		if (sanitize) {
			phoneNumber = sanitizer.normalize(phoneNumber);
		}
		
		if (validator.isMyanmarPhoneNumber(phoneNumber, false)) {
			for (MyanmarTelecomsOperator myanmarTelecomsOperator : MyanmarTelecomsOperator.values()) {
				if (phoneNumber.matches(myanmarTelecomsOperator.getRegEx())) {
					telecomName = myanmarTelecomsOperator.getName();
					break;
				}
			}
		}

		return telecomName;
	}
	
	public boolean isTelecomPhoneNumber(String phoneNumber, String telecomOperatorName)
			throws InvalidMyanmarTelecomNameException {
		return isTelecomPhoneNumber(phoneNumber, telecomOperatorName, true);
	}

	private boolean isTelecomPhoneNumber(String phoneNumber, String telecomOperatorName, boolean sanitize)
			throws InvalidMyanmarTelecomNameException {

		if (telecomOperatorName == null || phoneNumber == null) {
			throw new NullPointerException();
		}

		if (sanitize) {
			phoneNumber = sanitizer.normalize(phoneNumber);
		}

		MyanmarTelecomsOperator myanmarTelecomsOperator = MyanmarTelecomsOperator
				.getMyanmarTelecomsOperator(telecomOperatorName);
		return phoneNumber.matches(myanmarTelecomsOperator.getRegEx());
	}

}
