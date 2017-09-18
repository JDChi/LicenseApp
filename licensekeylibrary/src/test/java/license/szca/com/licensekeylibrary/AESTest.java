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
    String originData = "b8555645050517e860e0d09e8468a225159834bc9014e48b0d1572d7d18684241af9b8dc8c3d85416ce29e895f541c40554f33dd1a11ae0f4b62dbd6676f82c20758f28c4b750a05824284e76f770fb2d225fdbd48bcb6592095f657784bffcfd80e97ff101cf6f8f0e37815ee50cf46d7ad8481fcab5b8e0e294a16cbae33c4f5ddb80fb88f55273f2b14959f7bc56edbda56bf70c9ca8c88bf678929f1fed9039c8593455d41ee6ef9e25b1c99df3ee4f99be613ac077ca8197200f1833a42dfbe586c7307d2b1367089b8c5dce7312f5eae6418a5c8944fca99727ad87ca5fe823e11807e4616d464979263065f875a6cf669625f58dc3b320ac4383def899dfd3f5705af6413587acd3684cf5010f10b06d6d64131b8fa76ce2fcf5a01118010b26b6c90de8065bb31991a46d12c6baa6db9c729d1b74d2997a316b0daf6";
    @Before
    public void init(){
        aesUtil = new AESUtil();
    }

    @Test
    public void aesTest(){

        //aes加密
        byte[] encryptData = aesUtil.encryptData(originData.getBytes(), aesUtil.getAESSecretKey());
        //aes解密
        byte[] decryptData = aesUtil.decryptData(encryptData , aesUtil.getAESSecretKey());

        assertEquals(originData, new String(decryptData));
    }
}
