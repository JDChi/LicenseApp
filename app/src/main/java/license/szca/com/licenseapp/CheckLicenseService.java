package license.szca.com.licenseapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * description : 一个用于检验证书的服务，在新的进程里
 * author : JDNew
 * on : 2017/9/17.
 */

public class CheckLicenseService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
