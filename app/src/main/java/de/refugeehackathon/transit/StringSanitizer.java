package de.refugeehackathon.transit;

import android.support.annotation.NonNull;

public abstract class StringSanitizer {

    private final static String PATTERN_IMG = "<img([\\w\\W]+?)/>";
    private final static String PATTERN_BR = "^(<br><br>)";

    public static String getStringWithoutImg(@NonNull final String input) {
        return input.replaceAll(PATTERN_IMG, "");
    }

    public static String getStringWithoutLeadingBr(@NonNull final String input) {
        return input.replaceAll(PATTERN_BR, "");
    }

    public static String getSanitizeString(@NonNull final String input) {
        String output = getStringWithoutImg(input);
        return getStringWithoutLeadingBr(output);
    }

}
