package license.szca.com.licensekeylibrary;

import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import static org.junit.Assert.assertEquals;

/**
 * description
 * Created by JD
 * on 2017/9/14.
 */

public class HexTest {

    @Test
    public void hexTest(){
        assertEquals(hexData("abc736519d54f7e42fda6b066b74dff4657") , hexData("abc736519d54f7e42fda6b066b74dff4657"));
    }

    private String hexData(String data){
        String hexResult = new String(Hex.encode(data.getBytes()));
        return hexResult;
    }
}
