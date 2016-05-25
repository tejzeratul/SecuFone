package setting;

public class AdvisoryClassify {

    // Advisory higher group (classes)
    public static final String CLASS_SETTING = "Settings";
    public static final String CLASS_APPS = "Applications";

    // Advisory lower group (subclasses)
    public static final String SUBCLASS_APP_SOURCE = "Apps Source";
    public static final String SUBCLASS_SETTING_GEN = "Setting";

    // Advisory: general message
    public static final String APPS_MSG1 = "Source not traceable for app ";

    // Advisory: specific message
    public static final String ROOT_MSG = "Device is rooted, malware can easily breach";
    public static final String STORAGE_MSG = "Storage is not encrypted, enable full-disk encryption";
    public static final String ANDROID_SW_MSG = "Android software version is old, try updating OS software";
    public static final String SIM_LOCK_MSG = "SIM is not PIN protected, try enabling to secure device";
}

