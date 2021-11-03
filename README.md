# SimpleWalker
知乎 Android 团队使用的 Java 库静态代码检查工具，支持目录、.jar、.aar、.apk、.dex 格式，可通过配置文件添加检查策略。
主要用于检查 Android 应用的三方依赖是否有调用隐私接口。
# 使用方法
项目工程中 release/ 目录下是最新版本的可执行文件，help 输出如下：
``` bash
zkw@zkw-mint: java -jar ./SimpleWalker-1.x.jar
Usage: SimpleWalker(一个简单的静态代码扫描工具，可通过文件配置，支持目录、jar、aar 格式) [options]
  Options:
    -h, --help

  * -i, --input
      被扫描的文件，支持 jar、aar 和存放这些文件的目录
    -o, --output
      Json 报告输出文件名，默认为当前目录的 result.json
      Default: ./result.json
  * -p, --policy
      规则配置文件，根据规则进行扫描
```
根据参数制定输入文件和策略配置文件：
``` bash
zkw@zkw-mint: java -jar ./release/SimpleWalker-1.x.jar -i ~/jars/ -p ./release/policy                                                                               SIGINT(2) ↵  8711  16:55:48
报告文件：/home/zkw/my_project/SimpleWalker/result.json
```
其中默认配置文件 policy 的内容如下：
```
#每行代表一个策略，以 ',' 分隔，含义分别是：指令类型,类名,方法名/属性名,描述
#以第一行策略为例，其含义是当源码中有通过指令 invokevirtual 调用 TelephonyManager 的任意方法时触发策略
invokevirtual,android.telephony.TelephonyManager,all,获取手机 ID 信息
invokespecial,dalvik.system.DexClassLoader,<init>,可能是热更新
invokevirtual,java.lang.Runtime,exec,执行外部命令
invokevirtual,android.accounts.AccountManager,getAccounts,获取帐号列表
invokevirtual,android.content.pm.PackageManager,getInstalled,获取已安装应用列表
invokevirtual,android.content.pm.PackageManager,queryIntentActivit,获取已安装取应用列表
invokevirtual,android.bluetooth.BluetoothAdapter,all,获取蓝牙信息
getstatic,android.provider.ContactsContract$Contacts,CONTENT_URI,获取联系人信息
invokevirtual,android.net.wifi.WifiInfo,getMacAddress,获取 MAC 地址
invokevirtual,java.net.NetworkInterface,getNetworkInterfaces,获取 MAC 地址
invokevirtual,android.nfc.NfcAdapter,all,获取 NFC 信息
invokevirtual,android.app.ActivityManager,getRunningAppProcesses,获取正在运行的应用列表
invokevirtual,android.hardware.SensorManager,all,获取传感器信息
invokevirtual,android.net.wifi.WifiManager,getConnectionInfo,获取 WIFI 信息
```
每行代表一个策略，以 ',' 分隔，含义分别是：指令类型,类名,方法名/属性名,描述。
以第一行策略为例，其含义是当源码中有通过指令 invokevirtual 调用 TelephonyManager 的任意方法时触发策略。

成功的话会输出报告文件 result.json，报告文件的内容格式如下：
``` json
{
  "android/net/wifi/WifiManager 获取 WIFI 信息": [
    "文件 location.jar 中的 com/dw.c() 方法调用了 WifiManager.getConnectionInfo()，获取 WIFI 信息",
    "文件 adxxx.aar 中的 com/cpu/d.f() 方法调用了 WifiManager.getConnectionInfo()，获取 WIFI 信息",
    "文件 facccc.aar 中的 com/apo/l.a() 方法调用了 WifiManager.getConnectionInfo()，获取 WIFI 信息"
  ],
  "android/provider/ContactsContract$Contacts 获取联系人信息": [],
  "dalvik/system/DexClassLoader 可能是热更新": [
    "文件 adxxx.aar 中的 com/xda.permissionClick() 方法调用了 DexClassLoader.<init>()，可能是热更新"
  ],
  "java/net/NetworkInterface 获取 MAC 地址": [],
  "android/nfc/NfcAdapter 获取 NFC 信息": [],
  "android/app/ActivityManager 获取正在运行的应用列表": [
    "文件 pushxxx.aar 中的 com/sdk/PushManager.e() 方法调用了 ActivityManager.getRunningAppProcesses()，获取正在运行的应用列表",
    "文件 bccxxx.aar 中的 com/smartlink/util/a.b() 方法调用了 ActivityManager.getRunningAppProcesses()，获取正在运行的应用列表"
  ],
  "android/bluetooth/BluetoothAdapter 获取蓝牙信息": [],
  "android/hardware/SensorManager 获取传感器信息": [
    "文件 location.jar 中的 com/ds.<init>() 方法调用了 SensorManager.getDefaultSensor()，获取传感器信息"
  ],
  "android/telephony/TelephonyManager 获取手机 ID 信息": [
    "文件 osdk.aar 中的 com/common/utils/Utils.getOperatorName() 方法调用了 TelephonyManager.getSimOperator()，获取手机 ID 信息",
    "文件 bccxxx.aar 中的 com/smartlink/util/a.a() 方法调用了 TelephonyManager.getDeviceId()，获取手机 ID 信息"
  ],
  "android/content/pm/PackageManager 获取已安装应用列表": [],
  "java/lang/Runtime 执行外部命令": [
    "文件 pushxxx.aar 中的 com/push/core/e.a() 方法调用了 Runtime.exec()，执行外部命令"
  ],
  "android/accounts/AccountManager 获取帐号列表": [],
  "android/content/pm/PackageManager 获取已安装取应用列表": [],
  "android/net/wifi/WifiInfo 获取 MAC 地址": [
    "文件 location.jar 中的 com/n.m() 方法调用了 WifiInfo.getMacAddress()，获取 MAC 地址",
    "文件 xunnnnn.aar 中的 com/voiceads/utils/k.b() 方法调用了 WifiInfo.getMacAddress()，获取 MAC 地址"
  ]
}
```
从报告中可以看到 jar 或 aar 包中的 class 中对敏感函数的调用关系，使用者可以根据此报告对第三方 SDK 做简单判断。
# 编译方法
``` bash
./gradlew jar
```
jar 文件的输出目录是
``` bash
./build/libs/SimpleWalker-x.x.jar
```
# 参考
[asm](https://gitlab.ow2.org/asm/asm)  
[dex2jar](https://github.com/pxb1988/dex2jar)