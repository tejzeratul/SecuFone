package session;

/**
 * Created by Tejas on 4/17/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.tejtron.secufone.LoginActivity;
import com.tejtron.secufone.MainActivity;

import setting.AppEnvironment;

public class UserSessionManager {

    private Context userContext;
    SharedPreferences pref;
    Editor editor;

    int PRIVATE_MODE = 0;    // Shared pref mode

    private static final String PREFER_NAME = "SecuFoneData";    // Sharedpref file name
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";      // All Shared Preferences Keys
    public static final String KEY_NAME = "name";                      // User name (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";                    // Email address (make variable public to access from outside)
    public static final String KEY_DEVICE_ID = "deviceId";

    // Constructor
    public UserSessionManager(Context context) {

        userContext = context;
        pref = userContext.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /*
     *
     *  Create login session for 'App-restart' use after 'Server' verification
     *
     */
    public void createUserLoginSession(String userName, String userEmail, String deviceId) {

        // Storing in sharedPreferences

        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_NAME, userName);
        editor.putString(KEY_EMAIL, userEmail);
        editor.putString(KEY_DEVICE_ID,deviceId);

        editor.commit();
    }

    /*
     *
     *  Check login method will check user login status from
     *
     */
    public boolean checkLogin() {

        // Check login status
        boolean loginStatus = pref.getBoolean(IS_USER_LOGIN, false);

        if (!loginStatus) {

            return false;
        } else {

            setAppEnvironment();

            return true;
        }
    }


    public String getUserEmail() {

        return pref.getString(KEY_EMAIL,"");
    }


    /*
     *
     *  Set session related App Environment
     *
     */
    public void setAppEnvironment() {

        String test1 = pref.getString(KEY_NAME, null);

        AppEnvironment.setEmail(pref.getString(KEY_EMAIL, null));

    }

    /*
     *
     *  Clear session data
     *
     */
    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // Clearing 'App-Environment' data

        // After logout redirect user to Main Activity
        userContext = MainActivity.getContext();
        Intent intent = new Intent(userContext,
                MainActivity.class);

        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        userContext.startActivity(intent);
    }

}