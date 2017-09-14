package license.szca.com.licensekeylibrary;

import java.util.UUID;

/**
 * description
 * Created by JD
 * on 2017/9/13.
 */

public class GetDeviceIdUtil {

    public String getGUID(){
        return UUID.randomUUID().toString();
    }
}
