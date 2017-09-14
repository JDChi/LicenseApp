package license.szca.com.licensekeylibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * description
 * Created by JD
 * on 2017/9/14.
 */

public class GenLicenseKeyTest {

    private GenLicenseKeyUtil genLicenseKeyUtil;

    @Before
    public void init(){
        genLicenseKeyUtil = new GenLicenseKeyUtil();
    }

    @Test
    public void testData(){
        assertEquals(genLicenseKeyUtil.genLicense("hello") , "123");
    }
}
