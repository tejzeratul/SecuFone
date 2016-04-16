package models;

import java.util.Date;

/**
 * Created by Tejas on 4/15/2016.
 */
public class TestInfo {

    private PhoneParameter objPhoneParamTO;
    private DeviceInfo objDeviceInfoTO;
    private String userEmail;
    private Date timeStamp;
    private String deviceName;

    public TestInfo (PhoneParameter objPhoneParamTO, DeviceInfo objDeviceInfoTO, Date timeStamp, String userEmail, String deviceName) {

        this.objPhoneParamTO=objPhoneParamTO;
        this.objDeviceInfoTO=objDeviceInfoTO;
        this.userEmail=userEmail;
        this.timeStamp=timeStamp;
        this.deviceName=deviceName;
    }

}
