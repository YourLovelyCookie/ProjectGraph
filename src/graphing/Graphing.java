package graphing;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
        //t_val.replace(funcPrefix, "").replace(pointPrefix, "").replace(pointSuffix, "");          // Change it  \/
        t_val = t_val.replace("(x)", "");                                         // Change it  /\
        if (type == PopUp.typeCE.FUNCTION.ordinal()) {
            String[] allowedCharsA = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "x", "X", "+", "-", "/", "*", "^", "(", ")"  };
            String[] spacingCharsA = { "+", "-", "/", "*"};
            Set<String> allowedCharsS = new HashSet<String>(Arrays.asList(allowedCharsA));
            Set<String> spacingCharsS = new HashSet<String>(Arrays.asList(spacingCharsA));

            t_val = t_val.replace(" ", "");
            //if (t_val.startsWith("-")) t_val = t_val.substring(1);
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
        String[] allowedCharsA = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ".", ";", ","};
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
        int t_type = type;
        if (type == -1) {
            t_type = detectType(val);
        }

        if (t_type == PopUp.typeCE.FUNCTION.ordinal()) return val.substring(funcPrefix.length());
        if (t_type == PopUp.typeCE.POINT.ordinal()) return val.substring(pointPrefix.length()).substring(0, val.substring(pointPrefix.length()).length() - pointSuffix.length());

        return val;
    }
    public static String removePreASuf (String val) { return removePreASuf(val, -1); }


    public static float calculate (String input, float xVal) { // remove german comments !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! FUNKTIONIERT NICHT
        int t_type = detectType(input); 
        String calc = removePreASuf(input, t_type);
        String[] calcCharsA = {"^", "/", "*", "-", "+"};
        String[] calcCharsEA = {"^", "/", "*"}; // E = Extra
        Set<String> calcCharS = new HashSet<String>(Arrays.asList(calcCharsA));
        Set<String> calcCharES = new HashSet<String>(Arrays.asList(calcCharsEA)); // E = Extra

        calc = calc.replace(" ", "").replace("x", "X").replace("X", String.valueOf(xVal));

        // Check for brackets
        for (int i = 0; i < calc.length(); i++) { // is working
            int fb = -1; // first bracket
            int sb = -1; // second bracket
            if (String.valueOf(calc.charAt(i)).equals("(")) {
                fb = i;
                for (int j = calc.length()-1; j > 0; j--) {
                    if (String.valueOf(calc.charAt(j)).equals(")")) {
                        sb = j;
                        break;
                    }
                }
                calc = (calc.substring(0, fb) + calculate(calc.substring(fb+1, sb), 0) + calc.substring(sb+1));
                break;
            }
        }
        calc = calc.replace("++", "+").replace("+-", "-").replace("-+", "-").replace("--", "+");
        // Split the elements
        ArrayList<String> t_elements = new ArrayList<>();
        int fp = 0;  // first position
        for (int i = 1; i < calc.length(); i++) { // is working
            if (calcCharS.contains(String.valueOf(calc.charAt(i))) || i == calc.length()-1) {
                t_elements.add(calc.substring(fp, i == calc.length()-1 ? i+1 : i));
                if (calcCharES.contains(String.valueOf(calc.charAt(i)))) {
                    fp = i+1;
                    t_elements.add(String.valueOf(calc.charAt(i)));
                } else {
                    fp = i;
                }
            }
        }

        // Remove the empty elements
        t_elements.removeIf(String::isEmpty);

        // Calculate the Extra operations
        for (String t_calcChar : calcCharsEA) { // is working
            for (int i = 0; i < t_elements.size(); i++) {
                if (t_elements.get(i).equals(t_calcChar)) {
                    float t_res = calculateOperation(Float.parseFloat(t_elements.get(i-1)), Float.parseFloat(t_elements.get(i+1)), t_elements.get(i));
                    t_elements.subList(i-1, i+2).clear();
                    t_elements.add(i-1, String.valueOf(t_res));
                    i--;
                }
            }
        }

        // Add each element
        for (int i = 1; i < t_elements.size(); i++) {
            float t_res = calculateOperation(Float.parseFloat(t_elements.get(i-1)), Float.parseFloat(t_elements.get(i)), "+");
            t_elements.subList(i-1, i+1).clear();
            t_elements.add(i-1, String.valueOf(t_res));
            i--;
        }
        calc = t_elements.getFirst();

        // IGNORE THIS CODE BELOW -----------------------
        /*for (int i = 0; i < calcCharS.size(); i++) {
            boolean found = false;
            int ff = 0;
            int fs = -1;
            int sf = -1;
            int ss = calc.length()-1;
            for (int j = 0; j < calc.length(); j++) {
                //if (!calcCharsA[i].equals("*")) break;
                if (!found) {
                    if (calcCharsA[i].equals(String.valueOf(calc.charAt(j)))) {
                        found = true;
                        fs = j-1;
                        sf = j+1;
                    } else if (calcCharS.contains(String.valueOf(calc.charAt(j)))) {
                        ff = j+1;
                    }
                } else {
                    if (calcCharS.contains(String.valueOf(calc.charAt(j))) || j == calc.length()-1) {
                        ss = j == calc.length()-1 ? j : j-1;

                        // CALCULATE HERE!!
                        float fF = Float.parseFloat(calc.substring(ff, fs+1));
                        float sF = Float.parseFloat(calc.substring(sf, ss+1));
                        switch (calcCharsA[i]) {
                            case "^": // start with ^
                                calc = calc.substring(0, ff) + (Math.pow(fF, sF)) + calc.substring(ss+1);
                                break;
                            case "/": // start with ^
                                calc = calc.substring(0, ff) + fF/sF + calc.substring(ss+1);
                                break;
                            case "*": // start with ^
                                calc = calc.substring(0, ff) + fF*sF + calc.substring(ss+1);
                                break;
                            case "-":
                                calc = calc.substring(0, ff) + (fF-sF) + calc.substring(ss+1);
                                break;
                            case "+":
                                calc = calc.substring(0, ff) + (fF+sF) + calc.substring(ss+1);
                                break;
                        }
                        i = 0;
                        break;

                    }
                }
            }
        }*/
        return Float.parseFloat(calc);
    }

    public static float calculateOperation (float a, float b, String operation) {
        return switch (operation) {
            case "^" -> (float) Math.pow(a, b);
            case "/" -> a / b;
            case "*" -> a * b;
            case "+" -> a + b;
            case "-" -> a - b;
            default -> 0;
        };
    }

}




















/*for (int i = 0; i < calcCharS.size(); i++) {
    int ffNo = -1; // f = first, s = second
    int fsNo = -1; // 1 nach diesem ist ein zeichen
    int sfNo = -1; // 1 vor diesem ist ein zeichen
    int ssNo = -1;
    for(int j = 0; j < calc.length(); j++) {
        if(ssNo != -1) {

        } else if (!calcCharS.contains(String.valueOf(calc.charAt(j)))) {
            if (ffNo == -1) ffNo = j;
            if ()
        }
    }
}*/