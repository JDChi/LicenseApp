package license.szca.com.licensekeylibrary;

/**
 * description 提交给客户端的数据实体类
 * Created by JD
 * on 2017/9/18.
 */

public class RootSubmitData {
    /**
     * RSA公钥，用于解密出AES密钥
     */
    private String rsaPublicKey;
    /**
     * AES密钥，用于解密出客户端数据
     */
    private String encrptAESKey;
    /**
     * 被AES加密过的数据
     */
    private String encryptAESClientData;

    public String getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(String rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

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


}
