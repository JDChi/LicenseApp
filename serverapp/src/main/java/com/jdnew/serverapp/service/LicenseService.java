package com.jdnew.serverapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.jdnew.serverapp.aidl.ILicense;
import com.jdnew.serverapp.aidl.RootLicenseData;

import org.spongycastle.util.encoders.Hex;

import license.szca.com.licensekeylibrary.AESUtil;
import license.szca.com.licensekeylibrary.CodeUtil;
import license.szca.com.licensekeylibrary.RSAUtil;
import license.szca.com.licensekeylibrary.RootData;
import license.szca.com.licensekeylibrary.RootSubmitData;
import license.szca.com.licensekeylibrary.SHAUtil;


/**
 * description :
 * author : JDNew
 * on : 2017/9/24.
 */

public class LicenseService extends Service {

    private SHAUtil shaUtil;
    private CodeUtil codeUtil;
    private RSAUtil rsaUtil;
    private AESUtil aesUtil;
    private RootLicenseData rootLicenseData;
    private final String TAG = "LicenseService";
    private Binder linsenseBinder = new ILicense.Stub() {

        @Override
        public com.jdnew.serverapp.aidl.RootLicenseData returnLicense(String licenseData) throws RemoteException {

            byte[] sha1Data = shaUtil.encodeSHA1(licenseData.getBytes());
            String licenseKey = codeUtil.hexData(sha1Data);
            //获取RSA的公钥
            byte[] rsaPublicKey = rsaUtil.getPublicKey();
            RootLicenseData rootLicenseData = new RootLicenseData(licenseKey, rsaPublicKey);

            return rootLicenseData;
        }

        @Override
        public String returnCheckResult(String submitData) throws RemoteException {

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
                return "true";
            } else {
               return "false";
            }

        }
    };

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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return linsenseBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        shaUtil = new SHAUtil();
        rsaUtil = new RSAUtil();
        codeUtil = new CodeUtil();
        aesUtil = new AESUtil();
        rsaUtil.initPrivateAndPublicKey();
    }
}
