package models;

/**
 * Created by Tejas on 4/15/2016.
 */

class PhoneAppInfo {

    private String paramPackageInfo;    // Application package name
    private String paramInstallerInfo;  // Application installer info

    PhoneAppInfo(String paramPackageInfo, String paramInstallerInfo) {

        this.paramPackageInfo=paramPackageInfo;
        this.paramInstallerInfo=paramInstallerInfo;

    }

    public String getParamPackageInfo() {
        return paramPackageInfo;
    }

    public void setParamPackageInfo(String paramPackageInfo) {
        this.paramPackageInfo = paramPackageInfo;
    }

    public String getParamInstallerInfo() {
        return paramInstallerInfo;
    }

    public void setParamInstallerInfo(String paramInstallerInfo) {
        this.paramInstallerInfo = paramInstallerInfo;
    }
}