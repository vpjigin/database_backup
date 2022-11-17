package in.solocrew.utils;

public class Utils {

    public static String quote(String s) {
        return new StringBuilder()
                .append('\'')
                .append(s)
                .append('\'')
                .toString();
    }

}
