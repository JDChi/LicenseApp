package license.szca.com.licenseapp.presenter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import license.szca.com.licenseapp.service.BackService;
import license.szca.com.licenseapp.view.IRegistView;
import license.szca.com.licensekeylibrary.GenLicenseKeyUtil;

import static license.szca.com.licenseapp.common.Constant.MSG_CHECK_LICENSE;
import static license.szca.com.licenseapp.common.Constant.MSG_GET_LICENSE;

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
    private byte[] mRSAPublicKey;
    private Messenger mToServiceMessenger;
    private Messenger mReplyMessenger = new Messenger(new replyHandler());



    //处理服务端返回的结果
    private class replyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //返回证书和RSA公钥
                case MSG_GET_LICENSE:
                    String license = msg.getData().getString("license");
                    mRSAPublicKey = msg.getData().getByteArray("rsaPublicKey");
                    //填写到证书输入框里
                    mIRegistView.getLicenseKeySuccess(license);
                    break;
                //返回校验结果
                case MSG_CHECK_LICENSE:
                    boolean isSuccess = msg.getData().getBoolean("success");
                    if (isSuccess) {
                        Log.d(TAG , "注册成功");
                        mIRegistView.checkLicenseKeySuccess("注册成功");
                    }else {
                        mIRegistView.checkLicenseKeyFailed("注册失败");
                        Log.d(TAG , "注册失败");
                    }
                    break;
            }


        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mToServiceMessenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public RegistPresenter(Activity context, IRegistView iRegistView) {
        this.mContext = context;
        this.mIRegistView = iRegistView;
        genLicenseKeyUtil = new GenLicenseKeyUtil(mContext);
        //绑定服务
        Intent intent = new Intent(mContext, BackService.class);
        mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    public void onStop() {
        mContext.unbindService(serviceConnection);
    }

    /**
     * 获取证书
     *
     * @param name
     */
    public void genLicenseKey(String name) {
        if (TextUtils.isEmpty(name)) {
            mIRegistView.getLicenseKeyFailed("请输入用户名");
            return;
        }

        mIRegistView.showLoading("正在获取证书");
        callServiceToGetLicense(name);

    }

    /**
     * 发送用户数据给服务端，让服务端生成证书
     *
     * @param input
     */
    private void callServiceToGetLicense(String input) {
        try {
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("userData", genLicenseKeyUtil.getDataToGenLicense(input));
            message.what = MSG_GET_LICENSE;
            message.setData(bundle);
            message.replyTo = mReplyMessenger;
            mToServiceMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    /**
     * 提交处理后的数据给服务端校验
     * @param licenseKey
     * @return
     */
    public void submitData(String licenseKey) {
        if (TextUtils.isEmpty(licenseKey)) {
            mIRegistView.checkLicenseKeyFailed("请输入证书");
            return;
        }

        mIRegistView.showLoading("正在校验");
        String submitData = genLicenseKeyUtil.getSubmitData(licenseKey , mRSAPublicKey);

        callServiceToCheck(submitData);
    }

    /**
     * 通知服务端校验
     * @param submitData 提交的数据
     */
    private void callServiceToCheck(String submitData) {
        try {
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("submitData" , submitData);
            message.what = MSG_CHECK_LICENSE;
            message.replyTo = mReplyMessenger;
            message.setData(bundle);
            mToServiceMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}
