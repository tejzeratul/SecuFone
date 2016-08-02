package session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tejas on 5/31/2016.
 */
public class TestResultSessionManager {

    private Context resultContext;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    int PRIVATE_MODE = 0;    // Shared pref mode

    private static final String PREFER_NAME = "SecuFoneResult";    // Sharedpref file name
    private static final String KEY_TEST_RESULT_AVAILABLE = "IsTestResultAvailable";      // All Shared Preferences Keys
    public static final String KEY_RESULT = "resultMain";                      // User name (make variable public to access from outside)
    public static final String KEY_DATE = "resultDate";                      // Date on which result generated (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";                    // Email address (make variable public to access from outside)

    // Constructor
    public TestResultSessionManager(Context context) {

        resultContext = context;
        pref = resultContext.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /*
     *
     *  Create test session for 'App-restart' having session data (score & advisory)
     *
     */
    public void setTestResult(String email, String result, String date) {

        // Storing in sharedPreferences

        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_RESULT, result);
        editor.putString(KEY_DATE, date);
        editor.putBoolean(KEY_TEST_RESULT_AVAILABLE,true);

        editor.commit();
    }

    public String getTestResult(String email) {

        return pref.getString(KEY_RESULT,null);
    }

    public String getTestDate(String email) {

        return pref.getString(KEY_DATE,null);
    }


    public boolean isTestResultAvailable()  {

        return pref.getBoolean(KEY_TEST_RESULT_AVAILABLE,false);
    }

}
