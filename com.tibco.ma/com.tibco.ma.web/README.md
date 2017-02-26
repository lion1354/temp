ma mobilePlatform项目 Gradle 结构项目SVN地址：https://192.168.81.254/svn/ma/trunk/com.tibco.ma，请按以下步骤操作：
1，安装gradle插件 gradle - http://dist.springsource.com/release/TOOLS/gradle
2，下载项目代码https://192.168.81.254/svn/ma/trunk/com.tibco.ma
3，导入子项目
4，全选所有项目，Ctrl+F5，刷新
5，选择com.tibco.ma.web项目， Ctrl+Shift+Alt+R 执行命令：jettyStart， 启动项目第一次启动，需要下载相关jar文件， 会比较慢
        执行命令：jettyStartDebug， 启动debug模式

http://localhost:80/mobilePlatform/app/views/index.html



http://dist.springsource.com/milestone/TOOLS/gradle

5005

gradle clean war

查询平台所有 rest api 信息，并可以进行调试
mobilePlatform/api-doc/index.html

test:
http://192.168.82.153:8080/mobilePlatform/

spring-data
http://docs.spring.io/spring-data/mongodb/docs/current/reference/html

研究mongodb数据库事务