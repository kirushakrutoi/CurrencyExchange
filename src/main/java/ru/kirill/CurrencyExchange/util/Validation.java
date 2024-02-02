package ru.kirill.CurrencyExchange.util;

public class Validation {

    public static boolean isValidCurrencyParam(String code, String fullName, String sign) {
        if (code == null || fullName == null || sign == null)
            return false;
        if (code.isEmpty() || fullName.isEmpty() || sign.isEmpty())
            return false;
        if (code.length() != 3 || fullName.length() > 100 || sign.length() > 1)
            return false;

        return true;
    }

    public static boolean isValidCode(String code){
        if(code == null)
            return false;
        if(code.isEmpty())
            return false;
        if(code.length() != 3)
            return false;

        return true;
    }

    public static boolean isValidExchangeRateCurrencyParam(String param){
        if(param == null || param.length() != 6)
            return false;
        return true;
    }

    public static boolean isValidExchangeRatesParam(String baseCode, String targetCode, String rate) {
        if (baseCode == null || targetCode == null || rate == null)
            return false;
        if (baseCode.isEmpty() || targetCode.isEmpty() || rate.isEmpty())
            return false;
        if (baseCode.length() != 3 || targetCode.length() != 3)
            return false;

        return true;
    }

    public static boolean isValidExchangeParam(String baseCode, String targetCode, String amount) {
        if (baseCode == null || targetCode == null || amount == null)
            return false;
        if (baseCode.isEmpty() || targetCode.isEmpty() || amount.isEmpty())
            return false;
        if (baseCode.length() != 3 || targetCode.length() != 3)
            return false;

        return true;
    }

}
