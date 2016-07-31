package com.tejtron.secufone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.Network_Access;
import net.ResultInterface;
import net.VolleyService;

import java.text.DecimalFormat;

import models.FinalScoreInfo;
import setting.AppEnvironment;
import setting.TempConfigFIle;

public class ScoreActivity extends AppCompatActivity {

    FinalScoreInfo objFinalScore = null;
    TextView tvYourScore;
    TextView tvMeanScore;
    TextView tvGlobalScore;
    Button btnDispAdvisory;

    private String resp;
    ProgressDialog progressDialog;

    // Volley related.
    ResultInterface mResultCallback = null;
    VolleyService mVolleyService;

    private String TAG = "ScoreActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // To launch 'Advisory' activity
        btnDispAdvisory = (Button) findViewById(R.id.btnAdvisory);
        btnDispAdvisory.setEnabled(false);

        btnDispAdvisory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ScoreActivity.this,
                        AdvisoryActivity.class);
                Bundle yourBundle = getIntent().getExtras();
                String jsonScoreResult = yourBundle.getString("test_result");
                intent.putExtra("test_result", jsonScoreResult);
                startActivity(intent);

            }
        });

        Bundle yourBundle = getIntent().getExtras();
        String jsonScoreResult = yourBundle.getString("test_result");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        objFinalScore = gson.fromJson(jsonScoreResult, FinalScoreInfo.class);

        tvYourScore = (TextView) findViewById(R.id.tvFinalScore);
        tvMeanScore = (TextView) findViewById(R.id.tvModelScore);
        tvGlobalScore = (TextView) findViewById(R.id.tvGlobalScore);

        if (objFinalScore != null) {

            if (objFinalScore.getScoreStatus() == 1) {

                DecimalFormat df2 = new DecimalFormat(".##");

                tvYourScore.setText(String.valueOf(df2.format(objFinalScore.getFinalTestScore())));
                tvMeanScore.setText(String.valueOf(df2.format(objFinalScore.getFinalModelMeanScore())));
                tvGlobalScore.setText(String.valueOf(df2.format(objFinalScore.getFinalGlobalMeanScore())));

                if (objFinalScore.getAdvisoryStatus() == 1) {
                    btnDispAdvisory.setEnabled(true);
                }
            }
        } else {
            System.out.println("ScoreActivity Status is 0");

            // Check Network Access
            Network_Access objNetworkAccess = new Network_Access();
            boolean isNetConnected = objNetworkAccess.isNetworkConnected(getApplicationContext());

            if (isNetConnected) {

                // To fetch Score in background using AsyncTask
                progressDialog = ProgressDialog.show(ScoreActivity.this,
                        "ProgressDialog",
                        "One moment");

                int testId = -1;
                if (objFinalScore != null)
                    testId = objFinalScore.getTestId();

                // Volley specific calls
                initVolleyCallback();
                performNetCall(testId);

            } else {

                setToastMessage("Network unavailable", Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_advisory_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_home:
                Intent intentHome = new Intent(ScoreActivity.this,
                        MainActivity.class);
                startActivity(intentHome);
                return true;
            case R.id.menu_about:
                Intent intent = new Intent(ScoreActivity.this,
                        AboutAppActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void initVolleyCallback() {

        mResultCallback = new ResultInterface() {

            @Override
            public void notifySuccess(String requestType, String response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);

                // execution of result of Long time consuming operation
                progressDialog.dismiss();
                Log.i("PerformTestActivity", "Response onPostExecute: " + response);


                Intent intent = new Intent(ScoreActivity.this,
                        ScoreActivity.class);
                intent.putExtra("score_result", response);
                System.out.println("ScoreActivity Starting again");
                startActivity(intent);
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + error.getMessage());
            }
        };
    }

    public void performNetCall(int id) {

        mVolleyService = new VolleyService(mResultCallback, getApplicationContext());

        // Perform POST call
        mVolleyService.postDataVolley(AppEnvironment.DEF_HTTP_TIMEOUT, "POSTCALL", TempConfigFIle.hostNameForScore, String.valueOf(id));
    }

    public void setToastMessage(String message, int type) {

        Toast.makeText(this, message, type).show();
    }
}