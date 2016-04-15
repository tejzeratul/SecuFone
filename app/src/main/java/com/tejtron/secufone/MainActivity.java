package com.tejtron.secufone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import models.DeviceInfo;

import setting.AppEnvironment;

public class MainActivity extends Activity {

    private static Context mContext;
    Button btnDoTest;
    Button btnCheckScore;
    Button btnCheckAdvisory;

    TextView tvUserName;
    TextView tvDeviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        // Setting model-Device info
        DeviceInfo.setManufacturer_name(Build.MANUFACTURER);
        DeviceInfo.setModel_name(Build.MODEL);
        DeviceInfo.setProduct_device(Build.PRODUCT);

        // Set AppEnvironment-APILevel
        AppEnvironment.setApiLevel(Build.VERSION.SDK_INT);

        // Button to go to 'PerformTest' activity
        btnDoTest = (Button) findViewById(R.id.btnPerformTest);

        btnDoTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,
                        PerformTestActivity.class);
                startActivity(intent);

            }
        });


        // Button to go to 'Score' activity
        btnCheckScore = (Button) findViewById(R.id.btnScore);

        btnCheckScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,
                        PerformTestActivity.class);
                startActivity(intent);

            }
        });


        // Button to go to 'Advisory' activity
        btnCheckAdvisory = (Button) findViewById(R.id.btnReco);

        btnCheckAdvisory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,
                        PerformTestActivity.class);
                startActivity(intent);

            }
        });


    }

    public static Context getContext() {
        return mContext;
    }

}
