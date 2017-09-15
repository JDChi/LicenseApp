package license.szca.com.licensekeylibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * description 生成的20位license的测试类
 * Created by JD
 * on 2017/9/14.
 */

public class SHATest {

  private String testData = "hello";
    private GenLicenseKeyUtil genLicenseKeyUtil;

    @Before
    public void init(){
        genLicenseKeyUtil = new GenLicenseKeyUtil();
    }

    @Test
    public void shaTest(){
        assertEquals(genLicenseKeyUtil.genLicense(testData) , genLicenseKeyUtil.genLicense(testData));
    }


}
