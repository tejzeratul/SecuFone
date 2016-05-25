package evaluations;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.tejtron.secufone.MainActivity;

import setting.EnumValues;

/**
 * Created by Tejas on 4/16/2016.
 */
public class SIMLock {

    private Context sContext= MainActivity.getContext();

    public String findSIM_OperatorName() {
        TelephonyManager telephonyManager = (TelephonyManager) sContext.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();

        String result="Unknown";

        switch (simState) {

            case (TelephonyManager.SIM_STATE_READY): {

                String temp = telephonyManager.getSimOperatorName();
                temp=temp.trim();
                System.out.println("SIM Property: "+temp);

                if(temp!=null && !temp.isEmpty()) {
                    result=temp;
                }

                if(result.equals("Unknown")) {
                    temp=telephonyManager.getNetworkOperatorName();
                    result=temp;
                }
            }
        }

        return result;
    }

    public EnumValues getSIM_LockStatus(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_UNKNOWN:
                return EnumValues.UNKNOWN;
            case TelephonyManager.SIM_STATE_ABSENT:
                return EnumValues.UNKNOWN;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                return EnumValues.YES;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                return EnumValues.YES;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                return EnumValues.YES;
            case TelephonyManager.SIM_STATE_READY:
                return EnumValues.NO;
        }
        return EnumValues.UNKNOWN;

    }

    public boolean isSIMInstalled(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int state = manager.getSimState();

        if (state != TelephonyManager.SIM_STATE_READY) {
            return false;
        } else {
            return true;
        }
    }

}
