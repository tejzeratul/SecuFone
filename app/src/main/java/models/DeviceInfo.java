package models;

/**
 * Created by Tejas on 4/14/2016.
 */
public class DeviceInfo {

    private static String manufacturer_name;   // Manufacturer name (Manufacturer)
    private static String model_name;          // Model name (Model)
    private static String product_device;      // Device name (Device)
    private static String simOperator_name;    // Service provider name

    private static int apiLevel; // API Level


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

    public static String getSIM_OperatorName() {
        return simOperator_name;

    }

    public static void setSIM_OperatorName(String simOperator_Name) {

        DeviceInfo.simOperator_name = simOperator_Name;
    }

    public static int getApiLevel() {
        return apiLevel;
    }

    public static void setApiLevel(int apiLevel) {
        DeviceInfo.apiLevel = apiLevel;
    }


}