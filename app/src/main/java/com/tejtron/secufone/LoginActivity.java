package com.tejtron.secufone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import java.util.HashMap;
import java.util.Map;

import session.UserSessionManager;
import setting.AppEnvironment;
import setting.EncryptString;
import setting.TempConfigFIle;
import validate.UserValidation;
import validate.Validation;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginProperty mAuthTask = null;
    private Intent in;
    UserSessionManager sessionLogin;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView tvRegisterText;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton;

    // Volley related.
    ResultInterface mResultCallback = null;
    VolleyService mVolleyService;

    private String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        tvRegisterText = (TextView) findViewById(R.id.link_to_register);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        // Listening to register new account link
        tvRegisterText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_about:
                Intent intent = new Intent(LoginActivity.this,
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * Handles UI operations too.
     */
    private void attemptLogin() {

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Validation
        UserValidation userValidate = new UserValidation();

        // Check for a valid password, if the user entered one.
        Validation validatePassword = userValidate.isPasswordValid(password);
        if (!validatePassword.getStatus()) {
            mPasswordView.setError(validatePassword.getErrorMessage());
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        Validation validateEmail = userValidate.isEmailValid(email);
        if (!validateEmail.getStatus()) {
            mEmailView.setError(validateEmail.getErrorMessage());
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {

            // Check Network Access
            Network_Access objNetworkAccess = new Network_Access();
            boolean isNetConnected = objNetworkAccess.isNetworkConnected(getApplicationContext());

            if (isNetConnected) {

                // Show a progress spinner, to perform the login attempt.
                showProgress(true);
                mAuthTask = new UserLoginProperty(email, password);

                // Volley specific calls.
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
                Log.d(TAG, "Volley JSON post " + response);

                mAuthTask = null;
                showProgress(false);

                setToastMessage("Response received" + response, Toast.LENGTH_LONG);

                if (response.trim().equalsIgnoreCase("true")) {

                    setToastMessage("Sign in success", Toast.LENGTH_SHORT);

                    // Initialize session object
                    sessionLogin = new UserSessionManager(MainActivity.getContext());

                    String dev_AndroidId;
                    dev_AndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                    // TODO: Update condition
                    if (dev_AndroidId.trim().isEmpty() || dev_AndroidId == null)
                        dev_AndroidId = AppEnvironment.DEF_ANDROID_ID;

                    AppEnvironment.setAndroidId(dev_AndroidId);

                /*
                 * TODO: Hard coded name
                 * In future, get it from server, using json response
                 */
                    sessionLogin.createUserLoginSession("Tejas", UserLoginProperty.mEmail, dev_AndroidId);

                    // Call Main Activity
                    in = new Intent(LoginActivity.this, MainActivity.class);
                    in.putExtra("loggedIn", true);
                    startActivity(in);

                    finish();

                } else {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
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
        String tempPassword = es.doEncryption(UserLoginProperty.mPassword);

        paramPost.put("email", UserLoginProperty.mEmail);
        paramPost.put("password", tempPassword);

        try {
            UserLoginProperty.requestStr = getPostDataString(paramPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Perform POST call
        mVolleyService.postDataVolley(AppEnvironment.DEF_HTTP_TIMEOUT, "POSTCALL", TempConfigFIle.hostNameForLogin, UserLoginProperty.requestStr);
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

class UserLoginProperty {

    static String mEmail;
    static String mPassword;
    static String requestStr;

    UserLoginProperty(String email, String password) {
        mEmail = email;
        mPassword = password;
    }
}