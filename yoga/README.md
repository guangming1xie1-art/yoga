# 瑜伽场馆课程预约系统

完整工程骨架，包含 5 个子工程：

| 工程 | 技术栈 | 描述 |
|------|--------|------|
| yoga-common | Maven JAR | 公共库：实体/DTO/工具/异常 |
| yoga-front-api | Spring Boot | 用户端 API |
| yoga-backend-api | Spring Boot | 管理端 API |
| yoga-front | uni-app | 微信小程序 |
| yoga-backend | Vue3 + Element Plus | 管理后台 |

## 快速开始

### 前置条件
- JDK 17+
- Maven 3.8+
- Node.js 18+ (for frontend)
- MySQL 8.0+
- Redis 6+

### 构建后端

```bash
# 安装公共库
cd yoga
mvn install -pl yoga-common

# 启动用户端 API (端口 8081)
mvn spring-boot:run -pl yoga-front-api

# 启动管理端 API (端口 8082)
mvn spring-boot:run -pl yoga-backend-api
```

### 前端开发

```bash
# 微信小程序
cd yoga-front
npm install
npm run dev:mp-weixin

# 管理后台
cd yoga-backend
npm install
npm run dev
```

## 项目结构

```
yoga/
├── yoga-common/           # 公共库
│   └── src/main/java/com/yoga/common/
│       ├── entity/         # 13张表实体
│       ├── dto/            # 业务DTO
│       ├── result/         # 统一响应
│       ├── exception/      # 异常处理
│       ├── util/           # 工具类
│       └── constant/       # 常量
│
├── yoga-front-api/        # 用户端 API
│   └── src/main/java/com/yoga/front/
│       ├── config/         # 配置类
│       ├── security/       # JWT安全
│       └── module/         # 业务模块
│
├── yoga-backend-api/       # 管理端 API
│   └── src/main/java/com/yoga/backend/
│
├── yoga-front/             # 微信小程序
│   ├── pages/              # 页面
│   ├── api/                # API请求
│   ├── store/              # 状态管理
│   └── utils/              # 工具
│
└── yoga-backend/           # Vue3管理后台
    ├── src/
    │   ├── views/          # 页面
    │   ├── api/             # API
    │   ├── router/         # 路由
    │   └── store/          # 状态
    └── package.json
```

## API 文档

- 用户端：http://localhost:8081/doc.html
- 管理端：http://localhost:8082/doc.html

## 数据库

执行 `yoga-front-api/src/main/resources/db/schema.sql` 初始化数据库。
