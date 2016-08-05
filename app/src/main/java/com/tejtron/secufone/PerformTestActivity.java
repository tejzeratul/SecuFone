package com.tejtron.secufone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.HTTPS_Utility;
import net.Network_Access;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import evaluations.Root;
import evaluations.SIMLock;
import evaluations.ScreenLock;
import evaluations.StorageEncrypt;
import file.FileProcessing;
import models.DeviceInfo;
import models.FinalScoreInfo;
import models.ParameterStatus;
import models.PhoneParameter;
import models.TestInfo;
import session.TestResultSessionManager;
import setting.AppEnvironment;
import setting.EnumValues;
import setting.TempConfigFIle;

public class PerformTestActivity extends Activity {

    private static Context pContext;

    String test_result;
    Handler handler;
    static ProgressDialog pd;

    TestResultSessionManager sessionTestResult = new TestResultSessionManager(MainActivity.getContext());

    PhoneParameter objParamData = null;
    DeviceInfo objDeviceInfo = null;
    TestInfo objTestInfo = null;
    FinalScoreInfo objFinalScore = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform);
        pContext = getApplicationContext();

        if (savedInstanceState != null) {

            ParameterStatus.dialogScheduled = savedInstanceState.getBoolean("DSCH");
            ParameterStatus.scheduled = savedInstanceState.getBoolean("SCH");
        }

        boolean pastData = sessionTestResult.isTestResultAvailable();

        if (pastData) {

            String pastDateStr = sessionTestResult.getTestDate(null);
            if (pastDateStr != null) {
                DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
                try {
                    Date pastDate = df.parse(pastDateStr);
                    Date currentDate = new Date();
                    long diff = pastDate.getTime() - currentDate.getTime();
                    long diffSeconds = (diff / 1000) % 60;

                    if (diffSeconds < 86400) {

                        Log.i("PerformTestActivity", "Using Past Data ");
                        String pastResult = sessionTestResult.getTestResult(null);
                        Intent intent = new Intent(PerformTestActivity.this,
                                ScoreActivity.class);
                        intent.putExtra("test_result", pastResult);
                        setToastMessage("Displaying Score", Toast.LENGTH_SHORT);
                        startActivity(intent);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Check Network Access
        Network_Access objNetworkAccess = new Network_Access();
        boolean isNetConnected = objNetworkAccess.isNetworkConnected(getApplicationContext());

        if (isNetConnected) {

            // To calculate parameters in background only once
            if (ParameterStatus.dialogScheduled == false) {
                ParameterStatus.dialogScheduled = true;
                Log.i("PerformTestActivity", "Start Dialog called");
                startDialog();

            }

        } else {

            setToastMessage("Network unavailable", Toast.LENGTH_LONG);
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                pd.dismiss();

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                String result = (String) msg.obj;
                Log.i("PerformTestActivity", "Response onPostExecute: " + result);

                GsonBuilder gsonBuilder = new GsonBuilder();
                // Allowing the serialization of static fields
                gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
                gsonBuilder.serializeNulls();
                // Creates a Gson instance based on the current configuration
                Gson gson = gsonBuilder.create();

                objFinalScore = gson.fromJson(result, FinalScoreInfo.class);
                test_result = result;

                if (objFinalScore != null) {
                    if (objFinalScore.getScoreStatus() == 1) {

                        // Initialize session object
                        // sessionTestResult = new TestResultSessionManager(MainActivity.getContext());
                        Bundle yourBundle = getIntent().getExtras();
                        String emailFromIntent = yourBundle.getString("user_email");

                        Date today = new Date();
                        String currentDate = today.toString();
                        sessionTestResult.setTestResult(emailFromIntent, test_result, currentDate);

                        Intent intent = new Intent(PerformTestActivity.this,
                                ScoreActivity.class);
                        intent.putExtra("test_result", test_result);
                        setToastMessage("Displaying Score", Toast.LENGTH_SHORT);

                        // TODO: Verify below parameters working correctly

                        // ParameterStatus.scheduled = false;
                        // ParameterStatus.dialogScheduled = false;
                        startActivity(intent);
                    }
                } else {

                    Log.i("PerformTestActivity", "ObjectFinalScore is null: " + result);
                }
            }
        };
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putBoolean("SCH", ParameterStatus.scheduled);
        savedInstanceState.putBoolean("DSCH", ParameterStatus.dialogScheduled);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    private void startDialog() {

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        // TODO: Leaked window when rotated

        if (ParameterStatus.scheduled == false) {

            pd = ProgressDialog.show(PerformTestActivity.this, "Performing new test", "Computing test data");

            ParameterStatus.scheduled = true;
            //start a new thread to process job
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //heavy job here

                    String resp = computeAndSendParameter();
                    //send message to main thread
                    Message msg = Message.obtain(); // Creates an new Message instance
                    msg.obj = resp; // Put the string into Message, into "obj" field.
                    msg.setTarget(handler); // Set the Handler
                    msg.sendToTarget();

                }
            }).start();
        }
    }

    /*
        Method to find all parameters value and send using POST
     */
    protected String computeAndSendParameter() {

        // to hold PhoneParameter data
        objParamData = new PhoneParameter();

        // Set parameter ScreenLock
        int lockType = ScreenLock.getCurrent(getContentResolver());
        objParamData.setParamScreenLock(lockType);

        // Set parameter StorageEncryption
        StorageEncrypt objStorageEncrypt = new StorageEncrypt();
        objParamData.setParamStorageEncrypt(objStorageEncrypt.getParamStorage());

        // Set parameter RootStatus
        Root objRoot = new Root();
        EnumValues rootParamResult = objRoot.checkRootParam();
        objParamData.setParamRootStatus(rootParamResult);

        // Set parameter SIMLock
        SIMLock objSIM = new SIMLock();
        EnumValues paramSIMResult = objSIM.getSIM_LockStatus(pContext);
        objParamData.setParamSIMLock(paramSIMResult);

        // Set parameter BatteryHealth
        BroadcastReceiver battery_receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent batteryIntent) {

                int batteryHealth = batteryIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
                objParamData.setParamBatteryHealth(batteryHealth);
            }
        };

        // Set parameter Application Installed (AppInfo Object)
        getAppsInstalled();

        // Set parameter of 'DeviceInfo'
        DeviceInfo.setManufacturer_name(Build.MANUFACTURER);
        DeviceInfo.setModel_name(Build.MODEL);
        DeviceInfo.setProduct_device(Build.DEVICE);

        DeviceInfo.setSIM_OperatorName(new SIMLock().findSIM_OperatorName());
        DeviceInfo.setApiLevel(Build.VERSION.SDK_INT);
        objDeviceInfo = new DeviceInfo();

        // Add all parameters to TestInfo object
        objTestInfo = new TestInfo(objParamData, objDeviceInfo, new Date().toString(), AppEnvironment.getEmail(), AppEnvironment.getUserDeviceName(), AppEnvironment.getAndroidId());

        String jsonString_TestParam;

        GsonBuilder gsonBuilder = new GsonBuilder();
        // Allowing the serialization of static fields
        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        gsonBuilder.serializeNulls();
        // Creates a Gson instance based on the current configuration
        Gson gson = gsonBuilder.create();

        jsonString_TestParam = gson.toJson(objTestInfo);

        //System.out.println("JSON String: " + jsonString_TestParam);
        FileProcessing fp = new FileProcessing();
        fp.createWriteFile("jsonTestData", false, pContext, jsonString_TestParam);


        // Perform HTTP Post
        HTTPS_Utility objHTTP = new HTTPS_Utility();
        objHTTP.initSSL_ForHTTPS(getApplicationContext());

        String response = objHTTP.getSendParameter(TempConfigFIle.hostNameForTest, "POST", AppEnvironment.DEF_HTTP_TIMEOUT, jsonString_TestParam);
        Log.i("PerformTestActivity", "Received String : " + response);

        return response;
    }

    /*
     * To get list of installed applications & also check for Antivirus application presence
     */
    private void getAppsInstalled() {

        final PackageManager pm = getPackageManager();

        //get a list of installed apps
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : packages) {

            if (pm.getLaunchIntentForPackage(appInfo.packageName) != null) {
                // apps with launcher intent
                if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    // updated system apps

                } else if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    // system apps

                } else {
                    // user installed apps

                    String appName = appInfo.loadLabel(getPackageManager()).toString();
                    String appPackage = appInfo.packageName;
                    String appSource = pm.getInstallerPackageName(appInfo.packageName);

                    // Excluding current app
                    if (!appPackage.contains("com.tejtron.secufone")) {

                        // Probable way to find Antivirus app
                        if (appName.contains("antivirus") || appName.contains("security")) {
                            objParamData.setParamAntivirusPresent(true);
                        }
                    }

                    objParamData.addAppDetails(appName, appPackage, appSource);
                }
            }
        }
    }

    public void setToastMessage(String message, int type) {
        Toast.makeText(this, message, type).show();
    }
}