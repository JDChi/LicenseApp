# 客户端流程图

```flow

start=>start: 开始
end=>end: 结束

opGetDeviceInfo=>operation: 获取设备信息（uuid，包名）
opInputUserName=>operation: 用户输入“abc”
opGenLicenseData=>operation: 将用户输入和设备信息拼接起来
opSendLicenseData=>operation: 将拼接数据发给服务端
opGetLicense=>operation: 得到客户端返回的证书和RSA公钥
opCombimeDataWithLicense=>operation: 将license和用户输入，设备信息生成json（data）
opAESWithData=>operation: 用AES对data加密
opRSAWithAESKey=>operation: 用RSA公钥对AES的密钥加密
opRSASignData=>operation: 用RSA对数据进行签名
opGenSubmitData=>operation: 将加密后的data，加密后的AES密钥，RSA签名公钥和签名拼接成json（submitData）
opSendToServer=>operation: 将submitData提交给服务端

start->opGetDeviceInfo->opInputUserName->opGenLicenseData->opSendLicenseData->opGetLicense->opCombimeDataWithLicense->opAESWithData->opRSAWithAESKey->opGenSubmitData->opSendToServer->end

```
# 服务端流程图

```flow

start=>start: 开始
end=>end: 结束
opGenRSAKey=>operation: 生成RSA公钥和密钥
opReceiveData=>operation: 接收到来自客户端的数据
opSHA1WithData=>operation: 使用SHA1对拼接后的数据做摘要
opHexWithData=>operation: 对摘要后的数据做Hex
opGenLicense=>operation: 得到要填写的证书（20位，字母和数字构成）
opReplyLicense=>operation: 把证书和RSA公钥发给客户端
opReceiveSubmitData=>operation: 接收客户端发来的submitData
opRSADecryptAESKey=>operation: 用RSA公钥解密出AES密钥
opAESDecryptData=>operation: 用AES密钥解密出data
opCompareLicense=>operation: 将Hex后的数据与data里的license做对比
conLicense=>condition: 对比一致？
conSign=>condition: 签名校验成功?
opReplySuccess=>operation: 注册成功
opReplyFailed=>operation: 注册失败

start->opGenRSAKey->opReceiveData->opSHA1WithData->opHexWithData->opGenLicense->opReplyLicense->opReceiveSubmitData->opRSADecryptAESKey->opAESDecryptData->opCompareLicense->conLicense
conLicense(yes)->conSign
conLicense(no)->opReplyFailed->end
conSign(yes)->opReplySuccess->end
conSign(no)->opReplyFailed->end

```

![](https://github.com/JDNew/LicenseApp/blob/master/app/src/main/res/mipmap-xhdpi/screenshot_20170920-181656.png)
![](https://github.com/JDNew/LicenseApp/blob/master/app/src/main/res/mipmap-xhdpi/screenshot_20170920_181704.png)
![](https://github.com/JDNew/LicenseApp/blob/master/app/src/main/res/mipmap-xhdpi/Screenshot_20170920_181748.png)
