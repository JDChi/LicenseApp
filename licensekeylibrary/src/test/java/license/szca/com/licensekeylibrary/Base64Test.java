package license.szca.com.licensekeylibrary;

import org.junit.Test;
import org.spongycastle.util.encoders.Base64;

import static org.junit.Assert.assertEquals;

/**
 * description
 * Created by JD
 * on 2017/9/14.
 */

public class Base64Test {

    @Test
    public void base64Test() {
        assertEquals("abc", decodeData(encodeData("abc")));
    }

    /**
     * 编码
     */
    private String encodeData(String data) {
        byte[] encodeByte = Base64.encode(data.getBytes());
        return new String(encodeByte);
    }

    /**
     * 解码
     */
    private String decodeData(String data) {
        byte[] decodeByte = Base64.decode(data.getBytes());
        return new String(decodeByte);
    }
}
