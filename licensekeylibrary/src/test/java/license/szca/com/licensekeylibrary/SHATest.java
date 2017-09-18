package license.szca.com.licensekeylibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * description
 * Created by JD
 * on 2017/9/18.
 */

public class SHATest {

    SHAUtil shaUtil;
    CodeUtil codeUtil;
    String originData = "abc";
    @Before
    public void init(){
        shaUtil = new SHAUtil();
        codeUtil = new CodeUtil();
    }

    @Test
    public void shaTest(){
       assertEquals(codeUtil.hexData(shaUtil.encodeSHA1(originData.getBytes())) , codeUtil.hexData(shaUtil.encodeSHA1(originData.getBytes())));
    }
}
