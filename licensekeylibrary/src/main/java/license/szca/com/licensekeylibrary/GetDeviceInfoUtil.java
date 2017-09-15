package license.szca.com.licensekeylibrary;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description 获取设备相关信息的工具类
 * Created by JD
 * on 2017/9/13.
 */

public class GetDeviceInfoUtil {



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

    public String getApplicationId() {
        return BuildConfig.APPLICATION_ID;
    }


    public static String getApkSignP(String apkFilePath){
        byte[] readBuffer = new byte[8192];
        java.security.cert.Certificate[] certs = null;
        try{
            JarFile jarFile = new JarFile(apkFilePath);
            Enumeration entries = jarFile.entries();
            while(entries.hasMoreElements()){
                JarEntry je = (JarEntry)entries.nextElement();
                if(je.isDirectory()){
                    continue;
                }
                if(je.getName().startsWith("META-INF/")){
                    continue;
                }
                java.security.cert.Certificate[] localCerts = loadCertificates(jarFile,je,readBuffer);
                //  System.out.println("File " + apkFilePath + " entry " + je.getName()+ ": certs=" + certs + " ("+ (certs != null ? certs.length : 0) + ")");
                if (certs == null) {
                    certs = localCerts;
                }else{
                    for(int i=0; i<certs.length; i++){
                        boolean found = false;
                        for (int j = 0; j < localCerts.length; j++) {
                            if (certs[i] != null && certs[i].equals(localCerts[j])) {
                                found = true;
                                break;
                            }
                        }
                        if (!found || certs.length != localCerts.length) {
                            jarFile.close();
                            return null;
                        }
                    }
                }
            }
            jarFile.close();
            //Log.i("wind cert=",certs[0].toString());
            return certs[0].getPublicKey().toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static java.security.cert.Certificate[] loadCertificates(JarFile jarFile, JarEntry je, byte[] readBuffer) {
        try {
            InputStream is = jarFile.getInputStream(je);
            while(is.read(readBuffer,0,readBuffer.length)!=-1) {
            }
            is.close();
            return (java.security.cert.Certificate[])(je!=null?je.getCertificates():null);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception reading "+je.getName()+" in "+jarFile.getName()+": "+e);
        }
        return null;
    }
}
