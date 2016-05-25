package com.tejtron.secufone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.HTTP_Utility;

import java.text.DecimalFormat;

import models.FinalScoreInfo;
import setting.AppEnvironment;
import setting.TempConfigFIle;

public class ScoreActivity extends AppCompatActivity {

    FinalScoreInfo objFinalScore=null;
    TextView tvYourScore;
    TextView tvMeanScore;
    TextView tvGlobalScore;
    Button btnDispAdvisory;

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
                String jsonScoreResult = yourBundle.getString("score_result");
                intent.putExtra("score_result", jsonScoreResult);
                startActivity(intent);

            }
        });


        Bundle yourBundle = getIntent().getExtras();
        String jsonScoreResult = yourBundle.getString("score_result");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        objFinalScore = gson.fromJson(jsonScoreResult, FinalScoreInfo.class);

        tvYourScore = (TextView) findViewById(R.id.tvFinalScore);
        tvMeanScore = (TextView) findViewById(R.id.tvModelScore);
        tvGlobalScore = (TextView) findViewById(R.id.tvGlobalScore);

        if (objFinalScore.getScoreStatus() == 1) {

            DecimalFormat df2 = new DecimalFormat(".##");

            tvYourScore.setText(String.valueOf(df2.format(objFinalScore.getFinalTestScore())));
            tvMeanScore.setText(String.valueOf(df2.format(objFinalScore.getFinalModelMeanScore())));
            tvGlobalScore.setText(String.valueOf(df2.format(objFinalScore.getFinalGlobalMeanScore())));

            if(objFinalScore.getAdvisoryStatus()==1) {
                btnDispAdvisory.setEnabled(true);
            }
        } else {

            if (objFinalScore.getScoreStatus() == 0) {

                System.out.println("ScoreActivity Status is 0");

                // To fetch Score in background using AsyncTask
                new AsyncTaskFetchScore().execute(objFinalScore.getTestId());

            }

        }

    }

    protected String getScoreFromScoreId(int id) {

        // Perform HTTP Post
        HTTP_Utility objHTTP = new HTTP_Utility();
        String response = objHTTP.getSendParameter(TempConfigFIle.hostNameForScore, "POST", AppEnvironment.DEF_HTTP_TIMEOUT, String.valueOf(id));
        // Log.i("HTTP Response PerformTestActivity", "Received String : " +response);

        return response;
    }


    private class AsyncTaskFetchScore extends AsyncTask<Integer, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(Integer... params) {
            publishProgress("One moment..."); // Calls onProgressUpdate()
            try {

                // TODO verify it works
                resp = getScoreFromScoreId(params[0]);
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
            Log.i("PerformTestActivity", "Response onPostExecute: " + result);



                Intent intent = new Intent(ScoreActivity.this,
                        ScoreActivity.class);
                intent.putExtra("score_result", result);
                System.out.println("ScoreActivity Starting again");
                startActivity(intent);

        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ScoreActivity.this,
                    "ProgressDialog",
                    "One moment");
        }

        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);

        }

    }

}