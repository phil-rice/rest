package one.xingyi.restAnnotations;
public class Strings {

    public static String removeOptionalFirst(String first, String value) {
        if (value.startsWith(first))
            return value.substring(first.length());
        else return value;
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
