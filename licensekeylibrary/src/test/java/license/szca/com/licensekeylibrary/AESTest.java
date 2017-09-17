package license.szca.com.licensekeylibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * description :
 * author : JDNew
 * on : 2017/9/17.
 */

public class AESTest {

    AESUtil aesUtil;
    @Before
    public void init(){
        aesUtil = new AESUtil();
    }

    @Test
    public void aesTest(){
        assertEquals("abc" , aesUtil.decryptData(aesUtil.encryptData("abc")));
    }
}
