#客户端流程图

```flow
start=>start: 开始
end=>end: 结束
opGenRSAKey=>operation: 生成RSA公钥和密钥
opGetDeviceInfo=>operation: 获取设备信息（uuid，包名）
opInputUserName=>operation: 用户输入“abc”
opGenLicenseData=>operation: 将用户输入和设备信息拼接成json（data）
opClickLicenseBtn=>operation: 点击“获取证书”按钮
opSHA1WithData=>operation: 使用SHA1对data做摘要
opHexWithData=>operation: 对摘要后的data做Hex
opGenLicense=>operation: 得到要填写的证书（20位，字母和数字构成）
opCombimeDataWithLicense=>operation: 将证书拼接到data里
opAESWithData=>operation: 用AES对data加密
opRSAWithAESKey=>operation: 用RSA对AES的密钥加密
opGenSubmitData=>operation: 将加密后的data，加密后的AES密钥，RSA公钥拼接成json（submitData）
opSendToServer=>operation: 将submitData提交给服务端

start->opGenRSAKey->opGetDeviceInfo->opInputUserName->opGenLicenseData->opSHA1WithData->opHexWithData->opGenLicense->opCombimeDataWithLicense->opAESWithData->opRSAWithAESKey->opGenSubmitData->opSendToServer->end
```
