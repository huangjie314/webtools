# Svn Tool
①基于svn版本 1.9.4 (其他版本自行测试)   
②基于IDEA开发  
③最低兼容jdk1.7    

### 打包和运行方式
①生成war和jar包命令  
gradle build 

②运行jar包命令  (svntool.jar 由配置文件定义)  
java -jar svntool.jar --spring.profiles.active=prod 



