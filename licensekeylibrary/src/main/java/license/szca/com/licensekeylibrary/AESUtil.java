package license.szca.com.licensekeylibrary;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * description : aes对称加密工具类
 * author : JDNew
 * on : 2017/9/17.
 */

public class AESUtil {

    private final String KEY_ALGORITEM = "AES";
    private final String CIPHER_ALGORITEM = "AES/ECB/PKCS7Padding";
    private SecretKey mSecretKey;

    static {
        //从位置1开始，添加新的提供者
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    public AESUtil() {
        initKey();
    }

    /**
     * 生成加密的密钥
     */
    private void initKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITEM, "SC");
            keyGenerator.init(128);
            mSecretKey = keyGenerator.generateKey();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    /**
     * aes加密数据
     * @param data 要加密的数据
     * @param keyByte 二进制密钥
     * @return
     */
    public byte[] encryptData(byte[] data, byte[] keyByte) {
        byte[] enryptResult = null;
        Key key = genKey(keyByte);

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITEM, "SC");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            enryptResult = cipher.doFinal(data);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return enryptResult;
    }

    /**
     * aes解密数据
     * @param data 要解密的数据
     * @param keyByte 二进制密钥
     * @return
     */
    public byte[] decryptData(byte[] data, byte[] keyByte) {

        Key key = genKey(keyByte);
        byte[] decryptResult = null;
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITEM, "SC");
            cipher.init(Cipher.DECRYPT_MODE, key);
            decryptResult = cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        return decryptResult;
    }

    /**
     * 获取aes加密的密钥
     *
     * @return 二进制密钥
     */
    public byte[] getAESSecretKey() {
        return mSecretKey.getEncoded();
    }

    /**
     * 转换密钥 通过二进制转换
     *
     * @param keyByte 二进制密钥
     * @return 密钥
     */
    private Key genKey(byte[] keyByte) {
        SecretKey secretKey = new SecretKeySpec(keyByte, KEY_ALGORITEM);
        return secretKey;
    }

}
