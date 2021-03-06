package one.xingyi.restAnnotations.utils;
import java.util.Arrays;
import java.util.List;
public class Strings {
public static    List<String> useIf(boolean b, String string) { return b ? Arrays.asList(string) : Arrays.asList();}

    public static final String[] empty = new String[0];
    public static String quote(String s) { return "\"" + s + "\"";}

    public static String removeOptionalFirst(String first, String value) {
        if (value.startsWith(first))
            return value.substring(first.length());
        else return value;
    }

    public static String removeOptionalLast(String last, String value) {
        if (value.endsWith(last))
            return value.substring(0, value.length() - last.length());
        return value;
    }

    public static String extractFromOptionalEnvelope(String start, String end, String s) {
        int startIndex = s.indexOf(start);
        int endOfStart = startIndex + start.length() + 1;
        int endIndex = s.indexOf(end, endOfStart);
        if (startIndex == -1 || endIndex == -1)
            return s;
        return s.substring(endOfStart, endIndex);
    }

    public static String lastSegement(String separator, String s) {
        String[] split = s.split(separator);
        return split.length == 0 ? "" : split[split.length - 1];
    }
    public static String allButLastSegment(String separator, String s) {
        int index = s.lastIndexOf(separator);
        if (index == -1) return "";
        else return s.substring(0, index);
    }
    public static String firstLetterUppercase(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
