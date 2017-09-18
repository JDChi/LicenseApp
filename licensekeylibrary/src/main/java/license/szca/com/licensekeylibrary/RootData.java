package license.szca.com.licensekeylibrary;

/**
 * description 要提交的数据实体类
 * Created by JD
 * on 2017/9/15.
 */

public class RootData {

    private String userName;
    private String uuid;
    private String applicationId;
    private String appSignature;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getAppSignature() {
        return appSignature;
    }

    public void setAppSignature(String appSignature) {
        this.appSignature = appSignature;
    }
}
