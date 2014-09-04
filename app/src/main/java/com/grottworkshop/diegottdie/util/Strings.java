package com.grottworkshop.diegottdie.util;

/**
 * Created by fgrott on 9/4/2014.
 */
public final class Strings {
    private Strings() {
        // No instances.
    }

    /**
     * Is blank.
     *
     * @param string the string
     * @return the boolean
     */
    public static boolean isBlank(CharSequence string){
        return (string == null || string.toString().trim().length() == 0);
    }

    /**
     * Value or default.
     *
     * @param string the string
     * @param defaultString the default string
     * @return the string
     */
    public static String valueOrDefault(String string, String defaultString) {
        return isBlank(string) ? defaultString : string;
    }

    /**
     * Truncate at.
     *
     * @param string the string
     * @param length the length
     * @return the string
     */
    public static String truncateAt(String string, int length) {
        return string.length() > length ? string.substring(0, length) : string;
    }
}
