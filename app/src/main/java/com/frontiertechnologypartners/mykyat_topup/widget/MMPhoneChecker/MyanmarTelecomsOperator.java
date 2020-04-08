package com.frontiertechnologypartners.mykyat_topup.widget.MMPhoneChecker;

import com.frontiertechnologypartners.mykyat_topup.widget.MMPhoneChecker.exception.InvalidMyanmarTelecomNameException;

enum MyanmarTelecomsOperator {

    OOREDOO("Ooredoo", "^(09|\\+?959)9(5|7|6)\\d{7}$"),
    TELENOR("Telenor", "^(09|\\+?959)7(9|8|7|6|5)\\d{7}$"),
    MYTEL("Mytel", "^(09|\\+?959)6\\d{8}$"),
    MPT("MPT", "^(09|\\+?959)(5\\d{0,6}|4\\d{0,8}|2\\d{0,8}|3\\d{0,8}|6\\d{0,6}|8\\d{0,6}|7(0|1|2|3)\\d{6}|9(0|1|9)\\d{0,6}|2[0-4]\\d{0,5}|5[0-6]\\d{0,5}|8[13-7]\\d{0,5}|3[0-369]\\d{0,6}|34\\d{0,7}|4[1379]\\d{0,6}|73\\d{0,6}|91\\d{0,6}|25\\d{0,7}|26[0-5]\\d{0,6}|40[0-4]\\d{0,6}|42\\d{0,7}|45\\d{0,7}|89[6789]\\d{0,6}|)$");

    private String name;

    private String regEx;

    MyanmarTelecomsOperator(String name, String regEx) {
        this.name = name;
        this.regEx = regEx;
    }

    public String getName() {
        return name;
    }

    public String getRegEx() {
        return regEx;
    }

    public static MyanmarTelecomsOperator getMyanmarTelecomsOperator(String telecomOperatorName) throws InvalidMyanmarTelecomNameException {
        MyanmarTelecomsOperator telecomsOperator = null;
        for (MyanmarTelecomsOperator operator : MyanmarTelecomsOperator.values()) {
            if (operator.name.equalsIgnoreCase(telecomOperatorName)) {
                telecomsOperator = operator;
                break;
            }
        }
        if (telecomsOperator == null) {
            throw new InvalidMyanmarTelecomNameException();
        } else {
            return telecomsOperator;
        }
    }
}
