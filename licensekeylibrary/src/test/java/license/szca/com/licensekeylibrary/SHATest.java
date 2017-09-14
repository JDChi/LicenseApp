package license.szca.com.licensekeylibrary;

import org.junit.Before;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * description
 * Created by JD
 * on 2017/9/14.
 */

public class SHATest {

    String testData = null;

    @Before
    public void init(){
        testData = "abc736519d54f7e42fda6b066b74dff4657";
    }


    static {
        //从位置1开始，添加新的提供者
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    @Test
    public void shaTest(){
        assertArrayEquals(encodeSHA(testData) , encodeSHA(testData));
    }

    @Test
    public void shaHexTest(){
        assertEquals(hexData("hello") , hexData("hello"));
    }

    /**
     * 使用SHA1进行消息摘要
     * @param data
     * @return
     */
    private byte[] encodeSHA(String data){
        byte[] dataByte = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            dataByte = messageDigest.digest(data.getBytes());


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return dataByte;
    }

    private String hexData(String data){
        String hexResult = new String(Hex.encode(encodeSHA(data)));
        return hexResult;
    }

    private String hexData(byte[] data){
        String hexResult = new String(Hex.encode(data));
        return hexResult;
    }



}
