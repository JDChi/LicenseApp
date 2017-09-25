# 项目结构
## app
主要是用于用户输入信息

## licensekeylibrary
是一个库，里面放着加密，解密的封装，以及其它一些数据的生成

## serverapp
主要是对客户端提交的用户输入信息进行证书的生成和校验

<b>注：在运行的时候必需保证客户端和服务端两个app都已经安装在手机上，我这里没做判断，在之后会做多一层判断。</b>

<b>app和serverapp之间的通信是用AIDL的方式进行，在一开始我是用Messenger来做的（Messenger的底层也是AIDL，这里一开始为了方便就用Messenger），所以你可以看到有RegsitActivity和RegistActivity1，前者是用Messenger，后者就是用AIDL，所以可以做些适当的修改进行切换。</b>

<b>另我也对此写了相关博客</b>

[Android之使用AIDL进行IPC（一）](http://blog.csdn.net/u013066292/article/details/78083755)

# 序列图
```sequence
app->serverapp: 提交生成证书的数据
Note right of serverapp: 对数据进行处理
serverapp-->app: 返回证书
app->serverapp: 提交校验数据
Note right of serverapp: 对数据进行校验
serverapp-->app: 返回校验结果
```

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




![](https://github.com/JDNew/LicenseApp/blob/master/app/src/main/res/mipmap-xhdpi/screenshot_20170920_181656.png)
![](https://github.com/JDNew/LicenseApp/blob/master/app/src/main/res/mipmap-xhdpi/screenshot_20170920_181704.png)
![](https://github.com/JDNew/LicenseApp/blob/master/app/src/main/res/mipmap-xhdpi/screenshot_20170920_181748.png)
