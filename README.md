# 网络通信库集群


![](https://img.shields.io/badge/LICENSE-MIT-green.svg)  ![](https://img.shields.io/badge/CODE-Kotlin-green.svg)  ![](https://img.shields.io/badge/BUILD-Gradle-green.svg)  ![](https://img.shields.io/badge/TYPE-LIBRARY-green.svg)  [![](https://jitpack.io/v/ExplodingFKL/JNetServer.svg)](https://jitpack.io/#ExplodingFKL/JNetServer)


一个轻量级网络通信库集群，使用 Kotlin 开发，兼容 IPv6

### 注意事项

1. 此项目未进行大规模测试，请勿用于生产环境
2. 此项目基于 JavaSE 8，在JavaSE 9可能存在问题！
3. 咸鱼开发者，随时咕咕咕

引用方案详见 [JitPack](https://jitpack.io/#ExplodingFKL/JNetServer)

### 此合集存在有：

1. HttpServer：一个轻量级 HTTP 1.1 服务器（不支持 SSL）
2. HdpServer: 对UDP 进行二次封装，简化通信结构 (兼容 广播、单播和组播)

## 构建

#### 构建要求：

1. Oracle JDK 8 或者 OpenJDK 8 +  OpenJFX 8
2. 磁盘剩余空间 512M 以上 

### Windows


```cmd
git clone https://github.com/ExplodingFKL/JNetServer
cd JNetServer
gradlew.bat build
```

### MacOS / Linux

```bash
git clone https://github.com/ExplodingFKL/JNetServer
cd JNetServer
./gradlew build
```


## 查看示例程序

项目附带了基于 JavaFX GUI 的示例，包含了项目大部分使用方法范例和 API ，可使用以下方法查看

### Windows

```cmd
git clone https://github.com/ExplodingFKL/JNetServer
cd JNetServer
gradlew.bat bootRun

```

### MacOS / Linux

```bash
git clone https://github.com/ExplodingFKL/JNetServer
cd JNetServer
./gradlew bootRun

```



## LICENSE

```
Copyright (c) 2013, Aldo Cortesi. All rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```