package com.tejtron.secufone;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import evaluations.Root;
import evaluations.ScreenLock;

import models.PhoneParameter;

import setting.AppEnvironment;
import setting.EnumValues;
import setting.EnumValuesStorage;

public class PerformTestActivity extends Activity {

    private static Context mContext;

    Button btnToBeRemoved;
    Button btnCheckScore;
    Button btnCheckAdvisory;


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

        // To calculate parameters in background using AsyncTask
        new AsyncTaskCalculateParam().execute("5");

    }

    /*
        Method to find all parameters value and send using POST
     */
    protected void computeAndSendParameter() {

        // to hold parameter data
        PhoneParameter objParamData =new PhoneParameter();

        // Set parameter ScreenLock
        int lockType = ScreenLock.getCurrent(getContentResolver());
        objParamData.setParamScreenLock(lockType);

        // Set parameter StorageEncryption
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        int enStatus = dpm.getStorageEncryptionStatus();

        if (enStatus == android.app.admin.DevicePolicyManager.ENCRYPTION_STATUS_UNSUPPORTED)
            objParamData.setParamStorageEncrypt(EnumValuesStorage.UNSUPPORTED);
        else {
            if (enStatus == android.app.admin.DevicePolicyManager.ENCRYPTION_STATUS_INACTIVE)
                objParamData.setParamStorageEncrypt(EnumValuesStorage.INACTIVE);
            else {
                if (enStatus == android.app.admin.DevicePolicyManager.ENCRYPTION_STATUS_ACTIVATING)
                    objParamData.setParamStorageEncrypt(EnumValuesStorage.ACTIVATING);
                else {
                    if (enStatus == android.app.admin.DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE)
                        objParamData.setParamStorageEncrypt(EnumValuesStorage.ACTIVE);
                }
            }
        }

        // Set parameter RootStatus
        Root objRoot=new Root();
        EnumValues rootParamResult =objRoot.checkRootParam();
        System.out.println("Root St: "+rootParamResult);
        objParamData.setParamRootStatus(rootParamResult);


        /*
         * Add Below
         */



        // Keyguard Secure (SIm Status)
        KeyguardManager km = (KeyguardManager) getApplicationContext()
                .getSystemService(Context.KEYGUARD_SERVICE);
        //    tvSIM.setText("SIM Secure Unknown");

        //   (km.isDeviceSecure()) {
        //         if (km.isKeyguardSecure())


        /* old */



        // Metric 10 - Battery Health
        int batteryHealth=-1;
        BroadcastReceiver battery_receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent batteryIntent) {

               int batteryHealth = batteryIntent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            }
        };




    }

    private int getAppsInstalled() {
        final PackageManager pm = getPackageManager();

        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        // Create File
        File filePath=createFile("data");

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

                    c++;
                    writeToFile(filePath, c + ".");
                    writeToFile(filePath, "Installed package :" + appInfo.packageName);
                    writeToFile(filePath, "Source Dir :" + appInfo.sourceDir);
                    writeToFile(filePath, "Launch Activity :" + pm.getLaunchIntentForPackage(appInfo.packageName));
                    writeToFile(filePath, "Origin :" + pm.getInstallerPackageName(appInfo.packageName));
                    writeToFile(filePath, "");

                }

            }

        }

        // the getLaunchIntentForPackage returns an intent that you can use with startActivity()
        System.out.println("Count: "+c );
        return 0;
    }

    private File createFile(String rawFileName) {

        /*
        Separate File Creation & File Write Logic
         */

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        File filePath=null;
        Date now = new Date();
        String uniqueName = formatter.format(now);

        String fileName = rawFileName + uniqueName + AppEnvironment.FILE_EXTENSION;

        try {
            //File root = new File(Environment.getExternalStorageDirectory()+File.separator+"Music_Folder", "Report Files");
            File root = new File(Environment.getExternalStorageDirectory(), AppEnvironment.PARENT_FOLDER);
            if (!root.exists()) {
                root.mkdirs();
            }
            filePath = new File(root, fileName);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return filePath;
    }

    private void writeToFile(File filePath, String data) {

        try {
            // filename and append
            FileWriter writer = new FileWriter(filePath,true);
            writer.append(data+"\n\n");
            writer.flush();
            writer.close();
//            Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }

    }

    private class AsyncTaskCalculateParam extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Calculating..."); // Calls onProgressUpdate()
            try {

                getAppsInstalled();
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


