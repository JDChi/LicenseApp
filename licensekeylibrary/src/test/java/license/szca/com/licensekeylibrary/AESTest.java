package license.szca.com.licensekeylibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * description : AES测试类
 * author : JDNew
 * on : 2017/9/17.
 */

public class AESTest {

    AESUtil aesUtil;
    //未加密数据
    String originData = "abc";
    @Before
    public void init(){
        aesUtil = new AESUtil();
    }

    @Test
    public void aesTest(){

        //aes加密
        byte[] encryptData = aesUtil.encryptData(originData, aesUtil.getAESSecretKey());
        //aes解密
        byte[] decryptData = aesUtil.decryptData(encryptData , aesUtil.getAESSecretKey());

        assertEquals(originData, new String(decryptData));
    }
}
