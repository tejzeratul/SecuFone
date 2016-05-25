package evaluations;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import com.tejtron.secufone.MainActivity;

import java.io.File;

public class ScreenLock
{
    private final static String PASSWORD_TYPE_KEY = "lockscreen.password_type";

    /**
     * This constant means that android using some unlock method not described here.
     * Possible new methods would be added in the future releases.
     */
    public final static int SOMETHING_ELSE = -1;

    /**
     * Android using "None" or "Slide" unlock method. It seems there is no way to determine which method exactly used.
     * In both cases you'll get "PASSWORD_QUALITY_SOMETHING" and "LOCK_PATTERN_ENABLED" == 0.
     */
    public final static int NONE_OR_SLIDER = 1;

    /**
     * Android using "Pattern" unlock method.
     */
    public final static int PATTERN = 2;

    /**
     * Android using "PIN" unlock method.
     */
    public final static int PIN = 3;

    /**
     * Android using "COMPLEX PIN" unlock method.
     */
    public final static int PIN_COMPLEX = 4;


    /**
     * Android using "Password" unlock method with password containing only letters.
     */
    public final static int PASSWORD_ALPHABETIC = 5;

    /**
     * Android using "Password" unlock method with password containing both letters and numbers.
     */
    public final static int PASSWORD_ALPHANUMERIC = 6;

    /**
     * Android using "Password" unlock method with password containing special characters, letters and numbers.
     */
    public final static int PASSWORD_ALPHANUMERIC_COMPLEX = 7;


    /**
     * Android using "Password" or "PIN" or "SIMILAR" unlock method
     */
    public final static int PASSWORD_SOMETHING = 8;

    /**
     * Android using "Face Unlock" with "Pattern" as additional unlock method. Android don't allow you to select
     * "Face Unlock" without additional unlock method.
     */
    public final static int FACE_WITH_PATTERN = 9;

    /**
     * Android using "Face Unlock" with "PIN" as additional unlock method. Android don't allow you to select
     * "Face Unlock" without additional unlock method.
     */
    public final static int FACE_WITH_PIN = 10;

    /**
     * Android using "Face Unlock" with some additional unlock method not described here.
     * Possible new methods would be added in the future releases. Values from 5 to 8 reserved for this situation.
     */
    public final static int FACE_WITH_SOMETHING_ELSE = 11;


    /**
     * Returns current unlock method as integer value. You can see all possible values above
     * @param contentResolver we need to pass ContentResolver to Settings.Secure.getLong(...) and
     *                        Settings.Secure.getInt(...)
     * @return current unlock method as integer value
     */
    public static int getCurrent(ContentResolver contentResolver)
    {
        long mode = Settings.Secure.getLong(contentResolver, PASSWORD_TYPE_KEY,
                DevicePolicyManager.PASSWORD_QUALITY_SOMETHING);

        if (mode == DevicePolicyManager.PASSWORD_QUALITY_SOMETHING)
        {
            // Depreceated in API Level 23
            if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP_MR1) {

                if (Settings.Secure.getInt(contentResolver, Settings.Secure.LOCK_PATTERN_ENABLED, 0) == 1) {
                    return ScreenLock.PATTERN;
                }
            } else {

                KeyguardManager km = (KeyguardManager) MainActivity.getContext().getSystemService(Context.KEYGUARD_SERVICE);

                if(km.isDeviceSecure()) {


                    return ScreenLock.PATTERN;
                }
                else
                    return ScreenLock.NONE_OR_SLIDER;
            }
        }


        // check if it is still working
        else if (mode == DevicePolicyManager.PASSWORD_QUALITY_BIOMETRIC_WEAK)
        {
            String dataDirPath = Environment.getDataDirectory().getAbsolutePath();
            if (nonEmptyFileExists(dataDirPath + "/system/gesture.key"))
            {
                return ScreenLock.FACE_WITH_PATTERN;
            }
            else if (nonEmptyFileExists(dataDirPath + "/system/password.key"))
            {
                return ScreenLock.FACE_WITH_PIN;
            }
            else return FACE_WITH_SOMETHING_ELSE;
        }


        else if (mode == DevicePolicyManager.PASSWORD_QUALITY_COMPLEX)
        {
            return ScreenLock.PASSWORD_ALPHANUMERIC_COMPLEX;
        }
        else if (mode == DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC)
        {
            return ScreenLock.PASSWORD_ALPHANUMERIC;
        }



        else if (mode == DevicePolicyManager.PASSWORD_QUALITY_ALPHABETIC)
        {
            return ScreenLock.PASSWORD_ALPHABETIC;
        }
        else if (mode == DevicePolicyManager.PASSWORD_QUALITY_NUMERIC_COMPLEX)
        {
            return ScreenLock.PIN_COMPLEX;
        }
        else if (mode == DevicePolicyManager.PASSWORD_QUALITY_NUMERIC)
        {
            return ScreenLock.PIN;
        }
        else if (mode == DevicePolicyManager.PASSWORD_QUALITY_SOMETHING)
        {
            return ScreenLock.PASSWORD_SOMETHING;
        }
        else return ScreenLock.SOMETHING_ELSE;

        return ScreenLock.SOMETHING_ELSE;
    }

    private static boolean nonEmptyFileExists(String filename)
    {
        File file = new File(filename);
        return file.exists() && file.length() > 0;
    }

}


