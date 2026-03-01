# 瑜伽场馆课程预约系统

一个完整的瑜伽场馆课程预约系统，包含用户端微信小程序和管理后台。

## 项目结构

```
yoga/
├── yoga-common/           # Maven公共库
├── yoga-front-api/        # Spring Boot用户端API
├── yoga-backend-api/      # Spring Boot管理端API
├── yoga-front/            # uni-app微信小程序
└── yoga-backend/          # Vue3+Element Plus管理后台
```

## 技术栈

### 后端
- **Java 17**
- **Spring Boot 3.2.x**
- **MyBatis-Plus 3.5.x**
- **Spring Security + JWT**
- **Redis** (Token黑名单/Session缓存)
- **WebSocket** (STOMP实时推送)
- **Knife4j** (Swagger UI)
- **EasyExcel** (数据导出)
- **MySQL 8.0**

### 前端
- **uni-app** (微信小程序)
- **Vue 3** + **Element Plus** (管理后台)
- **Pinia** (状态管理)
- **Axios** (HTTP请求)
- **Vite** (构建工具)

## 模块说明

### yoga-common
公共库，包含：
- 统一响应体 `Result<T>`
- 业务错误码枚举 `ResultCode`
- 分页请求/响应 `PageRequest`/`PageResponse`
- 业务异常 `BusinessException`
- 全局异常处理 `GlobalExceptionHandler`
- JWT工具类 `JwtUtils`
- 系统常量 `Constants`
- 13张表实体类
- 业务DTO

### yoga-front-api (端口: 8081)
用户端API，提供：
- 微信登录认证
- 课程列表/详情/搜索
- 预约管理
- 订单/支付
- 签到（扫码）
- 评价
- WebSocket实时推送

### yoga-backend-api (端口: 8082)
管理端API，提供：
- 管理员认证
- 场馆管理
- 教练管理
- 课程模板管理
- 排课规则管理
- 课程排期管理
- 预约/订单管理
- 数据统计
- 操作日志

### yoga-front
uni-app微信小程序，包含：
- 首页（推荐课程/轮播）
- 课程列表/详情
- 预约/支付
- 我的预约/签到
- 用户中心

### yoga-backend
Vue3管理后台，包含：
- 数据仪表盘
- 场馆管理
- 教练管理
- 课程管理
- 排课管理
- 预约/订单管理
- 评价管理
- 用户管理
- 通知管理

## 快速开始

### 前置条件
- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6+
- Node.js 18+
- 微信开发者工具

### 后端启动

```bash
# 1. 初始化数据库
mysql -u root -p < yoga-front-api/src/main/resources/db/schema.sql

# 2. 安装公共库
cd yoga
mvn install -pl yoga-common

# 3. 启动用户端API
mvn spring-boot:run -pl yoga-front-api

# 4. 启动管理端API（新终端）
mvn spring-boot:run -pl yoga-backend-api
```

### 前端启动

```bash
# 用户端小程序
cd yoga/yoga-front
npm install
# 使用微信开发者工具打开项目

# 管理后台
cd yoga/yoga-backend
npm install
npm run dev
```

## 接口文档

- 用户端API: http://localhost:8081/doc.html
- 管理端API: http://localhost:8082/doc.html

## 数据库设计

系统包含13张核心表：
1. `users` - 用户表
2. `roles` - 角色表
3. `user_roles` - 用户角色关联表
4. `venues` - 场馆表
5. `coaches` - 教练表
6. `course_templates` - 课程模板表
7. `course_schedules` - 排课规则表
8. `course_sessions` - 课程排期表
9. `bookings` - 预约表
10. `orders` - 订单表
11. `checkins` - 签到表
12. `reviews` - 评价表
13. `notifications` - 通知表
14. `operation_logs` - 操作日志表

## 角色权限

- `SYS_ADMIN` - 系统管理员
- `VENUE_ADMIN` - 场馆管理员
- `COACH` - 教练
- `USER` - 普通用户

## License

MIT
