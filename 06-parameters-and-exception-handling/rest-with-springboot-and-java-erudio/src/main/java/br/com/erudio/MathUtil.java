package br.com.erudio;

public class MathUtil {

    public static boolean isNumeric(String strNumber) {
        if (strNumber == null) return false;
        strNumber = strNumber.replaceAll(",", ".");
        return strNumber.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public static Double convertToDouble(String strNumber) {
        strNumber = strNumber.replaceAll(",", ".");
        return Double.parseDouble(strNumber);
    }
}
