package license.szca.com.licenseapp.view;

/**
 * description
 * Created by JD
 * on 2017/9/12.
 */

public interface IRegistView {
    void genLicenseKeyFailed(String msg);

    void genLicenseKeySuccess(String key);

    void checkLicenseKeySuccess(String msg);

    void checkLicenseKeyFailed(String msg);
}
