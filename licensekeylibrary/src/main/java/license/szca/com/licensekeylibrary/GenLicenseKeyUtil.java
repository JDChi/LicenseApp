package license.szca.com.licensekeylibrary;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

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
     * @param input 用户输入的内容
     * @return lincense 长度为20位的数字证书（由大写字母和数字构成）
     */
    public String genLicense(String input) {

        //2.获取证书的原始数据
        String licenseData = genLicenseData(input);
        //3.对拼接后的数据使用SHA1做信息摘要
        byte[] sha1Data = shaUtil.encodeSHA1(licenseData);
        //4.对摘要后的数据做Hex编码，得到要输入的长度20位，大写字母和数字组成的证书
        mLicenseKey = codeUtil.hexData(sha1Data);

        return mLicenseKey;
    }

    /**
     * 获取要提交给服务端的数据
     *
     * @return
     */
    public String getSubmitData(String licenseKey) {

        //获取要提交的数据并加密（由于数据过长，使用aes对称加密）
        String clientData = genClientData(licenseKey);
        String aesEncryptClientData = new String(aesUtil.encryptData(clientData , aesUtil.getAESSecretKey()));
        //获取aes的加密密钥
        byte[] aesKey = aesUtil.getAESSecretKey();
        //对aes的密钥进行rsa加密
//        String encryptAESKey = new String(Base64.encode(rsaUtil.encryptWithPrivateKey(aesKey)));
        //对aes加密后客户端数据，aes密钥，rsa公钥打包成json返回
//        String submitData = genSubmitData(aesEncryptClientData , encryptAESKey , new String(Base64.encode(rsaUtil.getPublicKey())));


//        return submitData;
        return null;
    }

    /**
     * 提交给服务端的数据
     * @param clientData 被AES加密过的数据
     * @param encryptAESKey AES密钥，用于解密出客户端数据
     * @param rsaPublicKey RSA公钥，用于解密出AES密钥
     * @return
     */
    private String genSubmitData(String clientData,String encryptAESKey, String rsaPublicKey) {
        RootSubmitData rootSubmitData = new RootSubmitData();
        rootSubmitData.setEncryptAESClientData(clientData);
        rootSubmitData.setEncrptAESKey(encryptAESKey);
        rootSubmitData.setRsaPublicKey(rsaPublicKey);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("clientData" , rootSubmitData.getEncryptAESClientData());
            jsonObject.put("encryptAESKey" , rootSubmitData.getEncrptAESKey());
            jsonObject.put("rsaPublicKey" , rootSubmitData.getRsaPublicKey());




        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject.toString();
    }

    /**
     * 最后要提交给客户端的数据包括：
     * 1.通过aes加密后的clientData(username，uuid，applicationId，license）
     * 2.通过rsa加密后的aes密钥
     * 3.rsa的公钥
     * @return
     */
    private String genSubmitData() {
        return null;
    }

    /**
     * 获取要传给服务端的RSA公钥
     * @return
     */
    public byte[] getSubmitRSAPublicKey(){
        return rsaUtil.getPublicKey();
    }


    /**
     * 生成证书里的源数据（证书里的数据包括用户名，uuid和包名）
     *
     * @param input
     * @return
     */
    private String genLicenseData(String input) {

        mRootData.setUserName(input);
        mRootData.setUuid(getDeviceInfoUtil.getUUID());
        mRootData.setApplicationId(getDeviceInfoUtil.getAppPackageName());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", mRootData.getUserName());
            jsonObject.put("uuid", mRootData.getUuid());
            jsonObject.put("applicationId", mRootData.getApplicationId());
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
