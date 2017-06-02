# Svn Tool
①基于svn命令行,需要安装svn命令行工具.  
②基于IDEA开发,请自行测试其他IDE

### 打包和运行方式
③打包命令
gradle build -x text
④启动命令(svntool-1.0.jar 为jar包的名,根据配置文件修改)  
java -jar svntool-1.0.jar --spring.profiles.active=prod 
⑤端口:8888(可在配置文件修改)

### 开发日志:  
##### 2017.06.01
①兼容jdk1.6  
①实现基本功能  
②增加了登录校验  
③待完善:检索功能和按日期分类  

##### 2017.06.02  
①兼容jdk1.6