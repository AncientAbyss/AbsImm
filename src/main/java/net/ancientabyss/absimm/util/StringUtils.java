package net.ancientabyss.absimm.util;

import java.util.List;

public abstract class StringUtils {

    private StringUtils() {}

    // Apache Commons Lang seems unsupported in Android 4.1, so reimplemented here :/
    public static String join(List<String> list, String separator) {
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            if (builder.length() > 0) builder.append(separator);
            builder.append(str);
        }
        return builder.toString();
    }
}
