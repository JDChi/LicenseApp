package license.szca.com.licensekeylibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * description RSA加解密测试类
 * Created by JD
 * on 2017/9/15.
 */

public class RSATest {

    private GenLicenseKeyUtil genLicenseKeyUtil;
    private String originData = "abc";

    @Before
    public void init(){
        genLicenseKeyUtil = new GenLicenseKeyUtil();
    }

    @Test
    public void checkRSA(){
        //公钥加密
        byte[] encryptDataByte = genLicenseKeyUtil.encryptRSAPublicKey(originData);
        //私钥解密
        byte[] decryptDataByte = genLicenseKeyUtil.decryptRSAPrivateKey(encryptDataByte);
        //转换成String
        String decryptResult = new String(decryptDataByte);

        assertEquals(originData , decryptResult);
    }
}
