package license.szca.com.licensekeylibrary;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
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
 * description : RSA工具类
 * author : JDNew
 * on : 2017/9/17.
 */

public class RSAUtil {

    static {
        //从位置1开始，添加新的提供者
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    private Map<String, Key> mKeyMap = new HashMap<>();
    private final String PUBLIC_KEY = "publicKey";
    private final String PRIVATE_KEY = "privateKey";
    private final String KEY_ALGORITHM = "RSA";


    /**
     * RSA私钥加密数据
     * @param data 要加密的数据
     * @param privateKeyByte 二进制的私钥
     * @return 私钥加密后的数据
     */
    public byte[] encryptWithPrivateKey(byte[] data, byte[] privateKeyByte) {
        byte[] dataBytes = null;
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyByte);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
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
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return dataBytes;
    }

    /**
     * RSA解密数据
     * @param data 要解密的数据
     * @param publicKeyByte 二进制公钥
     * @return 解密后的数据
     */
    public byte[] decryptWithPublicKey(byte[] data, byte[] publicKeyByte) {
        byte[] dataBytes = null;
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyByte);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm(), new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
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
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return dataBytes;
    }

    /**
     * 初始化生成公钥和密钥
     *
     * @return
     */
    public Map<String, Key> initPrivateAndPublicKey() {

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "SC");
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
    public byte[] getPrivateKey() {
        return mKeyMap.get(PRIVATE_KEY).getEncoded();
    }

    /**
     * 获取生成的公钥
     *
     * @return
     */
    public byte[] getPublicKey() {
        return mKeyMap.get(PUBLIC_KEY).getEncoded();
    }

}
