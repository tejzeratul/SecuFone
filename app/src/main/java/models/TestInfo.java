package models;

/**
 * Created by Tejas on 4/15/2016.
 */
public class TestInfo {

    private PhoneParameter objPhoneParamTO;
    private DeviceInfo objDeviceInfoTO;
    private String userEmail;
    private String timeStamp;
    private String deviceName;
    private String androidID;

    public TestInfo (PhoneParameter objPhoneParamTO, DeviceInfo objDeviceInfoTO, String timeStamp, String userEmail, String deviceName,String androidId) {

        this.objPhoneParamTO=objPhoneParamTO;
        this.objDeviceInfoTO=objDeviceInfoTO;
        this.userEmail=userEmail;
        this.timeStamp=timeStamp;
        this.deviceName=deviceName;
        this.androidID=androidId;
    }

}
