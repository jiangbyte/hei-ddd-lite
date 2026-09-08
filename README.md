# hei-ddd-lite

轻量 **DDD-lite** 单体骨架：分层约定 + 基础抽象 + 可选基础设施自动配置。  
不是完整 DDD（无 Event Sourcing / 强制 CQRS / 复杂聚合编排）。

## 模块结构

```text
hei-ddd-lite/          # parent
├── domain/            # 领域约定 + 占位模型
├── application/       # 应用约定 + 占位应用服务
├── interfaces/        # 统一响应 / 全局异常 / JWT+@RequireLogin + Controller
├── infrastructure/    # 可选自动配置 + 仓储实现
└── bootstrap/         # 启动入口
```

依赖方向（单向）：

```text
bootstrap → interfaces → application → domain
bootstrap → infrastructure → domain / application
```

## 快速启动

需 **JDK 21**（仅当前终端设置 `JAVA_HOME`，不改 shell 配置）：

```bash
export JAVA_HOME=/home/charlie/Workspace/sdks/jdk-21
export PATH="$JAVA_HOME/bin:$PATH"
mvn install -DskipTests
mvn -pl bootstrap spring-boot:run
```

公开接口：

```bash
curl http://localhost:8080/hello
```

登录并访问需鉴权接口：

```bash
TOKEN=$(curl -s -X POST 'http://localhost:8080/auth/login?username=admin' | sed -n 's/.*"token":"\([^"]*\)".*/\1/p')
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/hello/secure
```

## 分层约定

| 层 | 放什么 | 不放什么 |
|----|--------|----------|
| domain | 实体/聚合、仓储接口、领域服务、领域异常 | Spring、HTTP、SQL |
| application | 用例编排、事务边界 | 对外 DTO、技术细节 |
| interfaces | Controller、DTO、Assembler、统一响应、`@RequireLogin` / JWT | 业务规则、持久化 |
| infrastructure | 仓储实现、Redis/MP/MinIO 等 | 领域规则 |

Controller 上使用：

```java
@RequireLogin
@GetMapping("/xxx")
public R<Void> xxx() { ... }
```

## 可选基础设施

`infrastructure` 中通过 **classpath + 配置开关** 启用。配置示例见 `bootstrap/src/main/resources/application.yml`。

| 能力 | 依赖示例 | 开关 |
|------|----------|------|
| 数据源 | `druid-spring-boot-4-starter` + 驱动 | `hei.ddd.datasource.enabled` |
| MyBatis-Plus | `mybatis-plus-spring-boot4-starter` | `hei.ddd.mybatis.enabled` |
| Redis | `spring-boot-starter-data-redis` | `hei.ddd.redis.enabled` |
| JWT | 已内置于 `interfaces`（jjwt） | `hei.ddd.jwt.enabled` |
| MinIO | `minio` | `hei.ddd.minio.enabled` |

版本在父 POM 的 `<properties>` 中用 `${xxx.version}` 统一管理（如 `lombok.version`、`hutool.version`、`mapstruct.version`、`jjwt.version`、`mybatis-plus.version`、`druid.version`、`minio.version`）。

## 与完整 DDD 的差异

**做：** 四层分包、仓储接口倒置、应用服务编排、统一响应、自实现 JWT 注解鉴权、可选基础设施。  
**不做：** Event Sourcing、Saga、强制 CQRS、ACL 脚手架、多限界上下文拆分。

## 技术栈

- Java 21 / Spring Boot 4.1.x / Maven 多模块
- Lombok / Hutool / MapStruct / JJWT
- Druid / MyBatis-Plus / Redis / MinIO（可选）
