package license.szca.com.licensekeylibrary;

import org.spongycastle.util.encoders.Hex;

/**
 * description : 编码工具类
 * author : JDNew
 * on : 2017/9/17.
 */

public class CodeUtil {

    /**
     * 十六进制编码
     * @param data
     * @return
     */
    public String hexData(byte[] data) {
//        String hexResult = new String(Hex.encode(data));
//        return hexResult;
        return new String(Hex.encode(data)).toUpperCase();
    }
}
