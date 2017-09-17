package license.szca.com.licensekeylibrary;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.util.encoders.Hex;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static javax.crypto.Cipher.PRIVATE_KEY;

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
        //1.随机初始化密钥和公钥
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
        //4.对摘要后的数据做Hex编码
        mLicenseKey = codeUtil.hexData(sha1Data);

        return mLicenseKey;
    }

    public String getSubmitData(){
        //获取要提交给服务端的数据
        String submitData = genData();
        //数据过长，使用aes对称加密
String data = aesUtil.encryptData(submitData);




        //将数据进行rsa加密
        byte[] encryptDataByte = rsaUtil.encryptRSAPublicKey(submitData);

        return new String(encryptDataByte);
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
     * 生成要提交的原始数据，即在用户名，uuid，applicaitonId的基础上
     * 加入证书
     *
     * @return
     */
    private String genData() {


        mRootData.setLicenseKey(mLicenseKey);
        mRootData.setAppSignture(getDeviceInfoUtil.getSign());
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("licenseKey", mRootData.getLicenseKey());
            jsonObject.put("appSignature", mRootData.getAppSignture());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject.toString();
    }

    public void getPublicKey(){

    }


}
