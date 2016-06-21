package com.tejtron.secufone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import models.AdvisoryInfo;
import models.FinalScoreInfo;

public class AdvisoryActivity extends AppCompatActivity {

    Button btnDispScore;
    FinalScoreInfo objFinalScore = null;
    TextView tvAdvisory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisory);

        // To launch 'Advisory' activity
        btnDispScore = (Button) findViewById(R.id.btnScore);
        btnDispScore.setEnabled(false);

        btnDispScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(AdvisoryActivity.this,
                        ScoreActivity.class);
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

        StringBuilder sb = new StringBuilder();
        ArrayList<AdvisoryInfo> objAdvisoryInfoList = objFinalScore.getObjAdvisoryInfoList();

        if (objFinalScore.getScoreStatus() == 1 && objFinalScore.getAdvisoryStatus() == 1) {

            for (int i = 0; i < objAdvisoryInfoList.size(); i++) {
                sb.append((i + 1) + ". ");
                sb.append(objAdvisoryInfoList.get(i).getAdvisoryText());
                sb.append(System.getProperty("line.separator"));

            }

            if (objFinalScore.getScoreStatus() == 1) {
                btnDispScore.setEnabled(true);
            }
        }

        String finalAdvisory = sb.toString().trim();

        tvAdvisory = (TextView) findViewById(R.id.tvAdvisory);

        if (finalAdvisory != null && !finalAdvisory.isEmpty()) {
            tvAdvisory.setText(finalAdvisory);
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
                Intent intentHome = new Intent(AdvisoryActivity.this,
                        MainActivity.class);
                startActivity(intentHome);
                return true;
            case R.id.menu_about:
                Intent intent = new Intent(AdvisoryActivity.this,
                        AboutAppActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setToastMessage(String message, int type) {
        Toast.makeText(this, message, type).show();
    }

}
