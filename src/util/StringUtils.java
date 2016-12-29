package com.cbscap.util;

import java.util.StringTokenizer;
import java.util.Vector;


/**
 * <p>내용          : 문자열 처리에 필요한 Util Class</p>
 */
public class StringUtils {
    /**
     * array를 Vector 형으로 변환
     * @param array String[]
     * @return Vector
     */
    public static Vector arrayToVector(String[] array) {
        if (array == null) {
            return null;
        }

        Vector vec = new Vector();

        for (int i = 0; i < array.length; i++) {
            vec.add(array[i]);
        }

        return vec;
    }

    /**
     * Vector를 Array로 변환
     * @param vec Vector
     * @return String[]
     */
    public static String[] vectorToArray(Vector vec) {
        if (vec == null) {
            return null;
        }

        String[] array = new String[vec.size()];

        for (int i = 0; i < vec.size(); i++) {
            array[i] = (String) vec.get(i);
        }

        return array;
    }

    /**
     * 따옴표 처리
     * @param original String
     * @return String
     */
    public static String processQuotation(String original) {
        if (original == null) {
            return null;
        } else {
            return replaceAll(original, "'", "''");
        }
    }

    /**
     *
     * @param original String
     * @return String
     */
    public static String processHtmlQuotation(String original) {
        if (original == null) {
            return null;
        }

        String retVal = "";

        for (int i = 0; i < original.length(); i++) {
            char c = original.charAt(i);
            Character cObj = new Character(c);
            Character sq = new Character('\'');
            Character dq = new Character('"');

            if (cObj.compareTo(sq) == 0) {
                Character reverse = new Character('\\');
                retVal += reverse.toString();
                retVal += sq;
            } else if (cObj.compareTo(dq) == 0) {
                Character reverse = new Character('\\');
                retVal += reverse.toString();
                retVal += dq;
            } else {
                retVal += cObj.toString();
            }
        }

        return retVal;
    }

    /**
     * Replaces all the occurences of a substring found within a string by a
     * replacement string
     *
     * @param original the string in where the replace will take place
     * @param find the substring to find and replace
     * @param replacement the replacement string for all occurences of find
     * @return the original string where all the occurences of find are replaced by
     *          replacement
     */
    public static String replaceAll(String original, String find,
        String replacement) {
        StringBuffer buffer = new StringBuffer(original);

        return replaceAll(buffer, find, replacement).toString();

        /*
                int idx = original.length();
                int offset = find.length();

                while( ( idx=original.lastIndexOf(find, idx-1) ) > -1 )
                {
                    buffer.replace(idx,idx+offset, replacement);
                }

                return buffer.toString();
        */
    }

    /**
     * Replaces all the occurences of a substring found
     * within a StringBuffer by a
     * replacement string
     *
     * @param buffer the StringBuffer in where the replace will take place
     * @param find the substring to find and replace
     * @param replacement the replacement string for all occurences of find
     * @return the original StringBuffer where all the
     * occurences of find are replaced by replacement
     */
    public static StringBuffer replaceAll(StringBuffer buffer, String find,
        String replacement) {
        int bufidx = buffer.length() - 1;
        int offset = find.length();

        while (bufidx > -1) {
            int findidx = offset - 1;

            while (findidx > -1) {
                if (bufidx == -1) {
                    //Done
                    return buffer;
                }

                if (buffer.charAt(bufidx) == find.charAt(findidx)) {
                    findidx--; //Look for next char
                    bufidx--;
                } else {
                    findidx = offset - 1; //Start looking again
                    bufidx--;

                    if (bufidx == -1) {
                        //Done
                        return buffer;
                    }

                    continue;
                }
            }

            //Found
            //System.out.println( "replacing from " + (bufidx + 1) +
            //                    " to " + (bufidx + 1 + offset ) +
            //                    " with '" + replacement + "'" );
            buffer.replace(bufidx + 1, bufidx + 1 + offset, replacement);

            //start looking again
        }

        //No more matches
        return buffer;
    }

