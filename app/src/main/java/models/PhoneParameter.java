package models;

import java.util.ArrayList;

import setting.EnumValues;
import setting.EnumValuesStorage;

/**
 * Created by Tejas on 4/14/2016.
 */

public class PhoneParameter {

    ArrayList<PhoneAppInfo> objAppInfoList;

    private int paramScreenLock;     // Screen lock value (range: -1 to 11)
    private int paramBatteryHealth;  // BatteryHealth values (range: -1, 1 to 7)

    private EnumValues paramKeySecure;      // KeyGuard secured  status
    private EnumValues paramDeviceSecure;   // Device secured  status
    private EnumValues paramRootStatus;     // Device root access status

    private EnumValuesStorage paramStorageEncrypt;     // Storage Encryption status

    private boolean paramAntivirusPresent;  // Antivirus status true/false

    public EnumValuesStorage getParamStorageEncrypt() {
        return paramStorageEncrypt;
    }

    public void setParamStorageEncrypt(EnumValuesStorage paramStorageEncrypt) {
        this.paramStorageEncrypt = paramStorageEncrypt;
    }


    public PhoneParameter() {
        objAppInfoList =new ArrayList<PhoneAppInfo>();
    }

    public void addAppDetails(String packageInfo, String installerInfo) {

        PhoneAppInfo objAppData =new PhoneAppInfo(packageInfo,installerInfo);
        objAppInfoList.add(objAppData);

    }



    public int getParamScreenLock() {
        return paramScreenLock;
    }

    public void setParamScreenLock(int paramScreenLock) {
        this.paramScreenLock = paramScreenLock;
    }

    public int getParamBatteryHealth() {
        return paramBatteryHealth;
    }

    public void setParamBatteryHealth(int paramBatteryHealth) {
        this.paramBatteryHealth = paramBatteryHealth;
    }

    public EnumValues getParamKeySecure() {
        return paramKeySecure;
    }

    public void setParamKeySecure(EnumValues paramKeySecure) {
        this.paramKeySecure = paramKeySecure;
    }

    public EnumValues getParamDeviceSecure() {
        return paramDeviceSecure;
    }

    public void setParamDeviceSecure(EnumValues paramDeviceSecure) {
        this.paramDeviceSecure = paramDeviceSecure;
    }

    public EnumValues getParamRootStatus() {
        return paramRootStatus;
    }

    public void setParamRootStatus(EnumValues paramRootStatus) {
        this.paramRootStatus = paramRootStatus;
    }

    public boolean isParamAntivirusPresent() {
        return paramAntivirusPresent;
    }

    public void setParamAntivirusPresent(boolean paramAntivirusPresent) {
        this.paramAntivirusPresent = paramAntivirusPresent;
    }

}

