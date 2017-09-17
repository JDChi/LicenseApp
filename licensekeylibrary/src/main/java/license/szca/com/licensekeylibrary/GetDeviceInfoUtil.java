package license.szca.com.licensekeylibrary;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import org.spongycastle.util.encoders.Hex;

import java.io.ByteArrayInputStream;

import java.security.cert.CertificateFactory;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.cert.X509Certificate;

/**
 * description 获取设备相关信息的工具类
 * Created by JD
 * on 2017/9/13.
 */

public class GetDeviceInfoUtil {


    private Context mContext;
    public GetDeviceInfoUtil(Context context){
        mContext = context;
    }
    /**
     * 获取唯一标识码（每次安装app都是不一样的值）
     * 如：736519d5-4f7e-42fd-a6b0-66b74dff4657
     *
     * @return 返回过滤掉“-”的标识码
     * 736519d54f7e42fda6b066b74dff4657
     */
    public String getUUID() {
        String regularExpression = "-";
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(UUID.randomUUID().toString());
        return matcher.replaceAll("").trim();
    }

    /**
     * 获取应用包名
     * @return
     */
    public String getAppPackageName() {
        return mContext.getPackageName();
    }

    /**
     * 获取应用签名
     * @return
     */
    public String getSign() {
        PackageManager pm = mContext.getPackageManager();
        List<PackageInfo> apps = pm
                .getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();

        while (iter.hasNext()) {
            PackageInfo info = iter.next();
            String packageName = info.packageName;
            //按包名 取签名
            if (packageName.equals(getAppPackageName())) {
                byte[] signByte = info.signatures[0].toByteArray();

                return new String( Hex.encode(signByte));

            }
        }
        return null;
    }


}
