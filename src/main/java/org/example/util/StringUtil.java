package org.example.util;

/**
 * class for String utils
 */
public class StringUtil {

    public static long convertStringToLong(String str){
        String result = str.replaceAll(" ", "");
        return Long.parseLong(result.substring(0,result.length()-1));
    }
}
