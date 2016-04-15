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

    TestInfo (PhoneParameter objPhoneParamTO, DeviceInfo objDeviceInfoTO,String userEmail, String timeStamp, String deviceName) {

        this.objPhoneParamTO=objPhoneParamTO;
        this.objDeviceInfoTO=objDeviceInfoTO;
        this.userEmail=userEmail;
        this.timeStamp=timeStamp;
        this.deviceName=deviceName;
    }

}
