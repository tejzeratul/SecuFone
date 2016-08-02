package com.tejtron.secufone;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import session.UserSessionManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AboutAppActivity extends AppCompatActivity {


    private static Context aContext;
    EditText etTextAbout;
    UserSessionManager session;
    AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        aContext = getApplicationContext();
        session = new UserSessionManager(getApplicationContext());

        adView = (AdView) findViewById(R.id.ad_view);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        etTextAbout = (EditText) findViewById(R.id.editTextAbout);


        String line;

        String line1 = "This application evaluates security and privacy of Android devices";
        String line2 = "This application is not a substitute of an antivirus app";
        String line5 = "--Developer--";
        String line6 = "Tejas Padliya";

        line = line1 + "\n" + "\n" + line2 + "\n" + "\n" + "\n" + line5 + "\n" + line6;

        etTextAbout.setText(line);
        etTextAbout.setKeyListener(null);
        etTextAbout.setEnabled(false);

    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (adView != null) {
            adView.resume();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.fragment_activity_about_app, container, false);
            return rootView;
        }
    }

    public static Context getContext() {
        return aContext;
    }

    public void setToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show();
    }

}
