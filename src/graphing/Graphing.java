package graphing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Graphing {

    public static String inputNaming(String val, int type) { // Check if "val" only contains characters that are allowed (e.g. 0-9, "x", "+", "-", "/", "*", "^", ".") and remove what is not allowed (e.g. iterate through every character and check if it is alright)
        String[] allowedCharsA = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "x", "X", "-", "/", "*", "^", "."};
        Set<String> allowedCharsS = new HashSet<String>(Arrays.asList(allowedCharsA));

        StringBuilder newVal = new StringBuilder();
        String t_val = val;
        t_val = t_val.replace(" ", "");
        for(int i = 0; i < t_val.length(); i++) {
            if(allowedCharsS.contains(String.valueOf(t_val.charAt(i)))) {
                newVal.append(t_val.charAt(i));
            }
        }

        if(newVal.isEmpty()) return "ERROR";

        if(type == PopUp.typeCE.FUNCTION.ordinal()) return "f (x) = " + newVal; // remove all spaces and then add them to certain characters (e.g. " + ")
        if(type == PopUp.typeCE.POINT.ordinal())return "a (" + newVal + ")";

        return "ERROR";
    }

}
