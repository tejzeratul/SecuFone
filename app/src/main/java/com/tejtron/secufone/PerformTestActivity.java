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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Date;
import java.util.List;

import evaluations.Root;
import evaluations.ScreenLock;
import evaluations.StorageEncrypt;
import file.FileProcessing;
import models.DeviceInfo;
import models.PhoneParameter;
import models.TestInfo;
import setting.AppEnvironment;
import setting.EnumValues;

public class PerformTestActivity extends Activity {

    private static Context mContext;

    Button btnToBeRemoved;
    Button btnCheckScore;
    Button btnCheckAdvisory;

    PhoneParameter objParamData=null;
    TestInfo objTestInfo=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        mContext=getApplicationContext();

        // To launch 'Score' activity
        btnCheckScore = (Button) findViewById(R.id.btnPerformTest);

        btnCheckScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(PerformTestActivity.this,
                        MainActivity.class);
                startActivity(intent);

            }
        });


        // To launch 'Advisory' activity
        btnCheckAdvisory = (Button) findViewById(R.id.btnPerformTest);

        btnCheckAdvisory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(PerformTestActivity.this,
                        MainActivity.class);
                startActivity(intent);

            }
        });

        btnCheckAdvisory.setEnabled(false);
        btnCheckScore.setEnabled(false);

        // To calculate parameters in background using AsyncTask
        new AsyncTaskCalculateParam().execute("5");

    }

    /*
        Method to find all parameters value and send using POST
     */
    protected void computeAndSendParameter() {

       // to hold PhoneParameter data
        objParamData =new PhoneParameter();

        // Set parameter ScreenLock
        int lockType = ScreenLock.getCurrent(getContentResolver());
        objParamData.setParamScreenLock(lockType);

        // Set parameter StorageEncryption
        StorageEncrypt objStorageEncrypt=new StorageEncrypt();
        objParamData.setParamStorageEncrypt(objStorageEncrypt.getParamStorage());

        // Set parameter RootStatus
        Root objRoot=new Root();
        EnumValues rootParamResult =objRoot.checkRootParam();
        System.out.println("Root St: "+rootParamResult);
        objParamData.setParamRootStatus(rootParamResult);

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

        // Add all parameters to TestInfo object
        objTestInfo=new TestInfo(objParamData,DeviceInfo.getInstance(),new Date(),AppEnvironment.getEmail(),AppEnvironment.getUserDeviceName());

        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString_TestParam = mapper.writeValueAsString(objTestInfo);
            System.out.println("JSON String: "+jsonString_TestParam);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }

       // Perform HTTP Post

    }

    /*
     * To get list of installed applications and also check for Antivirus application presence
     */
    private int getAppsInstalled() {

        final PackageManager pm = getPackageManager();

        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        // Create File
        FileProcessing objectFP=new FileProcessing();
        File filePath=objectFP.createFile("data",true);

        int c=0;
        for (ApplicationInfo appInfo : packages) {

            if(pm.getLaunchIntentForPackage(appInfo.packageName) != null) {
                // apps with launcher intent
                if((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    // updated system apps

                } else if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    // system apps

                } else {
                    // user installed apps

                    String appName=appInfo.loadLabel(getPackageManager()).toString();
                    String appPackage=appInfo.packageName;
                    String appSource= pm.getInstallerPackageName(appInfo.packageName);

                    c++;
                    objParamData.addAppDetails(appName,appPackage,appSource);

                    /*
                        objectFP.writeToFile(filePath, c + ".");
                        objectFP.writeToFile(filePath, " Application Name : "+ appInfo.loadLabel(getPackageManager()).toString());
                        objectFP.writeToFile(filePath, "Installed package :" + appInfo.packageName);
                        objectFP.writeToFile(filePath, "Source Dir :" + appInfo.sourceDir);
                        objectFP.writeToFile(filePath, "Launch Activity :" + pm.getLaunchIntentForPackage(appInfo.packageName));
                        objectFP.writeToFile(filePath, "Origin :" + pm.getInstallerPackageName(appInfo.packageName));
                        objectFP.writeToFile(filePath, "");

                     */

                }

            }

        }

        // the getLaunchIntentForPackage returns an intent that you can use with startActivity()
        System.out.println("Count: "+c );
        return 0;
    }





    private class AsyncTaskCalculateParam extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Calculating..."); // Calls onProgressUpdate()
            try {

                computeAndSendParameter();
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