    /**
      *  Takes an array of tokens and converts into separator-separated string.
      *
      * @param String[] The array of strings input.
      * @param String The string separator.
      * @return String A string containing tokens separated by seperator.
      */
    public static final String arrayToString(String[] array, String separators) {
        StringBuffer sb = new StringBuffer("");
        String empty = "";

        if (array == null) {
            return empty;
        }

        if (separators == null) {
            separators = ",";
        }

        for (int ix = 0; ix < array.length; ix++) {
            if ((array[ix] != null) && !array[ix].equals("")) {
                sb.append(array[ix] + separators);
            }
        }

        String str = sb.toString();

        if (!str.equals("")) {
            str = str.substring(0, (str.length() - separators.length()));
        }

        return str;
    }

    /**
      *  Converts a delimited string into an array of string tokens.
      *
      * @param String[] The 'separator' separated string.
      * @param String The string separator.
      * @return String A string array of the original tokens.
      */
    public static final String[] stringToArray(String str, String separators) {
        StringTokenizer tokenizer;
        String[] array = null;
        int count = 0;

        if (str == null) {
            return array;
        }

        if (separators == null) {
            separators = ",";
        }

        tokenizer = new StringTokenizer(str, separators);

        if ((count = tokenizer.countTokens()) <= 0) {
            return array;
        }

        array = new String[count];

        int ix = 0;

        while (tokenizer.hasMoreTokens()) {
            array[ix] = tokenizer.nextToken();
            ix++;
        }

        return array;
    }

    /**
     * Remove a given set of characters from a String.
     *
     * @param String The input string to be cleansed of 'removeChars'.
     * @param String The characters to be removed.
     * @return String The new string cleansed of 'removeChars'.
     */
    public static String removeChars(String data, String removeChars) {
        String temp = null;
        StringBuffer out = new StringBuffer();
        temp = data;

        StringTokenizer st = new StringTokenizer(temp, removeChars);

        while (st.hasMoreTokens()) {
            String element = (String) st.nextElement();
            out.append(element);
        }

        return out.toString();
    }

    public static String trimString(String str) {
        if (null == str) {
            return null;
        }

        return str.trim();
    }

    public static String fillPkString(String str, int pkLen) {
        if (str == null) {
            return "";
        }

        if (str.length() >= pkLen) {
            return str;
        }

        String temp = "";

        for (int i = 0; i < (pkLen - str.length()); i++) {
            temp += " ";
        }

        return str + temp;
    }
    
    public static String fillPkStringB(String str, int pkLen) {
    	byte [] conv = str.getBytes();
    	int nCnt = conv.length;
    	
    	System.out.println("str::::::" + str);
    	System.out.println("conv::::::" + conv);
    	System.out.println("nCnt::::::" + nCnt);
    	
        if (conv == null) {
            return "";
        }

        if (nCnt >= pkLen) {
            return str;
        }

        String temp = "";

        for (int i = 0; i < (pkLen - nCnt); i++) {
            temp += " ";
        }

        System.out.println("str + temp:::::" + str + temp + "::::");
        return str + temp;
    }

    public static String fillPkNumber(String str, int pkLen) {
        if (str == null) {
            return "";
        }

        if (str.length() >= pkLen) {
            return str;
        }

        String temp = "";

        for (int i = 0; i < (pkLen - str.length()); i++) {
            temp += "0";
        }

        return temp + str;
    }

    public static void main(String[] args) {
        String retVal1 = fillPkString("aaa", 30);
        String retVal2 = fillPkNumber("30", 7);

        System.out.println(retVal1.length() + "  " + retVal1);
        System.out.println(retVal2);
    }
    
public static String fillPkSndNumber(int str, int pkLen) {
      	
    	String strTmpFillZero = new Integer(str).toString();
    	return fillPkNumber(strTmpFillZero,pkLen);
    	
    	}
}
