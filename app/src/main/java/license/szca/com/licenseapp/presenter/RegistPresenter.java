package license.szca.com.licenseapp.presenter;

import android.app.Activity;
import android.text.TextUtils;

import license.szca.com.licenseapp.view.IRegistView;
import license.szca.com.licensekeylibrary.GenLicenseKeyUtil;

/**
 * description
 * Created by JD
 * on 2017/9/12.
 */

public class RegistPresenter {

    private final String TAG = "RegistPresenter";
    private Activity mContext;
    private IRegistView mIRegistView;
    private GenLicenseKeyUtil genLicenseKeyUtil;


    public RegistPresenter(Activity context, IRegistView iRegistView) {
        this.mContext = context;
        this.mIRegistView = iRegistView;
        genLicenseKeyUtil = new GenLicenseKeyUtil(mContext);


    }

    /**
     * 获取证书
     * @param name
     */
    public void genLicenseKey(String name) {
        if (TextUtils.isEmpty(name)) {
            mIRegistView.genLicenseKeyFailed("请输入用户名");
            return;
        }

        mIRegistView.genLicenseKeySuccess(genLicenseKeyUtil.genLicense(name));
//        Log.d(TAG, genLicenseKeyUtil.genLicense(name));
    }

    public String submitData(String licenseKey) {
        if (TextUtils.isEmpty(licenseKey)) {
            mIRegistView.checkLicenseKeyFailed("请输入证书");
            return null;
        }
        return genLicenseKeyUtil.getSubmitData(licenseKey);
    }
}
