# zunyuan

# 一、项目简介
实现用户端APP登录、操作，管理端预录入准备数据的简单流程，无用户注册、无管理端用户新增

项目架构：
前后端分离
APP和管理端H5实现，静态页面跳转
服务端基于gradle的项目


#二、项目目录结构：

#1、AppManage: 
  用户端页面，入口: car_logIn.html

#2、backManage:
  管理端页面，入口: login.html
  
#3、服务端：
  api-core：服务端项目入口，其他子模块都以jar包方式集成进此项目的war包
  m-download：预定义出来做下载相关实现，暂未启用
  m-login：管理登录验证的模块，功能太简单，需重构
  m-order：订单相关业务逻辑(直接与order表数据交互，数据结构和业务逻辑待重构) 
  m-org：机构相关
  m-product：商品相关，管理端录入，用户端销售，待重构
  m-sales-network：销售网点相关
  util：工具类和一些基础类，之前实现了一个短信发送模块
  
  
#三、构建：

命令行进入api-core目录，执行"gradle build"命令即可,编译成功会在build/libs目录下出现api-core-1.0-SNAPSHOT.war，改名成zunyuan.war,丢到tomcat8/webapps即可

api-core的build.gradle：（可以看出模块之间的依赖关系，有些子模块会用到其他子模块的功能，也需添加依赖）
dependencies {
    ……
    compile project(':util')
    compile project(':m-login')
    compile project(':m-sales-network')
    compile project(':m-product')
    compile project(':m-order')
    compile project(':m-downLoad')
    compile project(':m-org')
}
