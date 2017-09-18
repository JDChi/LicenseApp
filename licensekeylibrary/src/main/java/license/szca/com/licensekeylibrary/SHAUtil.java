package license.szca.com.licensekeylibrary;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * description : SHA消息摘要工具类
 * author : JDNew
 * on : 2017/9/17.
 */

public class SHAUtil {
    static {
        //从位置1开始，添加新的提供者
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }
    /**
     * 使用SHA1进行消息摘要
     *
     * @param data
     * @return
     */
    public byte[] encodeSHA1(byte[] data) {
        byte[] dataByte = null;
        byte[] newDataByte = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            dataByte = messageDigest.digest(data);
            //摘要后截取后10位返回
            newDataByte = new byte[10];
            System.arraycopy(dataByte, 9, newDataByte, 0, 10);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return newDataByte;
    }

}
