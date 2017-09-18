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

import org.spongycastle.util.encoders.Base64;

import license.szca.com.licensekeylibrary.AESUtil;
import license.szca.com.licensekeylibrary.RSAUtil;
import license.szca.com.licensekeylibrary.RootSubmitData;

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
    private String mClientData = null;
    private final String TAG = "CheckLicenseService";
    private RSAUtil rsaUtil;
    private AESUtil aesUtil;




    @Override
    public void onCreate() {
        super.onCreate();
        rsaUtil = new RSAUtil();
        aesUtil = new AESUtil();
    }

    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CLIENT_DATA:
                    //接收来自客户端发过来的数据
                    if (receiveDatafromClient(msg)) {
                        //处理客户端发送过来的数据
                        analyzeData(msg);
                    } else {
                        //回复客户端失败
                        replyClientError();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * 回复客户端接收错误
     */
    private void replyClientError() {

    }

    /**
     * 处理客户端发送过来的数据
     *
     * @param msg
     */
    private void analyzeData(Message msg) {

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
        mSubmitData = msg.getData().getString("submitData");
        Log.d(TAG, "获取到客户端发来的数据：" + mSubmitData);
        Gson gson = new Gson();
        RootSubmitData rootSubmitData = gson.fromJson(mSubmitData, RootSubmitData.class);
        mRSAPublicKey = Base64.decode(rootSubmitData.getRsaPublicKey().getBytes());

        mAESKey = Base64.decode(rootSubmitData.getEncrptAESKey().getBytes());
        mClientData = rootSubmitData.getEncryptAESClientData();
        //用RSA公钥解密出AES密钥
        byte[] aesKeyByte = rsaUtil.decryptWithPublicKey(mAESKey, mRSAPublicKey);
//        String result = aesUtil.decryptData(mClientData , aesKeyByte);
//        Log.d(TAG, "结果是：" + result);

        return true;

    }

    private Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
