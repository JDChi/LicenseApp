package license.szca.com.licensekeylibrary;

/**
 * description : 客户端数据的实体类
 * author : JDNew
 * on : 2017/9/17.
 */

public class RootData {
    private String userName;
    private String uuid;
    private String applicationId;
    private String licenseKey;
    private String appSignture;




    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getAppSignture() {
        return appSignture;
    }

    public void setAppSignture(String appSignture) {
        this.appSignture = appSignture;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }


}
