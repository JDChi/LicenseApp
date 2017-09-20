package license.szca.com.licensekeylibrary;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.util.encoders.Hex;

import java.security.Security;

/**
 * description
 * Created by JD
 * on 2017/9/12.
 */

public class GenLicenseKeyUtil {

    static {
        //从位置1开始，添加新的提供者
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    private final String TAG = "GenLicenseKeyUtil";

    private GetDeviceInfoUtil getDeviceInfoUtil;
    private Context mContext;
    private String mLicenseKey = null;
    private String mAppSignature = null;
    private CodeUtil codeUtil;
    private RSAUtil rsaUtil;
    private SHAUtil shaUtil;
    private RootData mRootData;
    private AESUtil aesUtil;

    public GenLicenseKeyUtil(Context context) {
        mContext = context;
        getDeviceInfoUtil = new GetDeviceInfoUtil(mContext);
        codeUtil = new CodeUtil();
        rsaUtil = new RSAUtil();
        shaUtil = new SHAUtil();
        aesUtil = new AESUtil();
        mRootData = new RootData();
        //1.初始化密钥和公钥
        rsaUtil.initPrivateAndPublicKey();
    }



    /**
     * 获取要提交给服务端生成证书的数据
     * @param input
     * @return
     */
    public String getDataToGenLicense(String input){

        mRootData.setUserName(input);
        mRootData.setUuid(getDeviceInfoUtil.getUUID());
        mRootData.setApplicationId(getDeviceInfoUtil.getAppPackageName());

        return mRootData.getUserName() + mRootData.getUuid() + mRootData.getApplicationId();
    }

    /**
     * 获取要提交给服务端校验的数据
     *
     * @return
     */
    public String getSubmitData(String licenseKey, byte[] RSAPublicKey) {

        //获取要提交的客户端数据
        String clientData = genClientData(licenseKey);
        //由于数据过长，无法进行RSA加密，所以使用AES加密客户端数据
        byte[] aesEncryptClientByte = aesUtil.encryptData(Hex.encode(clientData.getBytes()) , aesUtil.getAESSecretKey());
        //获取AES的加密密钥
        byte[] aesKey = aesUtil.getAESSecretKey();
        //用服务端发过来的RSA公钥对AES的密钥进行加密
        byte[] encryptAESKey = rsaUtil.encryptWithPublicKey(aesKey , RSAPublicKey);
        //使用RSA对数据进行签名
        byte[] signData = rsaUtil.signWithPrivateKey(clientData.getBytes() , rsaUtil.getPrivateKey());
        //获取RSA的公钥
        byte[] rsaSignPublicKey = rsaUtil.getPublicKey();


        //对aes加密后客户端数据，aes密钥，rsa公钥三者打包成json返回
        String submitData = genSubmitData(aesEncryptClientByte , encryptAESKey , signData , rsaSignPublicKey);


        return submitData;

    }

    /**
     * 转换成json，提交给服务端的数据
     * @param clientData 被AES加密过的数据
     * @param encryptAESKey AES密钥，用于解密出客户端数据
     * @param signData 签名后的数据
     *@param rsaSignPublicKey RSA签名公钥
     * @return
     */
    private String genSubmitData(byte[] clientData, byte[] encryptAESKey, byte[] signData, byte[] rsaSignPublicKey) {
        RootSubmitData rootSubmitData = new RootSubmitData();
        rootSubmitData.setEncryptAESClientData(new String(Hex.encode(clientData)));
        rootSubmitData.setEncrptAESKey(new String(Hex.encode(encryptAESKey)));
        rootSubmitData.setSignData(new String(Hex.encode(signData)));
        rootSubmitData.setRsaSignPublicKey(new String(Hex.encode(rsaSignPublicKey)));


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("encryptAESClientData" , rootSubmitData.getEncryptAESClientData());
            jsonObject.put("encrptAESKey" , rootSubmitData.getEncrptAESKey());
            jsonObject.put("signData" , rootSubmitData.getSignData());
            jsonObject.put("rsaSignPublicKey" , rootSubmitData.getRsaSignPublicKey());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject.toString();
    }




    /**
     * 生成客户端数据，即在用户名，uuid，applicaitonId的基础上
     * 加入证书
     *
     * @return
     */
    private String genClientData(String licenseKey) {


        mRootData.setLicenseKey(licenseKey);
//        mRootData.setAppSignture(getDeviceInfoUtil.getSign());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", mRootData.getUserName());
            jsonObject.put("uuid", mRootData.getUuid());
            jsonObject.put("applicationId", mRootData.getApplicationId());
            jsonObject.put("licenseKey", mRootData.getLicenseKey());
//            jsonObject.put("appSignature", mRootData.getAppSignture());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject.toString();
    }




}
