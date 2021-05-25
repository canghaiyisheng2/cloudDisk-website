# 在线云盘系统

## 项目架构

Vue+Springboot+Mysql

## 项目本地部署运行注意事项

1. front-project目录下为Vue项目，已打包至后端资源目录下的static目录下
2. 本项目需要配置Mysql数据库，相关数据库表的创建设置语句见根目录下的CloudDisk.sql文件
3. 运行springboot源码前需配置连接本地数据库，可以在资源目录下的appllication-dev.properties文件中配置相应的url、username、password等
4. 运行springboot源码前需配置上传数据目录，可以在资源目录下的appllication-dev.properties文件中配置相应的location
