package license.szca.com.licensekeylibrary;

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
    private final String PUBLIC_KEY = "publicKey";
    private final String PRIVATE_KEY = "privateKey";
    private Map<String, Key> mKeyMap = new HashMap<>();
    private String mOriginData;
    private GetDeviceInfoUtil getDeviceInfoUtil;

    public GenLicenseKeyUtil() {
        //1.随机初始化密钥和公钥
        initPrivateAndPublicKey();
        getDeviceInfoUtil = new GetDeviceInfoUtil();
    }

    /**
     * @param input 用户输入的内容
     * @return lincense 长度为20位的数字证书（由大写字母和数字构成）
     */
    public String genLicense(String input) {

        //2.获取要加密的原始数据
        mOriginData = genData(input);
        //3.对拼接后的数据使用SHA1做信息摘要
        byte[] sha1Data = encodeSHA1(mOriginData);
        //4.对摘要后的数据做Hex编码
        String licenseKey = hexData(sha1Data);

        return licenseKey;
    }

    /**
     * 对用户输入的内容进行一系列的算法运算，返回license
     *
     * @param input
     * @return
     */
    private String encryptData(String input) {


//        //4.对摘要后的数据使用RSA公钥加密
//        byte[] encryptData = encryptRSAPublicKey(sha1Data);
//        //5.对加密后的数据进行Hex编码
//        String hexData = hexData(encryptData);
//        byte[] signData = signWithPrivateKey(hexData);
//        String sighHexData = hexData(signData);

        return null;
    }


    /**
     * 生成原始数据
     *
     * @param input
     * @return
     */
    private String genData(String input) {

        RootData rootData = new RootData();
        rootData.setUserName(input);
        rootData.setUuid(getDeviceInfoUtil.getUUID());
        rootData.setApplicationId(getDeviceInfoUtil.getApplicationId());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", rootData.getUserName());
            jsonObject.put("uuid", rootData.getUuid());
            jsonObject.put("applicationId", rootData.getApplicationId());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject.toString();
    }

    public boolean checkData(String licenseKey) {


        return true;
    }

    /**
     * RSA加密数据
     *
     * @param data
     * @return
     */
    public byte[] encryptRSAPublicKey(byte[] data) {
        byte[] dataBytes = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, mKeyMap.get(PUBLIC_KEY));
            dataBytes = cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return dataBytes;
    }

    /**
     * RSA加密数据
     *
     * @param data
     * @return
     */
    public byte[] encryptRSAPublicKey(String data) {
        byte[] dataBytes = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, mKeyMap.get(PUBLIC_KEY));
            dataBytes = cipher.doFinal(data.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return dataBytes;
    }

    /**
     * RSA解密数据
     *
     * @param data
     */
    public byte[] decryptRSAPrivateKey(String data) {
        byte[] dataBytes = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, mKeyMap.get(PRIVATE_KEY));
            dataBytes = cipher.doFinal(data.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return dataBytes;
    }

    /**
     * RSA解密数据
     *
     * @param data
     */
    public byte[] decryptRSAPrivateKey(byte[] data) {
        byte[] dataBytes = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, mKeyMap.get(PRIVATE_KEY));
            dataBytes = cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return dataBytes;
    }

    /**
     * 初始化生成公钥和密钥
     *
     * @return
     */
    private Map<String, Key> initPrivateAndPublicKey() {

        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA", "SC");
            keyPairGenerator.initialize(2048);
            //生成一个密钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            mKeyMap.put(PUBLIC_KEY, publicKey);
            mKeyMap.put(PRIVATE_KEY, privateKey);
//        Log.d(TAG, "生成密钥对成功");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }


        return mKeyMap;
    }

    /**
     * 获取生成的密钥
     *
     * @return
     */
    public Key getPrivateKey() {
        return mKeyMap.get(PRIVATE_KEY);
    }

    /**
     * 获取生成的公钥
     *
     * @return
     */
    public Key getPublicKey() {
        return mKeyMap.get(PUBLIC_KEY);
    }




    /**
     * 使用SHA1进行消息摘要
     *
     * @param data
     * @return
     */
    private byte[] encodeSHA1(String data) {
        byte[] dataByte = null;
        byte[] newDataByte = null;
        try {
            //使用SHA进行消息摘要
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            dataByte = messageDigest.digest(data.getBytes());
            //摘要后截取后10位返回
            newDataByte = new byte[10];
            System.arraycopy(dataByte , 9 , newDataByte ,0, 10);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return newDataByte;
    }

    private String hexData(byte[] data) {
//        String hexResult = new String(Hex.encode(data));
//        return hexResult;
        return new String(Hex.encode(data)).toUpperCase();
    }

    /**
     * 私钥签名
     * @param data
     * @return
     */
    private byte[] signWithPrivateKey(String data) {

        byte[] result = null;
        try {

            PKCS8EncodedKeySpec pkcs8EncodeKeySpec = new PKCS8EncodedKeySpec(mKeyMap.get(PRIVATE_KEY).getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodeKeySpec);
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            //私钥签名后的数据
            result = signature.sign();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 公钥验签
     */
    private boolean checkWithPublicKey(byte[] data) {

        boolean isRight = false;
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(mKeyMap.get(PUBLIC_KEY).getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);
            isRight = signature.verify(data);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        return isRight;
    }


}
