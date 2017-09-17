package license.szca.com.licenseapp.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import org.spongycastle.util.encoders.Base64;
import org.spongycastle.util.encoders.Hex;

import java.util.Iterator;
import java.util.List;

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
    private String sign;

    public RegistPresenter(Activity context, IRegistView iRegistView) {
        this.mContext = context;
        this.mIRegistView = iRegistView;
        genLicenseKeyUtil = new GenLicenseKeyUtil(mContext);
        sign = getSign(mContext);

    }

    public void genLicenseKey(String name) {
        if (TextUtils.isEmpty(name)) {
            mIRegistView.genLicenseKeyFailed("请输入用户名");
            return;
        }

        mIRegistView.genLicenseKeySuccess(genLicenseKeyUtil.genLicense(name));
        Log.d(TAG, genLicenseKeyUtil.genLicense(name));
    }

    public void checkLicenseKey(String licenseKey) {
String result = genLicenseKeyUtil.getSubmitData();
        int i = 0;
    }

    /**
     * 获取应用签名
     * @param context
     * @return
     */
    public String getSign(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm
                .getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();

        while (iter.hasNext()) {
            PackageInfo info = iter.next();
            String packageName = info.packageName;
            //按包名 取签名
            if (packageName.equals("license.szca.com.licenseapp")) {
                byte[] signByte = info.signatures[0].toByteArray();

                return new String( Hex.encode(signByte));

            }
        }
        return null;
    }
}
