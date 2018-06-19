package api;

public class ConfigAPI {

    private static String ipEmulador = "10.0.2.2";
    private static String ip ="10.51.67.49";
    public static String BASE_URL = "http://"+ipEmulador+":8080/";
    public static String getIp() {
        return ip;
    }

}
