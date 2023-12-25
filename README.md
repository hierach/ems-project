# ems-project
基于Vue3 + SpringBoot的教务管理系统

开发时间：`2023.12`

开发应用：`大三软件工程实验课大作业`

声明：<font color="red">仅供学习，禁止抄袭！</font>

## 1. 项目介绍

开发功能：

1. 登录 / 登出模块

![image-20231221115715301](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525926.png)

---

2. 学生模块

   1. 个人信息展示 / 修改密码

   2. 选课

   3. 已选课程查分 / 退课

   4. 请假申请 / 查看状态

   5. 学生已获学分统计

   6. 学生课程表

![image-20231225160607605](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312251606743.png)

![image-20231221115756404](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525928.png)

![image-20231221115803651](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525929.png)

![image-20231221115833622](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525930.png)

![image-20231221115814571](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525931.png)

![image-20231221115820816](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525932.png)

---

3. 教师模块

   1. 教师信息展示/修改密码

   2. 课程管理。开课/修改信息/删除课程
            查询课程

   3. 打分管理。按名字查询，打分。
   4. 请假管理。批准/驳回学生请假

![image-20231225160653190](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312251606285.png)

![image-20231221115856101](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525934.png)

![image-20231221115903970](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525936.png)

![image-20231221115911798](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525937.png)

---

4. 管理员模块

   1. 管理员信息展示/修改密码

   2. 学生管理。学生信息增删改查。

   3. 教师管理。教师信息增删改查。
   4. 开课申请。对教师申请的开课进行
          批准 / 驳回

![image-20231225160720828](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312251607922.png)

![image-20231221115941553](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525939.png)

![image-20231221115946735](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525940.png)

![image-20231221115954416](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525941.png)



## 2. 项目实现介绍

* 开发周期：六天

* 开发难点：

   ①前后端联调
   ②合理的数据库表设计
   ③业务逻辑

* 项目实现：

   ①前端课程表前端代码和登录校验器参考网上编写

  [参考课程表链接---感谢作者](https://blog.csdn.net/qq_35163460/article/details/128451842)

  [参考登录校验器---感谢作者](https://www.bilibili.com/video/BV1cr4y1671t/?spm_id_from=333.337.search-card.all.click&vd_source=3c261d2624d54bbcb7865ce4e2fa8d13)
  
   ②其余主要代码均由个人实现



## 3. 技术选型

* 前端

   Vue3：用于接收和展示数据

   Pinia：状态管理工具。存储登录信息

   VueRouter：路由管理工具

   Axios：发送http请求，与后台交互

   ElementPlus：UI框架

   Echarts：基于 JavaScript 的开源可视化图表库

  ![image-20231221120059253](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525942.png)

* 后端

   SpringBoot2.5：用于接收http请求和处理

   MybatisPlus：单表和多表查询

  ![image-20231221120120042](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525943.png)

* 数据库

  MySQL8.0：存储表数据

  Redis：主要用来存储登录信息

  ![image-20231221120145651](https://weifengqin-image.oss-cn-nanjing.aliyuncs.com/img/202312211525944.png)

## 4. 目录结构说明

* ems-client文件夹：前端源代码
* ems-server文件夹：后端源代码
* ems.sql文件：数据库表文件，导入到MySQL即可使用
