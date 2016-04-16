package models;

import java.util.ArrayList;

import setting.EnumValues;
import setting.EnumValuesStorage;

/**
 * Created by Tejas on 4/14/2016.
 */

public class PhoneParameter {

    ArrayList<PhoneAppInfo> objAppInfoList;

    private boolean paramAntivirusPresent;  // Antivirus status true/false

    private int paramScreenLock;     // Screen lock value (range: -1 to 11)
    private int paramBatteryHealth;  // BatteryHealth values (range: -1, 1 to 7)

    // To be implemented
    private EnumValues paramSIM_Lock;      // SIM_Lock  status
    private EnumValues paramRootStatus;     // Device root access status

    private EnumValuesStorage paramStorageEncrypt;     // Storage Encryption status




    public EnumValuesStorage getParamStorageEncrypt() {
        return paramStorageEncrypt;
    }

    public void setParamStorageEncrypt(EnumValuesStorage paramStorageEncrypt) {
        this.paramStorageEncrypt = paramStorageEncrypt;
    }

    public PhoneParameter() {
        objAppInfoList =new ArrayList<PhoneAppInfo>();
    }

    public void addAppDetails(String appNameInfo, String packageInfo, String installerInfo) {

        PhoneAppInfo objAppData =new PhoneAppInfo(appNameInfo,packageInfo,installerInfo);
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

