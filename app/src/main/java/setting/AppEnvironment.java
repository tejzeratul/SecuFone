package setting;

import java.util.Date;

/**
 * Created by Tejas on 3/31/2016.
 */
public class AppEnvironment {

    public final static String PARENT_FOLDER = "SecuFone";
    public final static String FILE_EXTENSION = ".txt";
    public final static String DEF_ANDROID_ID ="0000";

    public final static int DEF_HTTP_TIMEOUT =25000;

    private static int apiLevel;   // API level of device

    private static String userDeviceName="test1";  // device name visible to user
    private static String userEmail="test1@gmail.com";  // email address to login

    private static String androidId= DEF_ANDROID_ID; // Android ID from device

    private static Date date;    // last login timestamp

    public static int getApiLevel() {
        return apiLevel;
    }

    public static void setApiLevel(int apiLevel) {
        AppEnvironment.apiLevel = apiLevel;
    }

    public static String getUserDeviceName() {
        return userDeviceName;
    }

    public static void setUserDeviceName(String userDeviceName) {
        AppEnvironment.userDeviceName = userDeviceName;
    }

    public static String getEmail() {
        return userEmail;
    }

    public static void setEmail(String email) {
        AppEnvironment.userEmail = email;
    }

    public static String getAndroidId() {
        return androidId;
    }

    public static void setAndroidId(String androidId) {
        AppEnvironment.androidId = androidId;
    }

    public static Date getDate() {
        return date;
    }

    public static void setDate(Date date) {
        AppEnvironment.date = date;
    }

}
