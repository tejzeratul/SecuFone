package setting;

/**
 * Created by Tejas on 4/20/2016.
 */
public class TempConfigFIle {
    public static final String hostName="http://23.22.78.172:80";
    public static String hostNameForLogin=hostName+"/secufone/user?action=tryLogin";
    public static String hostNameForTest=hostName+"/secufone/PerformTest?action=tryTest";
    public static String hostNameForScore=hostName+"/secufone/PerformTest?action=tryScore";
    public static String hostNameForRegister=hostName+"/secufone/user?action=tryRegister";
}
