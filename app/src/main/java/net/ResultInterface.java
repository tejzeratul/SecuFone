package net;

import com.android.volley.VolleyError;

/**
 * Created by Tejas on 7/10/2016.
 */

public interface ResultInterface {
    public void notifySuccess(String requestType, String response);
    public void notifyError(String requestType, VolleyError error);
}
