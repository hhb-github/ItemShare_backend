# Zeabur 部署指南

## 概述

本指南详细说明如何将个人物品分享系统部署到 Zeabur 平台。系统采用前后端分离架构，需要分别部署前端和后端服务。

## 系统架构

```
┌─────────────────┐    ┌─────────────────┐
│   前端应用       │    │   后端API       │
│ (React + Nginx) │    │ (Spring Boot)   │
│                 │    │                 │
│ Port: 8080      │    │ Port: 8080      │
└─────────────────┘    └─────────────────┘
```

## 准备工作

### 1. 确认代码仓库

确保以下 GitHub 仓库已准备就绪：
- 前端仓库：https://github.com/hhb-github/ItemShare_front.git
- 后端仓库：https://github.com/hhb-github/ItemShare_backend.git

### 2. 确认部署文件

项目已包含以下 Zeabur 优化文件：
- `item-share-frontend/Dockerfile.zeabur`
- `item-share-frontend/nginx.zeabur.conf`
- `item-share-backend/Dockerfile.zeabur`
- `item-share-backend/settings.zeabur.xml`

## 前端部署步骤

### 1. 登录 Zeabur

1. 访问 [Zeabur](https://zeabur.com)
2. 使用 GitHub 账号登录
3. 授权 Zeabur 访问您的 GitHub 仓库

### 2. 创建前端服务

1. 在 Zeabur 控制台点击 "New Project"
2. 选择 "Import from GitHub"
3. 选择 `ItemShare_front` 仓库
4. 设置服务名称：`item-share-frontend`

### 3. 配置前端服务

#### 自动检测配置
Zeabur 会自动检测到 `Dockerfile.zeabur` 文件并配置：

```dockerfile
# 构建阶段
FROM node:18-alpine AS builder
WORKDIR /app
RUN npm config set registry https://registry.npmmirror.com/
COPY package*.json ./
RUN npm ci --only=production
COPY . .
RUN npm run build

# 生产阶段
FROM nginx:1.25-alpine
# ... 配置详情请参考 Dockerfile.zeabur
```

#### 环境变量设置
在 Zeabur 控制台中设置以下环境变量：

```
NODE_ENV=production
REACT_APP_API_URL=https://your-backend-domain.zeabur.app
REACT_APP_ENV=production
```

#### 端口配置
- 容器端口：8080
- HTTP 端口：80

### 4. 部署前端

1. 点击 "Deploy" 开始构建
2. 等待构建完成（约 3-5 分钟）
3. 获取前端服务域名：`https://item-share-frontend.zeabur.app`

## 后端部署步骤

### 1. 创建后端服务

1. 在同一个项目中，点击 "Add Service"
2. 选择 "Import from GitHub"
3. 选择 `ItemShare_backend` 仓库
4. 设置服务名称：`item-share-backend`

### 2. 配置后端服务

#### 自动检测配置
Zeabur 会自动检测到 `Dockerfile.zeabur` 文件并配置：

```dockerfile
# 构建阶段
FROM maven:3.9.4-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY settings.xml /usr/share/maven/ref/
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# 运行阶段
FROM eclipse-temurin:17-jre-alpine
# ... 配置详情请参考 Dockerfile.zeabur
```

#### 环境变量设置
在 Zeabur 控制台中设置以下环境变量：

```bash
# 数据库配置
SPRING_DATASOURCE_URL=jdbc:mysql://your-mysql-host:3306/items_share
SPRING_DATASOURCE_USERNAME=your-db-username
SPRING_DATASOURCE_PASSWORD=your-db-password
SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver

# JWT 配置
JWT_SECRET=your-jwt-secret-key
JWT_EXPIRATION=86400

# 应用配置
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8080

# 文件存储配置
FILE_STORAGE_TYPE=local
FILE_STORAGE_PATH=/app/files

# CORS 配置
CORS_ALLOWED_ORIGINS=https://item-share-frontend.zeabur.app
```

#### 端口配置
- 容器端口：8080
- HTTP 端口：8080

### 3. 部署后端

1. 点击 "Deploy" 开始构建
2. 等待构建完成（约 5-10 分钟）
3. 获取后端服务域名：`https://item-share-backend.zeabur.app`

## 数据库配置

### 1. 创建 MySQL 数据库

在 Zeabur 中添加 MySQL 服务：

1. 在项目中点击 "Add Service"
2. 选择 "MySQL"
3. 设置数据库名称：`items_share`
4. 记录数据库连接信息

### 2. 初始化数据库

在 MySQL 服务中执行以下 SQL 创建表结构：

```sql
-- 用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    avatar_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 物品表
CREATE TABLE items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    condition_status VARCHAR(20),
    price DECIMAL(10,2),
    location VARCHAR(100),
    owner_id BIGINT NOT NULL,
    images JSON,
    status VARCHAR(20) DEFAULT 'available',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- 收藏表
CREATE TABLE favorites (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_favorite (user_id, item_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);

-- 物品图片表
CREATE TABLE item_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    item_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (item_id) REFERENCES items(id)
);
```

## 域名配置

### 1. 配置自定义域名（可选）

1. 在 Zeabur 控制台中，进入服务设置
2. 点击 "Domains" 标签
3. 添加自定义域名
4. 配置 DNS 记录

### 2. 更新环境变量

将前端和后端的域名环境变量更新为实际域名：

```bash
# 前端环境变量
REACT_APP_API_URL=https://your-custom-backend-domain.com

# 后端环境变量
CORS_ALLOWED_ORIGINS=https://your-custom-frontend-domain.com
```

## 监控和维护

### 1. 日志查看

在 Zeabur 控制台中可以查看：
- 应用日志
- 构建日志
- 运行时日志

### 2. 性能监控

Zeabur 提供内置的监控功能：
- CPU 使用率
- 内存使用率
- 网络流量
- 响应时间

### 3. 自动扩缩容

Zeabur 支持自动扩缩容：
- 根据负载自动调整实例数量
- 配置最小和最大实例数
- 设置扩缩容策略

## 故障排查

### 1. 常见问题

#### 构建失败
- 检查 Dockerfile 语法
- 确认依赖文件存在
- 查看构建日志

#### 运行时错误
- 检查环境变量配置
- 查看应用日志
- 确认数据库连接

#### 跨域问题
- 确认 CORS 配置
- 检查前端 API 地址
- 验证后端 CORS 设置

### 2. 调试命令

在 Zeabur 控制台中可以执行调试命令：

```bash
# 进入容器
zeabur exec <service-name> -- /bin/sh

# 查看环境变量
env

# 查看日志
tail -f /app/logs/application.log
```

## 安全配置

### 1. 环境变量安全

- 使用 Zeabur 的环境变量管理
- 避免在代码中硬编码敏感信息
- 定期轮换密钥

### 2. 网络安全

- 启用 HTTPS
- 配置防火墙规则
- 限制数据库访问

### 3. 访问控制

- 配置管理员权限
- 实施访问日志
- 定期安全审计

## 性能优化

### 1. 前端优化

- 启用静态资源缓存
- 压缩图片资源
- 优化打包体积

### 2. 后端优化

- 配置数据库连接池
- 启用缓存机制
- 优化查询语句

### 3. 数据库优化

- 创建合适的索引
- 定期清理日志
- 监控慢查询

## 备份策略

### 1. 数据备份

```bash
# 数据库备份
mysqldump -h <host> -u <user> -p <database> > backup.sql

# 文件备份
tar -czf files_backup.tar.gz /app/files/
```

### 2. 配置备份

- 导出 Zeabur 配置
- 备份环境变量
- 保存 Dockerfile

## 更新部署

### 1. 蓝绿部署

1. 创建新的服务实例
2. 部署新版本到临时环境
3. 测试通过后切换流量
4. 删除旧实例

### 2. 滚动更新

1. 更新 Docker 镜像
2. 逐个替换容器实例
3. 确保服务不中断

## 支持和联系

如需技术支持，请联系：
- Zeabur 官方文档：https://docs.zeabur.com
- 项目 GitHub 仓库
- 技术支持邮箱

---

**部署完成后，您的个人物品分享系统将在 Zeabur 上运行，具备高可用性、自动扩缩容和监控功能。**