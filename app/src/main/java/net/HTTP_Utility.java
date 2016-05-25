package net;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HTTP_Utility {

    public String getSendParameter(String url, String method, int timeout, String json) {
        HttpURLConnection connection = null;
        try {

            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod(method);

            //set the sending type and receiving type to json
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            if (json != null) {
                //set the content length of the body
                connection.setRequestProperty("Content-length", json.getBytes().length + "");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                //send the json as body of the request
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.close();
            }

            //Connect to the server
            connection.connect();

            int status = connection.getResponseCode();
            Log.i("HTTP Client", "HTTP status code : " + status);
            switch (status) {
                case 200:
                case 201:
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb = new StringBuilder();
                        sb.append(line);
                        Log.i("HTTP Client", "Received Response getSendParameter: " + line);
                    }
                    bufferedReader.close();

                    //return received string
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } catch (IOException ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } catch (Exception ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Log.e("HTTP Client", "Error in http connection" + ex.toString());
                }
            }
        }
        return null;
    }

    public String performPostCall(String requestURL, String method, int timeout, HashMap<String, String> postDataParams) {

        URL url;
        HttpURLConnection conn = null;
        String response = "";
        try {
            url = new URL(requestURL);

            conn = (HttpURLConnection) url.openConnection();

            // Wifi Sign on
            if (url.getHost().equals(conn.getURL().getHost())) {

                conn.setReadTimeout(timeout);
                conn.setConnectTimeout(timeout);
                conn.setRequestMethod(method);
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));


                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();
                Log.i("HTTP Client", "HTTP response code : " + responseCode);
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        Log.i("HTTP Client", "Received Response performPostCall: " + line);
                        response += line;
                    }
                } else {
                    response = "";

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return response;
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
