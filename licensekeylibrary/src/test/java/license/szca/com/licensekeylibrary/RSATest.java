package license.szca.com.licensekeylibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * description : RSA测试类
 * author : JDNew
 * on : 2017/9/17.
 */

public class RSATest {


    private RSAUtil rsaUtil;
    private String originData = "abc";

    @Before
    public void init() {
        rsaUtil = new RSAUtil();
        //初始化生成公钥和密钥
        rsaUtil.initPrivateAndPublicKey();
    }

    @Test
    public void rsaTest() {
        //私钥加密
        byte[] encryptData = rsaUtil.encryptWithPrivateKey(originData.getBytes(), rsaUtil.getPrivateKey());
        //公钥解密
        byte[] decryptData = rsaUtil.decryptWithPublicKey(encryptData, rsaUtil.getPublicKey());

        assertEquals(originData, new String(decryptData));
    }
}
