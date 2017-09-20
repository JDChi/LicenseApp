package license.szca.com.licensekeylibrary;

/**
 * description 提交给客户端的数据实体类
 * Created by JD
 * on 2017/9/18.
 */

public class RootSubmitData {

    /**
     * AES密钥，用于解密出客户端数据
     */
    private String encrptAESKey;
    /**
     * 被AES加密过的数据
     */
    private String encryptAESClientData;
    /**
     * 签名后的数据
     */
    private String signData;
    /**
     * RSA签名公钥
     */
    private String rsaSignPublicKey;



    public String getEncrptAESKey() {
        return encrptAESKey;
    }

    public void setEncrptAESKey(String encrptAESKey) {
        this.encrptAESKey = encrptAESKey;
    }

    public String getEncryptAESClientData() {
        return encryptAESClientData;
    }

    public void setEncryptAESClientData(String encryptAESClientData) {
        this.encryptAESClientData = encryptAESClientData;
    }

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }

    public String getRsaSignPublicKey() {
        return rsaSignPublicKey;
    }

    public void setRsaSignPublicKey(String rsaSignPublicKey) {
        this.rsaSignPublicKey = rsaSignPublicKey;
    }


}
