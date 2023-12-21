# ems-project
基于Vue3 + SpringBoot的教务管理系统

开发时间：`2023.12`

开发应用：`大三软件工程实验课大作业`

声明：<font color="red">仅供学习，禁止抄袭！</font>

## 1. 项目介绍

开发功能：

1. 登录 / 登出模块

![image-20231221115715301](.\README.assets\image-20231221115715301.png)

---

2. 学生模块

   1. 个人信息展示 / 修改密码

   2. 选课

   3. 已选课程查分 / 退课

   4. 请假申请 / 查看状态

   5. 学生已获学分统计

   6. 学生课程表

![image-20231221115741780](.\README.assets\image-20231221115741780.png)

![image-20231221115756404](.\README.assets\image-20231221115756404.png)

![image-20231221115803651](.\README.assets\image-20231221115803651.png)

![image-20231221115833622](.\README.assets\image-20231221115833622.png)

![image-20231221115814571](.\README.assets\image-20231221115814571.png)

![image-20231221115820816](.\README.assets\image-20231221115820816.png)

---

3. 教师模块

   1. 教师信息展示/修改密码

   2. 课程管理。开课/修改信息/删除课程
            查询课程

   3. 打分管理。按名字查询，打分。
   4. 请假管理。批准/驳回学生请假

![image-20231221115849022](.\README.assets\image-20231221115849022.png)

![image-20231221115856101](.\README.assets\image-20231221115856101.png)

![image-20231221115903970](.\README.assets\image-20231221115903970.png)

![image-20231221115911798](.\README.assets\image-20231221115911798.png)

---

4. 管理员模块

   1. 管理员信息展示/修改密码

   2. 学生管理。学生信息增删改查。

   3. 教师管理。教师信息增删改查。
   4. 开课申请。对教师申请的开课进行
          批准 / 驳回

![image-20231221115935762](.\README.assets\image-20231221115935762.png)

![image-20231221115941553](.\README.assets\image-20231221115941553.png)

![image-20231221115946735](.\README.assets\image-20231221115946735.png)

![image-20231221115954416](.\README.assets\image-20231221115954416.png)



## 2. 项目实现介绍

* 开发周期：六天

* 开发难点：

   ①前后端联调
   ②合理的数据库表设计
   ③业务逻辑

* 项目实现：

   ①只有前端课程表前端代码参考网上编写

  [参考课程表链接---感谢作者](https://blog.csdn.net/qq_35163460/article/details/128451842)

   ②其余主要代码均由个人实现



## 3. 技术选型

* 前端

   Vue3：用于接收和展示数据

   Pinia：状态管理工具。存储登录信息

   VueRouter：路由管理工具

   Axios：发送http请求，与后台交互

   ElementPlus：UI框架

   Echarts：基于 JavaScript 的开源可视化图表库

  ![image-20231221120059253](.\README.assets\image-20231221120059253.png)

* 后端

   SpringBoot2.5：用于接收http请求和处理

   MybatisPlus：单表和多表查询

  ![image-20231221120120042](.\README.assets\image-20231221120120042.png)

* 数据库

  MySQL8.0：存储表数据

  Redis：主要用来存储登录信息

  ![image-20231221120145651](.\README.assets\image-20231221120145651.png)

## 4. 目录结构说明

* ems-client文件夹：前端源代码
* ems-server文件夹：后端源代码
* ems.sql文件：数据库表文件，导入到MySQL即可使用
