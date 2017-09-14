package license.szca.com.licensekeylibrary;

import org.spongycastle.jcajce.provider.asymmetric.rsa.KeyPairGeneratorSpi;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.util.encoders.Hex;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public GenLicenseKeyUtil() {
        //1.随机初始化密钥和公钥
        initPrivateAndPublicKey();
    }

    /**
     *
     * @param input 用户输入的内容
     * @return lincense
     */
    public String genLicense(String input) {

        String license = encryptData(input);
        return license;
    }

    /**
     * 对用户输入的内容进行一系列的算法运算，返回license
     * @param input
     * @return
     */
    private String encryptData(String input){
        //2.拼接数据（用户输入+设备唯一信息）
        String data = input + getUUID();
        //3.对拼接后的数据使用SHA1做信息摘要
        byte[] sha1Data = encodeSHA1(data);
        //4.对摘要后的数据使用RSA公钥加密
        byte[] encryptData = encryptRSAPublicKey(sha1Data);
        //5.对加密后的数据进行Hex编码
        String hexData = hexData(encryptData);

        return hexData;
    }

    public boolean checkData(String licenseKey){




        return true;
    }

    /**
     * 加密数据
     * @param data
     * @return
     */
    private byte[] encryptRSAPublicKey(byte[] data) {
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
     * 解密数据
     *
     * @param data
     */
    private void decryptRSAPrivateKey(String data) {
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
    }

    /**
     * 初始化生成公钥和密钥
     *
     * @return
     */
    private Map<String, Key> initPrivateAndPublicKey() {
        KeyPairGeneratorSpi keyPairGenerator = new KeyPairGeneratorSpi();
        keyPairGenerator.initialize(2048);
        //生成一个密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        Log.d(TAG, "生成密钥对成功");
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        mKeyMap.put(PUBLIC_KEY, publicKey);
        mKeyMap.put(PRIVATE_KEY, privateKey);

        return mKeyMap;
    }

    /**
     * 获取唯一标识码（每次安装app都是不一样的值）
     * 如：736519d5-4f7e-42fd-a6b0-66b74dff4657
     *
     * @return 返回过滤掉“-”的标识码
     * 736519d54f7e42fda6b066b74dff4657
     */
    private String getUUID() {
        String regularExpression = "-";
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(UUID.randomUUID().toString());
        return matcher.replaceAll("").trim();
    }


    /**
     * 使用SHA1进行消息摘要
     *
     * @param data
     * @return
     */
    private byte[] encodeSHA1(String data) {
        byte[] dataByte = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            dataByte = messageDigest.digest(data.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return dataByte;
    }

    private String hexData(byte[] data){
//        String hexResult = new String(Hex.encode(data));
//        return hexResult;
        return Hex.toHexString(data);
    }


}
