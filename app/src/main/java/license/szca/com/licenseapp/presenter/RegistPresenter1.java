package license.szca.com.licenseapp.presenter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.jdnew.serverapp.aidl.ILicense;
import com.jdnew.serverapp.aidl.RootLicenseData;

import license.szca.com.licenseapp.view.IRegistView1;
import license.szca.com.licensekeylibrary.GenLicenseKeyUtil;

/**
 * description :
 * author : JDNew
 * on : 2017/9/24.
 */

public class RegistPresenter1 {

    private Activity mContext;
    private IRegistView1 mIRegistView1;
    private GenLicenseKeyUtil genLicenseKeyUtil;
    private byte[] mRSAPublicKey;



    public RegistPresenter1(Activity context , IRegistView1 iRegistView1){
        this.mContext = context;
        this.mIRegistView1 = iRegistView1;
        genLicenseKeyUtil = new GenLicenseKeyUtil(mContext);

        Intent intent = new Intent();
        intent.setAction("com.jdnew.serverapp.service.LicenseService");
        intent.setPackage("com.jdnew.serverapp");
        mContext.bindService(intent , mServiceConnection , Context.BIND_AUTO_CREATE);




    }

    public void genLicenseKey(String name) {
        if (TextUtils.isEmpty(name)) {
            mIRegistView1.getLicenseKeyFailed("请输入用户名");
            return;
        }

        mIRegistView1.showLoading("正在获取证书");
        try {

           RootLicenseData rootLicenseData = mILicense.returnLicense(genLicenseKeyUtil.getDataToGenLicense(name)) ;
            mIRegistView1.getLicenseKeySuccess(rootLicenseData.getLicense());
            mRSAPublicKey = rootLicenseData.getRsaPublicKey();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void submitData(String licenseKey) {
        if (TextUtils.isEmpty(licenseKey)) {
            mIRegistView1.checkLicenseKeyFailed("请输入证书");
            return;
        }

        String submitData = genLicenseKeyUtil.getSubmitData(licenseKey , mRSAPublicKey);

        try {
            String result = mILicense.returnCheckResult(submitData);
            if (result.equals("true")) {
              mIRegistView1.checkLicenseKeySuccess("验证成功");
            }else {
                mIRegistView1.checkLicenseKeyFailed("验证失败");
            }


        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onStop() {

    }



    private ILicense mILicense;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mILicense = ILicense.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
