package utils;

public class Credentials {
    public static String ghToken = "";

    public static void setGhToken(String token) {
        ghToken = token;
    }
    public static String getGhToken() {
        return ghToken;
    }
}
