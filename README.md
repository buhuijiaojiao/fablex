# Fablex - 故事的集合地

## 项目概述
Fablex（fable-complex，故事的集合地）是一个基于SpringBoot3和Vue3开发的小说阅读网站项目。采用单体架构设计，为用户提供优质的小说阅读体验。

## 技术栈
- 后端：SpringBoot3、SpringMVC、MyBatis
- 前端：Vue3
- 数据库：MySQL
- 缓存：Redis
- 构建工具：Maven

## 主要功能
- 用户注册与登录系统
- 小说分类与检索
- 小说阅读与书架管理
- 用户评论与互动系统
- 阅读历史记录与推荐

## 项目特点
- 单体架构，便于部署与维护
- 响应式设计，适配多种终端设备
- Redis缓存机制，提高访问速度
- RESTful API设计规范

## 安装与运行
### 后端
```bash
# 克隆项目
git clone https://github.com/buhuijiaojiao/fablex.git

# 进入项目目录
cd fablex/backend

# 使用Maven构建项目
mvn clean install

# 运行项目
java -jar target/fablex-0.0.1-SNAPSHOT.jar
