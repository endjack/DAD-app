package api;

public class ConfigAPI {

    private static String ipEmulador = "10.0.2.2";
    private static String ip ="192.168.0.2";
    public static String BASE_URL = "http://"+ip+":8080/";


    public static String getIp() {
        return ip;
    }

}
