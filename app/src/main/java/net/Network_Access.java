package net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Tejas on 5/28/2016.
 */
public class Network_Access {

   public boolean isNetworkConnected(Context context) {

        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected=false;
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting())
            isConnected = true;

        return isConnected;
    }

    boolean isInternetConnected(Context context) {

        //TODO: Update code logic to check for Internet access in new thread
        return true;
    }
}
