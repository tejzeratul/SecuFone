package evaluations;

import android.app.admin.DevicePolicyManager;
import android.content.Context;

import com.tejtron.secufone.MainActivity;

import setting.EnumValuesStorage;

/**
 * Created by Tejas on 4/16/2016.
 */
public class StorageEncrypt {

    private Context sContext;

    public StorageEncrypt() {
        sContext = MainActivity.getContext();
    }

    public EnumValuesStorage getParamStorage() {
        DevicePolicyManager dpm = (DevicePolicyManager) sContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        int enStatus = dpm.getStorageEncryptionStatus();

        EnumValuesStorage result = EnumValuesStorage.UNKNOWN;

        if (enStatus == android.app.admin.DevicePolicyManager.ENCRYPTION_STATUS_UNSUPPORTED)
            result = EnumValuesStorage.UNSUPPORTED;
        else {
            if (enStatus == android.app.admin.DevicePolicyManager.ENCRYPTION_STATUS_INACTIVE)
                result = EnumValuesStorage.INACTIVE;
            else {
                if (enStatus == android.app.admin.DevicePolicyManager.ENCRYPTION_STATUS_ACTIVATING)
                    result = EnumValuesStorage.ACTIVATING;
                else {
                    if (enStatus == android.app.admin.DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE)
                        result = EnumValuesStorage.ACTIVE;
                }
            }
        }

        return result;
    }
}
