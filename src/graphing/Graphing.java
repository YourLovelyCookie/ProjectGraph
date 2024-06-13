package graphing;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Graphing {

    public static String funcPrefix = "f (x) = ";
    public static String pointPrefix = "P (";
    public static String pointSuffix = ")";

    public static String inputNaming(String val, int type) { // Check if "val" only contains characters that are allowed (e.g. 0-9, "x", "+", "-", "/", "*", "^", ".") and remove what is not allowed (e.g. iterate through every character and check if it is alright)
        StringBuilder newVal = new StringBuilder();
        String t_val = val;
        //t_val.replace(funcPrefix, "").replace(pointPrefix, "").replace(pointSuffix, "");
        t_val = t_val.replace("(x)", "");
        if (type == PopUp.typeCE.FUNCTION.ordinal()) {
            String[] allowedCharsA = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "x", "X", "+", "-", "/", "*", "^" };
            String[] spacingCharsA = { "+", "-", "/", "*"};
            Set<String> allowedCharsS = new HashSet<String>(Arrays.asList(allowedCharsA));
            Set<String> spacingCharsS = new HashSet<String>(Arrays.asList(spacingCharsA));

            t_val = t_val.replace(" ", "");
            for (int i = 0; i < t_val.length(); i++) {
                if (allowedCharsS.contains(String.valueOf(t_val.charAt(i)))) {
                    if (spacingCharsS.contains(String.valueOf(t_val.charAt(i)))) {
                        newVal.append(" ").append(t_val.charAt(i)).append(" ");
                    } else {
                        newVal.append(t_val.charAt(i));
                    }
                }
            }
            if (newVal.isEmpty()) return "ERROR";


            return funcPrefix + newVal;
    }
    else if (type == PopUp.typeCE.POINT.ordinal()) { // remove all double spaces and then add them to certain characters (e.g. " + ")
        String[] allowedCharsA = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ".", ";", "," };
        String[] spacingCharsA = { ":", ",", ";", "|"};
        Set<String> allowedCharsS = new HashSet<String>(Arrays.asList(allowedCharsA));
        Set<String> spacingCharsS = new HashSet<String>(Arrays.asList(spacingCharsA));

        t_val = t_val.replace(" ", "");
        for (int i = 0; i < t_val.length(); i++) {
            if (allowedCharsS.contains(String.valueOf(t_val.charAt(i)))) {
                if (spacingCharsS.contains(String.valueOf(t_val.charAt(i)))) {
                    newVal.append(";");
                } else {
                    newVal.append(t_val.charAt(i));
                }
            }
        }
        String newValFinal = newVal.toString();
        if(newValFinal.split(";").length >= 2 && !newValFinal.split(";")[0].isEmpty() && !newValFinal.split(";")[1].isEmpty()) {
            newValFinal = newValFinal.split(";")[0] + "; " + newValFinal.split(";")[1];

            return pointPrefix + newValFinal + pointSuffix;
        }
    }

        return "ERROR";
    }

    public static int detectType (String val) {
        if (val.startsWith(funcPrefix)) return PopUp.typeCE.FUNCTION.ordinal();
        if (val.startsWith(pointPrefix) && val.endsWith(pointSuffix)) return PopUp.typeCE.POINT.ordinal();

        return -1;
    }

    public static String removePreASuf (String val, int type) {
        int t_type = -1;
        if (type == -1) {
            t_type = detectType(val);
        }

        if (t_type == PopUp.typeCE.FUNCTION.ordinal()) return val.substring(funcPrefix.length());
        if (t_type == PopUp.typeCE.POINT.ordinal()) return val.substring(pointPrefix.length()).substring(0, val.substring(pointPrefix.length()).length() - pointSuffix.length());

        return val;
    }
    public static String removePreASuf (String val) { return removePreASuf(val, -1); }

}
