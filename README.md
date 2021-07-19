# 工程介绍

该工程为位置服务项目的后端部分。通过Telnet，每隔一段时间就从第三方服务器上获取设备的位置信息，并把这些位置信息存储到数据库中。另外，后端部分要为移动端部分和前端部分提供接口服务。

功能介绍：

- 定时从第三方服务器上获取数据并存储到数据库中
- 为移动端部分和前端部分提供登录认证和授权服务
- 为移动端部分和前端部分提供接口服务
  - 用户接口模块
    - 新增用户（为前端编写）
    - 用户注册（为移动端编写）
    - 删除用户
    - 查询所有用户
  - 设备接口模块
    - 查询某用户的所有设备
    - 为某用户增加设备
    - 为某用户删除设备
    - 新增设备
    - 删除设备
  - 位置信息接口模块
    - 根据编号查询某条位置信息
    - 查询某设备的所有位置信息
    - 查询某设备的最新位置信息
    - 查询某设备在某一时间段（日级）内的所有位置信息
    - 查询所有位置信息
    - 修改获取位置信息定时任务的触发时间
    - 下载所有位置信息（以Excel文件的形式）

# 开发环境

- `JDK8`

- `MySQL5`
- `IntelliJ IDEA 2019.2.2`

# 技术选型

- `Spring Boot`
- `Spring Security`
- `Quartz`
- `Log4j`
- `Commons-net`
- `MySQL`
- `Mybatis`
- `Mybatis PageHelper`
- `Swagger UI`
- `Druid`
- `FastJson`
- `EasyExcel`

# 本地运行

1. 

```bash
git clone 
```

2. 修改配置文件`application-dev.properties`中的数据库连接信息

```properties
spring.datasource.url=XXXX
spring.datasource.username=XXXX
spring.datasource.password=XXXX 
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

3. 启动

