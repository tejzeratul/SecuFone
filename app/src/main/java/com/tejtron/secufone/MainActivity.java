package com.tejtron.secufone;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import models.DeviceInfo;
import session.TestResultSessionManager;
import session.UserSessionManager;
import setting.AppEnvironment;

public class MainActivity extends AppCompatActivity {

    private static Context mContext;
    Button btnDoTest;
    Button btnCheckScore;
    Button btnCheckAdvisory;

    TextView tvUserName;
    TextView tvDeviceName;
    String userEmail;

    // User Session Manager Class
    UserSessionManager userSession;
    TestResultSessionManager testResultSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        // Session class instance
        userSession = new UserSessionManager(getApplicationContext());
        testResultSession = new TestResultSessionManager(getApplicationContext());

        // Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity
        // and finish current activity from activity stack.
        if (!userSession.checkLogin()) {

            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(myIntent);
        } else {

            // Set nonSession related settings

            // Setting model-Device info
            DeviceInfo.setManufacturer_name(Build.MANUFACTURER);
            DeviceInfo.setModel_name(Build.MODEL);
            DeviceInfo.setProduct_device(Build.PRODUCT);

            // Set AppEnvironment-APILevel
            AppEnvironment.setApiLevel(Build.VERSION.SDK_INT);

            // Set AppEnvironment-AndroidId (if not set)
            if (AppEnvironment.getAndroidId().equals(AppEnvironment.DEF_ANDROID_ID)) {
                String dev_AndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                if (dev_AndroidId.trim().isEmpty() || dev_AndroidId == null)
                    dev_AndroidId = AppEnvironment.DEF_ANDROID_ID;

                AppEnvironment.setAndroidId(dev_AndroidId);
            }
            // Set userName in UI
            userEmail = userSession.getUserEmail();
            System.out.println("Login Found: " + userEmail);
            tvUserName = (TextView) findViewById(R.id.tvUserName);
            tvUserName.setText(userEmail);

            // TODO: Generate UniqueDevice Name
            // Currently using 'First4Char of userEmail' + 'Model'

            StringBuilder sb = new StringBuilder();
            sb.append(userEmail.substring(0, Math.min(userEmail.length(), 4)));
            sb.append(DeviceInfo.getModel_name());

            // Set unique userDeviceName in AppEnvironment, UI
            AppEnvironment.setUserDeviceName(sb.toString());
            tvDeviceName = (TextView) findViewById(R.id.tvDeviceName);
            tvDeviceName.setText(AppEnvironment.getUserDeviceName());

        }

        // Button to go to 'PerformTest' activity
        btnDoTest = (Button) findViewById(R.id.btnPerformTest);

        btnDoTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,
                        PerformTestActivity.class);
                intent.putExtra("user_email", userEmail);
                intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });

        // Button to go to 'Score' activity
        btnCheckScore = (Button) findViewById(R.id.btnScore);

        btnCheckScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!testResultSession.isTestResultAvailable()) {

                    Intent intent = new Intent(MainActivity.this,
                            PerformTestActivity.class);
                    intent.putExtra("user_email", userEmail);
                    startActivity(intent);

                } else {

                    Log.i("MainActivity", "Using Past Data ");
                    String result = testResultSession.getTestResult(userEmail);
                    Intent intent = new Intent(MainActivity.this,
                            ScoreActivity.class);
                    intent.putExtra("user_email", userEmail);
                    intent.putExtra("test_result", result);
                    startActivity(intent);

                }
            }
        });

        // Button to go to 'Advisory' activity
        btnCheckAdvisory = (Button) findViewById(R.id.btnReco);

        btnCheckAdvisory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!testResultSession.isTestResultAvailable()) {

                    Intent intent = new Intent(MainActivity.this,
                            PerformTestActivity.class);
                    intent.putExtra("user_email", userEmail);
                    startActivity(intent);
                } else {

                    String result = testResultSession.getTestResult(userEmail);
                    Intent intent = new Intent(MainActivity.this,
                            AdvisoryActivity.class);
                    intent.putExtra("user_email", userEmail);
                    intent.putExtra("test_result", result);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!userSession.checkLogin()) {

            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);

            startActivity(myIntent);
        }

    }

    public static Context getContext() {
        return mContext;
    }


    public void setToastMessage(String message, int type) {
        Toast.makeText(this, message, type).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_logout:
                setToastMessage("Signing out", Toast.LENGTH_SHORT);
                userSession.logoutUser();
                return true;

            case R.id.menu_about:
                Intent intent = new Intent(MainActivity.this,
                        AboutAppActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}