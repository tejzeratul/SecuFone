package net;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Tejas on 7/10/2016.
 */
public class VolleyService {

    ResultInterface mResultCallback = null;
    Context mContext;
    String requestData;

    public VolleyService(ResultInterface resultCallback, Context context) {
        mResultCallback = resultCallback;
        mContext = context;
    }

    public void postDataVolley(int socketTimeout, final String requestType, String url, String objData) {
        try {

            requestData = objData;
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest jsonObj = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                }
            }

            ) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    String httpPostBody = requestData.trim();
                    // usually you'd have a field with some values you'd want to escape, you need to do it yourself if overriding getBody. here's how you do it

                    /*
                    try {
                       // httpPostBody = httpPostBody + "&randomFieldFilledWithAwkwardCharacters=" + URLEncoder.encode("{{%stuffToBe Escaped/", "UTF-8");

                    } catch (UnsupportedEncodingException exception) {
                        Log.e("ERROR", "exception", exception);
                        // return null and don't pass any POST string if you encounter encoding error
                        return null;
                    }
                    */
                    return httpPostBody.getBytes();
                }

            };

            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObj.setRetryPolicy(policy);
            queue.add(jsonObj);

        } catch (Exception e) {

        }
    }

    public void getDataVolley(final String requestType, String url) {
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest jsonObj = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                }
            });

            queue.add(jsonObj);
        } catch (Exception e) {

        }
    }
}
