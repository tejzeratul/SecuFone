package setting;

import java.util.Date;

/**
 * Created by Tejas on 3/31/2016.
 */
public class AppEnvironment {

    private static int apiLevel;   // API level of device

    private static String userDeviceName;  // device name created by user
    private static String userEmail;           // email address to login

    public static final String PARENT_FOLDER = "SecuFone";
    public static final String FILE_EXTENSION = ".txt";

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

    public static Date getDate() {
        return date;
    }

    public static void setDate(Date date) {
        AppEnvironment.date = date;
    }

}
