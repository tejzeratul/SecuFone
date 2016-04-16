package models;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.tejtron.secufone.MainActivity;

/**
 * Created by Tejas on 4/14/2016.
 */
public class DeviceInfo {

    private static String manufacturer_name;   // Manufacturer name (Manufacturer)
    private static String model_name;          // Model name (Model)
    private static String product_device;      // Device name (Device)
    private static String simOperator_name;        // Service provider name

    private static DeviceInfo objectDeviceInfo;

    private Context dContext;

    public DeviceInfo() {

        setManufacturer_name(Build.MANUFACTURER);
        setModel_name(Build.MODEL);
        setProduct_device(Build.DEVICE);
        dContext= MainActivity.getContext();
        setSIM_OperatorName();

    }

    public static String getManufacturer_name() {
        return manufacturer_name;
    }

    public static void setManufacturer_name(String manufacturer_name) {
        DeviceInfo.manufacturer_name = manufacturer_name;
    }

    public static String getModel_name() {
        return model_name;
    }

    public static void setModel_name(String model_name) {
        DeviceInfo.model_name = model_name;
    }

    public static String getProduct_device() {
        return product_device;
    }

    public static void setProduct_device(String product_device) {
        DeviceInfo.product_device = product_device;
    }

    public String getSIM_OperatorName() {
        return simOperator_name;

    }



    public static synchronized DeviceInfo getInstance( ) {
        if (objectDeviceInfo == null)
            objectDeviceInfo=new DeviceInfo();
        return objectDeviceInfo;
    }

    public void setSIM_OperatorName() {

        TelephonyManager  telephonyManager = (TelephonyManager) dContext.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();

        String result="Unknown";

        switch (simState) {

            case (TelephonyManager.SIM_STATE_READY): {
                result = telephonyManager.getSimOperatorName();
            }
        }

        simOperator_name=result;
    }

}