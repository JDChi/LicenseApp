package license.szca.com.licenseapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
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

/**
 * description : 一个用于检验证书的服务，在新的进程里
 * author : JDNew
 * on : 2017/9/17.
 */

public class CheckLicenseService extends Service {

    private final int MSG_CLIENT_DATA = 1;
    private String mSubmitData;
    private byte[] mRSAPublicKey = null;
    private byte[] mAESKey = null;
    private byte[] mClientData = null;
    private final String TAG = "CheckLicenseService";
    private RSAUtil rsaUtil;
    private AESUtil aesUtil;
private CodeUtil codeUtil;
    private SHAUtil shaUtil;



    @Override
    public void onCreate() {
        super.onCreate();
        rsaUtil = new RSAUtil();
        aesUtil = new AESUtil();
        codeUtil = new CodeUtil();
        shaUtil = new SHAUtil();
    }

    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CLIENT_DATA:
                    //接收来自客户端发过来的数据
                    if (receiveDatafromClient(msg)) {
                        //回复客户端成功
                        replySuccess(msg);
                    } else {
                        //回复客户端失败
                        replyError(msg);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * 回复客户端接收错误
     * @param msg
     */
    private void replyError(Message msg) {

    }

    /**
     * 处理客户端发送过来的数据
     *
     * @param msg
     */
    private void replySuccess(Message msg) {

//        try {
//            Messenger messenger = msg.replyTo;
//            Message message = new Message();
//            Bundle bundle = new Bundle();
//            bundle.putBoolean("success" , true);
//            message.setData(bundle);
//            messenger.send(message);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * 接收客户端提交的数据
     *
     * @param msg
     * @return
     */
    private boolean receiveDatafromClient(Message msg) {

        try {
            mSubmitData = msg.getData().getString("submitData");
            Log.d(TAG, "获取到客户端发来的数据：" + mSubmitData);
            Gson gson = new Gson();
            RootSubmitData rootSubmitData = gson.fromJson(mSubmitData, RootSubmitData.class);
            mRSAPublicKey = Hex.decode(rootSubmitData.getRsaPublicKey());
            mAESKey = Hex.decode(rootSubmitData.getEncrptAESKey());
            mClientData = Hex.decode(rootSubmitData.getEncryptAESClientData());
            //用RSA公钥解密出AES密钥
            byte[] aesKeyByte = rsaUtil.decryptWithPublicKey(mAESKey, mRSAPublicKey);
            //用AES密钥解密出客户端数据
            byte[] resultByte = aesUtil.decryptData(mClientData , aesKeyByte);


            //用解密出的AES密钥解密数据
            String result = new String(Hex.decode(resultByte));

            RootData rootData = gson.fromJson(result , RootData.class);
            String data = rootData.getUserName()
                    + rootData.getUuid()
                    + rootData.getApplicationId();


            String newLicense = codeUtil.hexData(
                    shaUtil.encodeSHA1(data.getBytes())
            );
            //判断由username，uuid和applicationId拼接并哈希后的值与客户端传过来的证书是否一致
            if(newLicense.equals(rootData.getLicenseKey())){
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
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
