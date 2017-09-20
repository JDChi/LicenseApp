package license.szca.com.licenseapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import org.spongycastle.util.encoders.Hex;

import license.szca.com.licensekeylibrary.AESUtil;
import license.szca.com.licensekeylibrary.CodeUtil;
import license.szca.com.licensekeylibrary.RSAUtil;
import license.szca.com.licensekeylibrary.RootData;
import license.szca.com.licensekeylibrary.RootSubmitData;
import license.szca.com.licensekeylibrary.SHAUtil;

import static license.szca.com.licenseapp.common.Constant.MSG_CHECK_LICENSE;
import static license.szca.com.licenseapp.common.Constant.MSG_GET_LICENSE;


/**
 * description 用Messenger（AIDL）通信的进程服务
 * Created by JD
 * on 2017/9/20.
 */

public class BackService extends Service {


    private SHAUtil shaUtil;
    private CodeUtil codeUtil;
    private RSAUtil rsaUtil;
    private AESUtil aesUtil;
    private final String TAG = "BackService";

    @Override
    public void onCreate() {
        super.onCreate();
        shaUtil = new SHAUtil();
        rsaUtil = new RSAUtil();
        codeUtil = new CodeUtil();
        aesUtil = new AESUtil();
        rsaUtil.initPrivateAndPublicKey();
    }

    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //生成证书
                case MSG_GET_LICENSE:
                    genLicense(msg);
                    break;
                //校验数据
                case MSG_CHECK_LICENSE:
                    checkData(msg);
                    break;

            }


        }
    }

    /**
     * 生成证书，并连同RSA公钥发给客户端
     *
     * @param msg
     */
    private void genLicense(Message msg) {
        String clientData = msg.getData().getString("userData");
        byte[] sha1Data = shaUtil.encodeSHA1(clientData.getBytes());
        String licenseKey = codeUtil.hexData(sha1Data);
        //获取RSA的公钥
        byte[] rsaPublicKey = rsaUtil.getPublicKey();
        //把证书和公钥发给客户端：
        try {
            Messenger replyMessenger = msg.replyTo;
            Message replyMessage = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("license", licenseKey);
            bundle.putByteArray("rsaPublicKey", rsaPublicKey);
            replyMessage.what = MSG_GET_LICENSE;
            replyMessage.setData(bundle);
            replyMessenger.send(replyMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验数据
     *
     * @param msg
     */
    private void checkData(Message msg) {
        String submitData = msg.getData().getString("submitData");
        Gson gson = new Gson();
        RootSubmitData rootSubmitData = gson.fromJson(submitData, RootSubmitData.class);
        byte[] aesKey = Hex.decode(rootSubmitData.getEncrptAESKey());
        byte[] encryptClientData = Hex.decode(rootSubmitData.getEncryptAESClientData());
        byte[] signData = Hex.decode(rootSubmitData.getSignData());
        byte[] rsaSignPublicKey = Hex.decode(rootSubmitData.getRsaSignPublicKey());


        //用RSA私钥解密出AES密钥
        byte[] aesKeyByte = rsaUtil.decryptWithPrivateKey(aesKey, rsaUtil.getPrivateKey());
        //用AES密钥解密出客户端数据
        byte[] decryptClientData = aesUtil.decryptData(encryptClientData, aesKeyByte);
        String result = new String(Hex.decode(decryptClientData));
        Log.d(TAG, result);

        if (isLicenseEqual(result) && isSignCorrect(result, signData, rsaSignPublicKey)) {
            replyResult(true, msg);
        } else {
            replyResult(false, msg);
        }


    }

    /**
     * 签名校验结果
     *
     * @param result
     * @param signData
     * @param rsaSignPublicKey
     * @return
     */
    private boolean isSignCorrect(String result, byte[] signData, byte[] rsaSignPublicKey) {
        return rsaUtil.verify(result.getBytes(), rsaSignPublicKey, signData);
    }

    /**
     * 返回结果给客户端
     *
     * @param result
     * @param msg
     */
    private void replyResult(boolean result, Message msg) {
        try {
            Messenger replyMessenger = msg.replyTo;
            Message replyMessage = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean("success", result);
            replyMessage.what = MSG_CHECK_LICENSE;
            replyMessage.setData(bundle);
            replyMessenger.send(replyMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验客户端传过来的证书和服务端生成的证书是否一致
     *
     * @param result
     * @return
     */
    private boolean isLicenseEqual(String result) {
        Gson gson = new Gson();
        RootData rootData = gson.fromJson(result, RootData.class);
        String data = rootData.getUserName()
                + rootData.getUuid()
                + rootData.getApplicationId();
        String newLicense = codeUtil.hexData(
                shaUtil.encodeSHA1(data.getBytes())
        );
        //判断由username，uuid和applicationId拼接并哈希后的值与客户端传过来的证书是否一致
        if (newLicense.equals(rootData.getLicenseKey())) {
            Log.d(TAG, "校验成功");
            return true;

        } else {
            Log.d(TAG, "校验失败");
            return false;
        }
    }


    private Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
