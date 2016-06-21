package com.tejtron.secufone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.HTTPS_Utility;
import net.Network_Access;

import java.util.Date;
import java.util.List;

import evaluations.Root;
import evaluations.SIMLock;
import evaluations.ScreenLock;
import evaluations.StorageEncrypt;
import file.FileProcessing;
import models.DeviceInfo;
import models.FinalScoreInfo;
import models.PhoneParameter;
import models.TestInfo;
import session.TestResultSessionManager;
import session.UserSessionManager;
import setting.AppEnvironment;
import setting.EnumValues;
import setting.TempConfigFIle;

public class PerformTestActivity extends Activity {

    private static Context pContext;

    Button btnToBeRemoved;
    Button btnCheckScore;
    Button btnCheckAdvisory;
    String test_result;

    TestResultSessionManager sessionTestResult;
    UserSessionManager sessionUser;

    PhoneParameter objParamData = null;
    DeviceInfo objDeviceInfo = null;
    TestInfo objTestInfo = null;
    FinalScoreInfo objFinalScore = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        pContext = getApplicationContext();

        // To launch 'Score' activity
        btnCheckScore = (Button) findViewById(R.id.btnCScore);

        btnCheckScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(PerformTestActivity.this,
                        ScoreActivity.class);

                if (test_result == null) {
                    Bundle yourBundle = getIntent().getExtras();
                    test_result = yourBundle.getString("test_result");

                    if (test_result == null) {

                        sessionTestResult = new TestResultSessionManager(MainActivity.getContext());
                        sessionUser = new UserSessionManager(MainActivity.getContext());

                        test_result = sessionTestResult.getTestResult(sessionUser.getUserEmail());

                    }
                }
                intent.putExtra("test_result", test_result);
                startActivity(intent);

            }
        });


        // To launch 'Advisory' activity
        btnCheckAdvisory = (Button) findViewById(R.id.btnCAdvisory);

        btnCheckAdvisory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(PerformTestActivity.this,
                        AdvisoryActivity.class);

                if (test_result == null) {
                    Bundle yourBundle = getIntent().getExtras();
                    test_result = yourBundle.getString("test_result");

                    if (test_result == null) {

                        sessionTestResult = new TestResultSessionManager(MainActivity.getContext());
                        sessionUser = new UserSessionManager(MainActivity.getContext());

                        test_result = sessionTestResult.getTestResult(sessionUser.getUserEmail());

                    }
                }

                intent.putExtra("test_result", test_result);
                startActivity(intent);

            }
        });

        btnCheckAdvisory.setEnabled(false);
        btnCheckScore.setEnabled(false);


        // Check Network Access
        Network_Access objNetworkAccess = new Network_Access();
        boolean isNetConnected = objNetworkAccess.isNetworkConnected(getApplicationContext());

        if (isNetConnected) {

            // To calculate parameters in background using AsyncTask
            new AsyncTaskCalculateParam().execute("5");
        } else {
            setToastMessage("Network unavailable", Toast.LENGTH_LONG);
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

    private class AsyncTaskCalculateParam extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Calculating..."); // Calls onProgressUpdate()
            try {

                resp = computeAndSendParameter();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {

            // execution of result of Long time consuming operation

                progressDialog.dismiss();
                btnCheckAdvisory.setEnabled(true);
                btnCheckScore.setEnabled(true);
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

                        // TODO: Verify if it works

                        // Initialize session object
                        sessionTestResult = new TestResultSessionManager(MainActivity.getContext());
                        Bundle yourBundle = getIntent().getExtras();
                        String emailFromIntent = yourBundle.getString("user_email");
                        sessionTestResult.setTestResult(emailFromIntent, test_result);


                        Intent intent = new Intent(PerformTestActivity.this,
                                ScoreActivity.class);
                        intent.putExtra("test_result", test_result);
                        setToastMessage("Displaying Score", Toast.LENGTH_SHORT);
                        startActivity(intent);
                    }
                } else {
                    Log.i("PerformTestActivity", "ObjectFinalScore is null: " + result);
                }

        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PerformTestActivity.this,
                    "ProgressDialog",
                    "Performing Test");
        }

        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);

        }

    }
}