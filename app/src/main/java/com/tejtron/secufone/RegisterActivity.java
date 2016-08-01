package com.tejtron.secufone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import net.Network_Access;
import net.ResultInterface;
import net.VolleyService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import setting.AppEnvironment;
import setting.EncryptString;
import setting.TempConfigFIle;
import validate.UserValidation;
import validate.Validation;

/**
 * A register screen that offers user registration.
 */
public class RegisterActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegisterProperty mAuthTask = null;
    private Intent intentForLogin;
    // UserSessionManager sessionLogin;

    // Volley related.
    ResultInterface mResultCallback = null;
    VolleyService mVolleyService;

    private String TAG = "RegisterActivity";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPassword1View;
    private EditText mPassword2View;
    private EditText mNameView;
    private TextView tvLoginText;
    private View mProgressView;
    private View mRegisterFormView;
    private Button mEmailSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        tvLoginText = (TextView) findViewById(R.id.link_to_login);
        mPassword1View = (EditText) findViewById(R.id.password1);
        mPassword2View = (EditText) findViewById(R.id.password2);
        mEmailSignUpButton = (Button) findViewById(R.id.email_register_button);

        // Listening to register new account link
        tvLoginText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        mPassword1View.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {


                return false;
            }
        });

        mPassword2View.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                return false;
            }
        });

        mNameView = (EditText) findViewById(R.id.personName);
        mNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                return false;
            }
        });

        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_about:
                Intent intent = new Intent(RegisterActivity.this,
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Attempts to register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPassword1View.setError(null);
        mPassword2View.setError(null);
        mNameView.setError(null);

        // Store values at the time of the register attempt.
        String email = mEmailView.getText().toString().trim().toLowerCase();
        String password1 = mPassword1View.getText().toString().trim();
        String password2 = mPassword2View.getText().toString().trim();
        String name = mNameView.getText().toString().trim().toLowerCase();

        // TODO: Get this from Android Location, if possible otherwise use unknown
        String country = "Unknown";
        String city = "Unknown";
        String state = "Unknown";

        boolean cancel = false;
        View focusView = null;

        // Validation
        UserValidation userValidate = new UserValidation();

        // Check for a valid password, if the user entered one.
        Validation validatePassword = userValidate.isPasswordValid(password1, password2);
        if (!validatePassword.getStatus()) {
            mPassword1View.setError(validatePassword.getErrorMessage());
            focusView = mPassword1View;
            cancel = true;
        }

        // Check for a valid email address.
        Validation validateEmail = userValidate.isEmailValid(email);
        if (!validateEmail.getStatus()) {
            mEmailView.setError(validateEmail.getErrorMessage());
            focusView = mEmailView;
            cancel = true;
        }

        // Check for valid name
        Validation validateName = userValidate.isNameValid(name);
        if (!validateName.getStatus()) {
            mNameView.setError(validateName.getErrorMessage());
            focusView = mNameView;
            cancel = true;
        }

        // Check for valid country,city,state
        //TODO: validation for country, city, state

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            // Check Network Access
            Network_Access objNetworkAccess = new Network_Access();
            boolean isNetConnected = objNetworkAccess.isNetworkConnected(getApplicationContext());

            if (isNetConnected) {

                showProgress(true);
                mAuthTask = new UserRegisterProperty(email, password1, name, country, city, state);
                initVolleyCallback();
                performNetCall();

            } else {
                setToastMessage("Network unavailable", Toast.LENGTH_LONG);
            }
        }
    }

    void initVolleyCallback() {

        mResultCallback = new ResultInterface() {
            @Override
            public void notifySuccess(String requestType, String response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);

                mAuthTask = null;
                showProgress(false);

                setToastMessage("Response received" + response, Toast.LENGTH_LONG);

                if (response.trim().equalsIgnoreCase("true")) {
                    setToastMessage("Sign up success", Toast.LENGTH_LONG);

                    // Call Login Activity
                    intentForLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intentForLogin);

                    finish();

                } else {
                    mEmailView.setError(getString(R.string.error_user_exist));
                    mEmailView.requestFocus();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + error.getMessage());
            }
        };
    }

    public void performNetCall() {

        mVolleyService = new VolleyService(mResultCallback, getApplicationContext());

        HashMap<String, String> paramPost = new HashMap<String, String>();
        EncryptString es = new EncryptString();
        String tempPassword = es.doEncryption(UserRegisterProperty.mPassword);

        paramPost.put("email", UserRegisterProperty.mEmail);
        paramPost.put("password", tempPassword);
        paramPost.put("name", UserRegisterProperty.mName);
        paramPost.put("country", UserRegisterProperty.mCountry);
        paramPost.put("city", UserRegisterProperty.mCity);
        paramPost.put("state", UserRegisterProperty.mState);
        paramPost.put("date", UserRegisterProperty.mDateCreated);

        try {
            UserRegisterProperty.requestStr = getPostDataString(paramPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Perform POST call
        mVolleyService.postDataVolley(AppEnvironment.DEF_HTTP_TIMEOUT, "POSTCALL", TempConfigFIle.hostNameForRegister, UserRegisterProperty.requestStr);
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}

/**
 * Represents an asynchronous registration task used to authenticate
 * the user.
 */
class UserRegisterProperty {

    static String mEmail;
    static String mPassword;
    static String mName;
    static String mCountry;
    static String mCity;
    static String mState;
    static String mDateCreated;
    static String requestStr;

    UserRegisterProperty(String email, String password, String name, String country, String state, String city) {
        mEmail = email;
        mPassword = password;
        mCountry = country;
        mName = name;
        mCity = city;
        mState = state;
        mDateCreated = new Date().toString();
    }
}