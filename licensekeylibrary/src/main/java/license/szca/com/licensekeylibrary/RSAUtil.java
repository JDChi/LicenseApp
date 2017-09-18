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
     *
     * @param data
     * @return
     */
    public byte[] encryptWithPrivateKey(byte[] data , byte[] privateKeyByte) {
        byte[] dataBytes = null;
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyByte);



        try {
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
     *
     * @param data
     */
    public byte[] decryptWithPublicKey(byte[] data , byte[] key) {
        byte[] dataBytes = null;
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);


        try {
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


//    /**
//     * 私钥签名
//     *
//     * @param data
//     * @return
//     */
//    private byte[] signWithPrivateKey(String data) {
//
//        byte[] result = null;
//        try {
//
//            PKCS8EncodedKeySpec pkcs8EncodeKeySpec = new PKCS8EncodedKeySpec(mKeyMap.get(PRIVATE_KEY).getEncoded());
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodeKeySpec);
//            Signature signature = Signature.getInstance("SHA1withRSA");
//            signature.initSign(privateKey);
//            signature.update(data.getBytes());
//            //私钥签名后的数据
//            result = signature.sign();
//
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (SignatureException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//    /**
//     * 公钥验签
//     */
//    private boolean checkWithPublicKey(byte[] data) {
//
//        boolean isRight = false;
//        try {
//            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(mKeyMap.get(PUBLIC_KEY).getEncoded());
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
//            Signature signature = Signature.getInstance("SHA1withRSA");
//            signature.initVerify(publicKey);
//            isRight = signature.verify(data);
//
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeySpecException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (SignatureException e) {
//            e.printStackTrace();
//        }
//
//        return isRight;
//    }

}
